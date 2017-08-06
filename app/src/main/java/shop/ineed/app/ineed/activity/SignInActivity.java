package shop.ineed.app.ineed.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dd.CircularProgressButton;

import shop.ineed.app.ineed.R;

public class SignInActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private CircularProgressButton buttonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        buttonSignIn = (CircularProgressButton) findViewById(R.id.btn_sign_in);
        buttonSignIn.setIndeterminateProgressMode(true);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonSignIn.getProgress() == 0) {
                    buttonSignIn.setProgress(50);
                } else if (buttonSignIn.getProgress() == 100) {
                    buttonSignIn.setProgress(0);
                } else {
                    buttonSignIn.setProgress(100);
                }
            }
        });
    }

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
}
