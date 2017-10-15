package shop.ineed.app.ineed.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.adapter.SlideAdapter;
import shop.ineed.app.ineed.domain.Store;
import shop.ineed.app.ineed.domain.util.LibraryClass;

public class StoreActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, OnMapReadyCallback {

    private ViewPager mPager;
    private static int CURRENT_PAGE = 0;
    private static int NUM_PAGES = 0;
    private Store mStore;
    private GoogleMap googleMap;

    private TextView txtNameContainerStore;
    private TextView txtDescriptionContainerStore;
    private TextView txtAddressContainerStore;
    private TextView txtPhoneContainerStore;
    private TextView txtCellPhoneContainerStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_store);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String store = getIntent().getStringExtra("store");


        TextView txtNameStore = findViewById(R.id.txtNameStore);
        TextView txtDescriptionStore = findViewById(R.id.txtDescriptionStore);
        CircleImageView ivStore = findViewById(R.id.ivStoreDetails);
        TextView txtNameStoreToolbar = findViewById(R.id.txtNameStoreToolbar);
        AppBarLayout appBar = findViewById(R.id.appBar);
        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbarLayout);


        DatabaseReference reference = LibraryClass.getFirebase().child("stores").child(store);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Store store = dataSnapshot.getValue(Store.class);
                mStore = store;
                initSlide();
                Log.i("STORE", store.getName());
                Picasso.with(getBaseContext()).load(store.getPictures().get(0)).into(ivStore);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    String imageTransitionName = getIntent().getExtras().getString(ProductsActivity.EXTRA_PRODUCT_IMAGE_TRANSITION_NAME);
                    ivStore.setTransitionName(imageTransitionName);

                    getWindow().setStatusBarColor(Color.parseColor(store.getColor()));
                }

                txtNameStore.setText(store.getName());
                txtDescriptionStore.setText(store.getDescription());
                txtNameStoreToolbar.setText(store.getName());
                appBar.setBackgroundColor(Color.parseColor(store.getColor()));
                toolbarLayout.setContentScrimColor(Color.parseColor(store.getColor()));
                toolbarLayout.setStatusBarScrimColor(Color.parseColor(store.getColor()));

                initViews();
                initValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void initViews() {
        txtNameContainerStore = findViewById(R.id.txtNameContainerStore);
        txtDescriptionContainerStore = findViewById(R.id.txtDescriptionContainerStore);
        txtAddressContainerStore = findViewById(R.id.txtAddressContainerStore);
        txtPhoneContainerStore = findViewById(R.id.txtPhoneContainerStore);
        txtCellPhoneContainerStore = findViewById(R.id.txtCellPhoneContainerStore);
    }

    public void initValue() {
        txtNameContainerStore.setText(mStore.getName());
        txtDescriptionContainerStore.setText(mStore.getDescription());
        txtAddressContainerStore.setText(mStore.getLocation().getAddress());
        txtPhoneContainerStore.setText(mStore.getPhone());
        txtCellPhoneContainerStore.setText(mStore.getCellPhone());
    }

    @Override
    protected void onResume() {
        super.onResume();

        SupportMapFragment mapContainerStore = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapContainerStore);
        mapContainerStore.getMapAsync(this);


    }

    private void initSlide() {
        mPager = findViewById(R.id.sliderDetailsStore);
        mPager.setAdapter(new SlideAdapter(getBaseContext(), mStore.getPictures()));


        CirclePageIndicator indicator =
                findViewById(R.id.indicatorDetailsSlide);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(5 * density);


        NUM_PAGES = mStore.getPictures().size();


        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (CURRENT_PAGE == NUM_PAGES) {
                CURRENT_PAGE = 0;
            }
            mPager.setCurrentItem(CURRENT_PAGE++, true);
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 4000);

        indicator.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        CURRENT_PAGE = position;
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (mStore != null) {
            googleMap.setMyLocationEnabled(true);

            LatLng location = new LatLng(mStore.getLocation().getLat(), mStore.getLocation().getLng());

            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 13);
            googleMap.moveCamera(update);

            googleMap.addMarker(new MarkerOptions()
                    .title(mStore.getName())
                    .snippet(mStore.getLocation().getAddress())
                    .position(location));

            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
