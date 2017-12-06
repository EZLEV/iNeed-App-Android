package shop.ineed.app.ineed.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import livroandroid.lib.utils.AndroidUtils;
import shop.ineed.app.ineed.R;

/**
 * Execução de alguma lógica, verificar acesso a internet entre outros.
 */

public class SplashScreenActivity extends BaseActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 3000;
    private String TAG = this.getClass().getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private boolean isLogged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if(user != null){
                isLogged = true;

                Log.d(TAG, "SplashScreenActivity.Handler().showContainer");
            }else{
                isLogged = false;
                Log.d(TAG, "SplashScreenActivity.Handler().showChooseMethod");
            }
        };

        new Handler().postDelayed(() -> {
            Log.d(TAG, "SplashScreenActivity.Handler().postDelayed");
            verifyMethod();
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private void showContainer(){
        Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void showChooseMethod(){
        Intent intent = new Intent(SplashScreenActivity.this, ChooseInputMethodActivity.class);
        startActivity(intent);
        finish();
    }

    private void verifyMethod(){
        if (AndroidUtils.isNetworkAvailable(getBaseContext())) {
            if(isLogged){
                showContainer();
            }else{
                showChooseMethod();
            }
        }else{
            showMSGErrorConnection();
        }
    }

    private void showMSGErrorConnection() {
        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        dialog.setTitleText("Atenção")
               .setContentText("sua conexão com a internet está inativa.\nToque no OK para tentar novamente.")
               .setCustomImage(R.drawable.ic_wifi_off)
               .setConfirmClickListener(sweetAlertDialog -> {
                   dialog.dismiss();
                   verifyMethod();
               })
               .show();
    }
}
