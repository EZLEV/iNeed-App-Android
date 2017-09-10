package shop.ineed.app.ineed.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.fragments.AccountFragment;
import shop.ineed.app.ineed.fragments.HomeFragment;
import shop.ineed.app.ineed.fragments.ListCategoriesFragment;

public class ContainerActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView mNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(this);

        if(savedInstanceState == null){
            HomeFragment fragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()){
            case R.id.navigation_home :
                fragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                return true;
            case R.id.navigation_list_products:
                fragment = new ListCategoriesFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                return true;
            case R.id.navigation_account:
                fragment = new AccountFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                return true;
        }
        return false;
    }
}
