package shop.ineed.app.ineed.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.util.List;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.domain.User;
import shop.ineed.app.ineed.util.FirebaseError;
import shop.ineed.app.ineed.util.PreferenceUtils;

public class SignInActivity extends CommonSubscriberActivity implements Validator.ValidationListener {

    private FloatingActionButton fab;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        getSupportActionBar().setHomeButtonEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = getFirebaseAuthResultHandler();

        validator = new Validator(this);
        validator.setValidationListener(this);

        initViews();
    }

    private FirebaseAuth.AuthStateListener getFirebaseAuthResultHandler() {
        FirebaseAuth.AuthStateListener callback = firebaseAuth -> {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();

            if (currentUser == null) {
                return;
            }

            if (mUser.getUid() == null && isNameOk(mUser, currentUser)) {
                mUser.setUid(currentUser.getUid());
                Log.i("TAG", "Firebase GOOGLE" + currentUser.getUid());
                mUser.setName(currentUser.getDisplayName());
                mUser.setEmail(currentUser.getEmail());
                mUser.updateUserLogged();
            }

        };
        return (callback);
    }

    // Verifica o nome do usuario está ok
    private boolean isNameOk(User user, FirebaseUser firebaseUser) {
        return (user.getName() != null || firebaseUser.getDisplayName() != null);
    }

    public void btnSignIn(View view) {
        validator.validate();
    }

    public void resetPassword(View view) {
        startActivity(new Intent(SignInActivity.this, ResetPasswordActivity.class));
    }

    // Chama a tela de SignUp
    public void onClickGoSignUp(View view) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
            ActivityCompat.startActivity(this, new Intent(this, SignUpActivity.class), options.toBundle());
        } else {
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
            ActivityCompat.startActivity(this, new Intent(this, SignUpActivity.class), options.toBundle());
        }
    }

    private void signIn() {
        mUser.saveProviderUserLogged(SignInActivity.this, "");
        mAuth.signInWithEmailAndPassword(mUser.getEmail(), mUser.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(this.getClass().getSimpleName(), "signInWithEmail:onComplete:" + task.isSuccessful());
                        closeProgressDialog();
                        if (!task.isSuccessful()) {
                            String messageError = FirebaseError.verify(SignInActivity.this, task);
                            showSnackbar(findViewById(android.R.id.content), messageError);
                            return;
                        }
                        FirebaseUser currentUser = mAuth.getCurrentUser();

                        mUser.saveProviderUserLogged(SignInActivity.this, currentUser.getUid());

                        PreferenceUtils.setUserId(getBaseContext(), currentUser.getUid());
                        PreferenceUtils.setNickname(getBaseContext(), currentUser.getDisplayName());

                        connectToSendBird(PreferenceUtils.getUserId(getBaseContext()), PreferenceUtils.getNickname(getBaseContext()), getBaseContext());

                        callHomeContainer();
                    }
                })
                .addOnFailureListener(this, e -> FirebaseCrash.report(e));
    }

    private void callHomeContainer() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
    }

    @Override
    protected void initUser() {
        mUser = new User();
        mUser.setEmail(email.getText().toString());
        mUser.setPassword(password.getText().toString());
    }

    @Override
    public void onValidationSucceeded() {
        openProgressDialog(SignInActivity.this);
        this.initUser();
        this.signIn();
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
