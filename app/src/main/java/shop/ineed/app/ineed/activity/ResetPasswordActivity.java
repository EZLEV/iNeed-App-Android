package shop.ineed.app.ineed.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.util.List;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.domain.User;

public class ResetPasswordActivity extends CommonSubscriberActivity implements Validator.ValidationListener {

    private FirebaseAuth mAuth;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        getSupportActionBar().setHomeButtonEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        validator = new Validator(this);
        validator.setValidationListener(this);

        initViews();
    }

    public void btnSend(View view) {
        validator.validate();
    }

    public void onClickClose(View view) {
        finish();
    }

    private void reset() {
        mAuth.sendPasswordResetEmail(mUser.getEmail())
                .addOnCompleteListener(task -> {
                    closeProgressDialog();
                    if (task.isSuccessful()) {
                        email.setText("");
                        showToast(ResetPasswordActivity.this, "E-mail enviado para " + mUser.getEmail());
                        finish();
                    } else {
                        showToast(ResetPasswordActivity.this, "Falhou! Tente novamente. ");
                    }

                })
                .addOnFailureListener(e -> FirebaseCrash.report(e));
    }

    @Override
    protected void initViews() {
        email = (EditText) findViewById(R.id.email);
        password = new EditText(ResetPasswordActivity.this);
    }

    @Override
    protected void initUser() {
        mUser = new User();
        mUser.setEmail(email.getText().toString());
    }

    @Override
    public void onValidationSucceeded() {
        openProgressDialog(ResetPasswordActivity.this);
        initUser();
        reset();
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
}
