package shop.ineed.app.ineed.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.adapter.ViewPagerAdapter;
import shop.ineed.app.ineed.fragments.FavoritesFragment;

public class FavoritesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        enableToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.favorites));

        ViewPager pager = (ViewPager) findViewById(R.id.favoritesViewPager);
        setupViewPager(pager);


        TabLayout tabFavorites = (TabLayout) findViewById(R.id.favoritesTabs);
        tabFavorites.setupWithViewPager(pager);
        tabFavorites.getTabAt(0).setText(getBaseContext().getString(R.string.products));
        tabFavorites.getTabAt(1).setText(getBaseContext().getString(R.string.store));

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        FavoritesFragment listProductsLikeFragment = new FavoritesFragment();
        Bundle bundleProducts = new Bundle();
        bundleProducts.putString("typeLike", "liked-products");
        listProductsLikeFragment.setArguments(bundleProducts);

        FavoritesFragment listStoresLikeFragment = new FavoritesFragment();
        Bundle bundleStores = new Bundle();
        bundleStores.putString("typeLike", "liked-stores");
        listStoresLikeFragment.setArguments(bundleStores);

        adapter.addFragment(listProductsLikeFragment);
        adapter.addFragment(listStoresLikeFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
