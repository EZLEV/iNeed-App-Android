package shop.ineed.app.ineed.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import shop.ineed.app.ineed.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.ic_action_search:
                Toast.makeText(this, "ic_action_search", Toast.LENGTH_LONG).show();
                break;
            case R.id.ic_action_settings:
                Toast.makeText(this, "ic_action_settings", Toast.LENGTH_LONG).show();
                break;
            case R.id.ic_action_help:
                Toast.makeText(this, "ic_action_help", Toast.LENGTH_LONG).show();
                break;
            case R.id.ic_action_about:
                Toast.makeText(this, "ic_action_about", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
