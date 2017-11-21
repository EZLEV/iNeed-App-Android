package shop.ineed.app.ineed.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.crash.FirebaseCrash;

import java.util.Arrays;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.domain.User;
import shop.ineed.app.ineed.util.FirebaseError;
import shop.ineed.app.ineed.util.PreferenceUtils;

public class ChooseInputMethodActivity extends CommonSubscriberActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN_GOOGLE = 7859;

    private String TAG = this.getClass().getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private User mUser;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_input_method);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = getFirebaseResultHandle();
        mUser = new User();

        // Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        // Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessFacebookLoginData(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(), "", Toast.LENGTH_LONG).show();
                FirebaseCrash.log(TAG + ":onCancelFacebook");
            }

            @Override
            public void onError(FacebookException error) {
                FirebaseCrash.report(error);
                Snackbar.make(findViewById(R.id.choose_container),
                        "Erro ao tentar fazer login usando a sua conta do Facebook.\n",
                        Snackbar.LENGTH_LONG)
                        .setAction("OK", view -> {

                        }).show();
            }
        });

    }

    // Monitora a alteração do status do usuário
    private FirebaseAuth.AuthStateListener getFirebaseResultHandle() {
        FirebaseAuth.AuthStateListener callback = firebaseAuth -> {
            FirebaseUser userFirebase = firebaseAuth.getCurrentUser();

            if (userFirebase == null) {
                return;
            }

            if (mUser.getUid() == null && isNameOk(mUser, userFirebase)) {
                mUser.setUid(userFirebase.getUid());
                mUser.setName(userFirebase.getDisplayName());
                mUser.setEmail(userFirebase.getEmail());
                mUser.saveUserLogged();
            }
            callContainerActivity();
        };
        return (callback);
    }

    // Verifica o nome do usuario está ok
    private boolean isNameOk(User user, FirebaseUser firebaseUser) {
        return (user.getName() != null || firebaseUser.getDisplayName() != null);
    }


    /*
     * Métodos que trata a questão de login com e-mail e senha, login pelo Facebook e Google.
     */
    private void accessFacebookLoginData(AccessToken accessToken) {
        saveLoginDataFirebase("facebook", (accessToken != null ? accessToken.getToken() : null));
    }

    private void accessGoogleLoginData(String accessToken) {
        saveLoginDataFirebase("google", accessToken);
    }

    /*
     * Faz login no firebase usando crendenciais dos provedores: Facebook ou Google.
     */
    private void saveLoginDataFirebase(String provider, String... tokens) {
        if (tokens != null && tokens.length > 0 && tokens[0] != null) {
            AuthCredential credential = FacebookAuthProvider.getCredential(tokens[0]);
            credential = provider.equalsIgnoreCase("google") ? GoogleAuthProvider.getCredential(tokens[0], null) : credential;

            mUser.saveProviderUserLogged(ChooseInputMethodActivity.this, "");
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            String messageError = FirebaseError.verify(ChooseInputMethodActivity.this, task);
                            showSnackbar(R.id.choose_container, messageError);
                            return;
                        }
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        mUser.saveProviderUserLogged(ChooseInputMethodActivity.this, currentUser.getUid());

                        PreferenceUtils.setUserId(getBaseContext(), currentUser.getUid());
                        PreferenceUtils.setNickname(getBaseContext(), currentUser.getDisplayName());

                        connectToSendBird(currentUser.getUid(), currentUser.getDisplayName(), getBaseContext());

                        callContainerActivity();
                    })
                    .addOnFailureListener(e -> {
                        FirebaseCrash.report(e);
                        Log.e(TAG, e.getMessage());
                    });
        } else {
            mAuth.signOut();
        }
    }

    // Ações de clique dos botões existente na Activity.
    public void sendEmail(View view) {
        FirebaseCrash.log(TAG + ":sendEmail");
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void sendFacebook(View view) {
        FirebaseCrash.log(TAG + ":sendFacebook");
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
    }

    public void sendGoogle(View view) {
        FirebaseCrash.log(TAG + ":sendGoogle");
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_SIGN_IN_GOOGLE);
    }

    public void btnSignInGuest(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void callContainerActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // Verificação da existência de um usuário conectado
    private void verifyLogged() {
        if (mAuth.getCurrentUser() != null) {
            callContainerActivity();
        } else {
            mAuth.addAuthStateListener(mAuthStateListener);
        }
    }

    private void showSnackbar(int idContainer, String message) {
        Snackbar.make(findViewById(idContainer),
                message,
                Snackbar.LENGTH_LONG)
                .setAction("OK", view -> {

                }).show();
    }

    // Recebe o resultado do login do Google ou Facebook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult");
        FirebaseCrash.log(TAG + ":onActivityResult");

        if (requestCode == RC_SIGN_IN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "GoogleSignInResult");
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                assert account != null;
                Log.d(TAG, "isSuccess");
                accessGoogleLoginData(account.getIdToken());
            }
        } else {
            Log.i(TAG, data.getData() + "");
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        FirebaseCrash.report(new Exception(
                connectionResult.getErrorCode() + " : " + connectionResult.getErrorMessage()
        ));
        Toast.makeText(getBaseContext(), getResources().getString(R.string.error_connection_google_services), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        verifyLogged();
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
        email = new EditText(getBaseContext());
        password = new EditText(getBaseContext());
    }

    @Override
    protected void initUser() {

    }
}
