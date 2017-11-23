package shop.ineed.app.ineed.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.wang.avi.AVLoadingIndicatorView;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.domain.User;
import shop.ineed.app.ineed.domain.util.LibraryClass;
import shop.ineed.app.ineed.util.Base64;

public class UpdatePersonalDataActivity extends CommonSubscriberActivity implements View.OnClickListener, Validator.ValidationListener {

    @NotEmpty(message = "Nome inválido")
    private EditText name;
    private ImageView ivProfileUpdate;

    private Bitmap ivProfileBitmap;
    private AVLoadingIndicatorView progressSettingsUser;
    private ScrollView containerSettingsUser;
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 13;

    private String uid;

    private Map<String, Object> mapUser;

    private static final int RESULT_LOAD_IMAGE = 19;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_personal_data);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.personal_data));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uid = getIntent().getStringExtra("profile");

        validator = new Validator(this);
        validator.setValidationListener(this);

        findViewById(R.id.btnSaveUpdate).setOnClickListener(view -> validator.validate());
        findViewById(R.id.btnCancelUpdate).setOnClickListener(view -> finish());

        initViews();
        loadProfile();
    }

    private void loadProfile() {
        DatabaseReference reference = LibraryClass.getFirebase().child("consumers").child(uid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                name.setText(user.getName());
                email.setText(user.getEmail());
                if (user.getImage() != null) {
                    ivProfileUpdate.setImageBitmap(Base64.convertToBitmap(user.getImage()));
                }

                if (progressSettingsUser.getVisibility() == View.VISIBLE) {
                    progressSettingsUser.setVisibility(View.INVISIBLE);
                }

                containerSettingsUser.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void initViews() {
        email = (EditText) findViewById(R.id.emailUpdate);
        name = (EditText) findViewById(R.id.nameUpdate);
        ivProfileUpdate = (ImageView) findViewById(R.id.ivProfileUpdate);
        ivProfileUpdate.setOnClickListener(this);
        progressSettingsUser = (AVLoadingIndicatorView) findViewById(R.id.progressSettingsUser);
        containerSettingsUser = (ScrollView) findViewById(R.id.containerSettingsUser);

        password = new EditText(UpdatePersonalDataActivity.this);
    }

    @Override
    protected void initUser() {
        mapUser = new HashMap<>();
        mapUser.put("email", email.getText().toString());
        mapUser.put("name", name.getText().toString());
        if (ivProfileBitmap != null) {
            mapUser.put("image", Base64.convertToBase64(ivProfileBitmap));
        }
    }

    @Override
    public void onClick(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermissions();
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RESULT_LOAD_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap img = BitmapFactory.decodeFile(picturePath);
            ivProfileBitmap = Bitmap.createScaledBitmap(img, 300, 300, false);

            ivProfileUpdate.setImageBitmap(ivProfileBitmap);
            Log.d("IMAGE", picturePath);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onValidationSucceeded() {
        openProgressDialog(UpdatePersonalDataActivity.this);
        this.initUser();
        this.updateUser();
    }

    private void updateUser() {
        DatabaseReference reference = LibraryClass.getFirebase().child("consumers").child(uid);
        reference.updateChildren(mapUser).addOnCompleteListener(task -> {
            closeProgressDialog();
            if (task.isSuccessful()) {
                showToast(getBaseContext(), getResources().getString(R.string.update_success_account));
                finish();
            } else {
                showSnackbar(ivProfileUpdate, getResources().getString(R.string.update_error_account));
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
        }
    }

    private void requestStoragePermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Permissao")
                    .setContentText("As permissões de acesso ao armazenamento são necessárias para carregar / baixar arquivos.")
                    .setCancelText("Cancelar")
                    .setConfirmText("OK")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            ActivityCompat.requestPermissions(UpdatePersonalDataActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSION_WRITE_EXTERNAL_STORAGE);
                            sweetAlertDialog.cancel();
                        }
                    })
                    .show();

        } else {
            // Permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(UpdatePersonalDataActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
    }
}
