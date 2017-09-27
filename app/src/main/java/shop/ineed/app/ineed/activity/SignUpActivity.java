package shop.ineed.app.ineed.activity;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.domain.User;
import shop.ineed.app.ineed.util.FirebaseError;

public class SignUpActivity extends CommonSubscriberActivity implements DatabaseReference.CompletionListener, Validator.ValidationListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private User mUser;
    @NotEmpty(message = "Preencha o campo nome.")
    private EditText name;
    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setHomeButtonEnabled(true);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = firebaseAuth -> {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            if (firebaseUser == null || mUser.getUid() != null) {
                return;
            }
            mUser.setUid(firebaseUser.getUid());
            mUser.saveUserLogged(SignUpActivity.this);
        };

        validator = new Validator(this);
        validator.setValidationListener(this);

        initViews();
    }


    private void saveUser() {
        FirebaseCrash.log(TAG + ":saveUser");
        mAuth.createUserWithEmailAndPassword(
                mUser.getEmail(),
                mUser.getPassword()
        ).addOnCompleteListener(task -> {
            closeProgressDialog();
            if (task.isSuccessful()) {
                showToast(getBaseContext(), "Usuário cadastrado com sucesso!");
                finish();
            } else {
                String messageError = FirebaseError.verify(SignUpActivity.this, task);
                showSnackbar(findViewById(android.R.id.content), messageError);
            }
        }).addOnFailureListener(this, e -> {
            Log.e(SignUpActivity.class.getSimpleName(), e.getMessage());
            FirebaseCrash.report(e);
        });
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        FirebaseCrash.log(TAG + ":onComplete");
        mAuth.signOut();
        closeProgressDialog();
    }

    public void btnSignUp(View view) {
        validator.validate();
    }

    public void onClickClose(View view) {
        finish();
    }

    private void signUp() {
        FirebaseCrash.log(TAG + ":signUp");
        openProgressDialog(this);
        initUser();
        saveUser();
    }

    @Override
    public void onValidationSucceeded() {
        signUp();
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

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void initViews() {
        name = (EditText) findViewById(R.id.full_name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
    }

    @Override
    protected void initUser() {
        mUser = new User();
        mUser.setName(name.getText().toString());
        mUser.setEmail(email.getText().toString());
        mUser.setPassword(password.getText().toString());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
