package shop.ineed.app.ineed.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.adapter.ViewPagerAdapter;
import shop.ineed.app.ineed.components.ViewPagerCustom;
import shop.ineed.app.ineed.domain.util.LibraryClass;
import shop.ineed.app.ineed.fragments.AccountFragment;
import shop.ineed.app.ineed.fragments.DisconnectedFromAccountFragment;
import shop.ineed.app.ineed.fragments.HomeFragment;
import shop.ineed.app.ineed.fragments.ListCategoriesFragment;
import shop.ineed.app.ineed.fragments.StoresFragment;
import shop.ineed.app.ineed.util.BottomNavigationViewHelper;

public class ContainerActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ViewPagerCustom viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        viewPager = (ViewPagerCustom) findViewById(R.id.containerViewPager);
        BottomNavigationView navigation = findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(this);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.offsetLeftAndRight(3);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setScrollHorizontal(false);
        setupViewPager(viewPager);

        if (savedInstanceState == null) {
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.ic_action_search:
                Toast.makeText(this, "ic_action_search", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, SearchActivity.class));
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
        switch (item.getItemId()) {
            case R.id.navigation_home:
                viewPager.setCurrentItem(0);
                return true;
            case R.id.navigation_list_products:
                viewPager.setCurrentItem(1);
                return true;
            case R.id.navigation_stores:
                viewPager.setCurrentItem(2);
                return true;
            case R.id.navigation_account:
                viewPager.setCurrentItem(3);
                return true;
        }
        return false;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new ListCategoriesFragment());
        adapter.addFragment(new StoresFragment());

        if (LibraryClass.isUserLogged(this)) {
            adapter.addFragment(new AccountFragment());
        } else {
            adapter.addFragment(new DisconnectedFromAccountFragment());
        }

        viewPager.setAdapter(adapter);
    }
}
