package shop.ineed.app.ineed.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import org.parceler.Parcels;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.domain.Product;
import shop.ineed.app.ineed.fragments.DetailsProductFragment;
import shop.ineed.app.ineed.util.Base64;

public class DetailsProductsActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {

    private Palette.Swatch mVibrantSwatch;
    private Bitmap mImageProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_details_products);
        enableToolbar();

        Product product = Parcels.unwrap(getIntent().getParcelableExtra("product"));
        mImageProduct = Base64.convertToBitmap(product.getPictures().get(0));

        getSupportActionBar().setTitle(product.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Palette.Builder builder = new Palette.Builder(mImageProduct);
        Palette palette = builder.generate();
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mVibrantSwatch = palette.getVibrantSwatch();
        if (mVibrantSwatch != null) {
            getToolbar().setBackgroundColor(mVibrantSwatch.getRgb());
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                appBarLayout.setBackgroundColor(mVibrantSwatch.getRgb());
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(mVibrantSwatch.getRgb());
                appBarLayout.addOnOffsetChangedListener(this);
            }
        }

        if (savedInstanceState == null) {
            DetailsProductFragment productFragment = new DetailsProductFragment();
            productFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.detailsProductFragment, productFragment).commit();
        }

        initViews();
    }

    private void initViews() {
        ImageView ivProduct = (ImageView) findViewById(R.id.ivProduct);
        ivProduct.setImageBitmap(mImageProduct);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = getIntent().getExtras().getString(ProductsActivity.EXTRA_PRODUCT_IMAGE_TRANSITION_NAME);
            ivProduct.setTransitionName(imageTransitionName);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int scrollRange = -1;
        //Initialize the size of the scroll
        if (scrollRange == -1) {
            scrollRange = appBarLayout.getTotalScrollRange();
        }
        //Check if the view is collapsed
        if (scrollRange + verticalOffset == 0) {
            getToolbar().setBackgroundColor(mVibrantSwatch.getRgb());
        } else {
            getToolbar().setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
