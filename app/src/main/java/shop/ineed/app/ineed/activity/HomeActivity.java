package shop.ineed.app.ineed.activity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.fragments.HomeFragment;

public class HomeActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        enableToolbar();
        setupNavigationDrawer();

        getToolbar().getNavigationIcon().setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Adiciona o fragment com o mesmo Bundle (args) da intent
        if (savedInstanceState == null) {
            HomeFragment frag = new HomeFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.containerHome, frag).commit();
        }
    }
}
