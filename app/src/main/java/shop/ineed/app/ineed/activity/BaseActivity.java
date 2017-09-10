package shop.ineed.app.ineed.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import shop.ineed.app.ineed.R;

/**
 * Created by jose on 9/8/17.
 */

public class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    protected void enableToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(mToolbar != null){
            setSupportActionBar(mToolbar);
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }
}
