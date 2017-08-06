package shop.ineed.app.ineed.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dd.CircularProgressButton;

import shop.ineed.app.ineed.R;

public class SignUpActivity extends AppCompatActivity {

    private CircularProgressButton buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        buttonSignUp = (CircularProgressButton) findViewById(R.id.btn_sign_up);
        buttonSignUp.setIndeterminateProgressMode(true);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonSignUp.getProgress() == 0) {
                    buttonSignUp.setProgress(50);
                } else if (buttonSignUp.getProgress() == 100) {
                    buttonSignUp.setProgress(0);
                } else {
                    buttonSignUp.setProgress(100);
                }
            }
        });
    }

    public void onClickClose(View view) {
        finish();
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
