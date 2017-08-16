package shop.ineed.app.ineed.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cn.pedant.SweetAlert.SweetAlertDialog;
import livroandroid.lib.utils.AndroidUtils;
import shop.ineed.app.ineed.R;

/**
 * Execução de alguma lógica, verificar acesso a internet entre outros.
 */

public class SplashScreenActivity extends AppCompatActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 3000;
    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "SplashScreenActivity.Handler().postDelayed");
                Intent intent = new Intent(SplashScreenActivity.this, ChooseInputMethodActivity.class);


                if (AndroidUtils.isNetworkAvailable(getBaseContext())) {
                    startActivity(intent);
                }else{
                    show();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public void show() {
       new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
               .setTitleText("Atenção")
               .setContentText("Fé é como WI-FI: invisível, mas tem o poder de te conectar com o que você precisa. iNeed s2\n Tente estabelecer uma conexão com a internet")
               .setCustomImage(R.drawable.ic_portable_wifi_off)
               .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                   @Override
                   public void onClick(SweetAlertDialog sweetAlertDialog) {
                       finish();
                   }
               })
               .show();
    }
}
