package shop.ineed.app.ineed.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.wang.avi.AVLoadingIndicatorView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import livroandroid.lib.utils.AndroidUtils;
import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.adapter.ProductsViewHolder;
import shop.ineed.app.ineed.domain.Category;
import shop.ineed.app.ineed.domain.Product;
import shop.ineed.app.ineed.domain.util.LibraryClass;
import shop.ineed.app.ineed.interfaces.RecyclerClickListener;

public class ProductsActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {

    private List<Product> mProducts = new ArrayList<>();
    private Category mCategory;
    private SwipeRefreshLayout mSwipeLayout;
    private AVLoadingIndicatorView mProgress;
    public static final String EXTRA_PRODUCT_IMAGE_TRANSITION_NAME = "product_image_transition";
    private final String TAG = this.getClass().getSimpleName();
    private FirebaseRecyclerAdapter<Product, ProductsViewHolder> mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_products);

        enableToolbar();

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mCategory = Parcels.unwrap(getIntent().getParcelableExtra("category"));
        Log.i(TAG, mCategory.getKey());
        getSupportActionBar().setTitle(mCategory.getValue());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerProducts);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        } else {
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        }
        loadData();

        //ProgressBar
        mProgress = (AVLoadingIndicatorView) findViewById(R.id.progress);
        mProgress.show();

        // Swipe to Refresh
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefreshProducts);
        mSwipeLayout.setOnRefreshListener(OnRefreshListener());
        mSwipeLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark
        );
    }


    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return () -> {
            // Valida se existe conexão ao fazer o gesto de Pull to Refresh
            if (AndroidUtils.isNetworkAvailable(getBaseContext())) {
                mAdapter.notifyDataSetChanged();
                mSwipeLayout.setRefreshing(false);
            } else {
                mSwipeLayout.setRefreshing(false);
                snack("Não foi possivel acessar a internet");
            }
        };
    }

    private void snack(String msg) {
        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.activityProducts),
                msg, Snackbar.LENGTH_SHORT);
        mySnackbar.setAction("OK", null);
        mySnackbar.show();
    }

    private void loadData() {
        DatabaseReference reference = LibraryClass.getFirebase().child("products-categories").child(mCategory.getKey());
        mAdapter = new FirebaseRecyclerAdapter<Product, ProductsViewHolder>(
                Product.class,
                R.layout.adapter_item_products,
                ProductsViewHolder.class,
                reference
        ) {
            @Override
            protected void populateViewHolder(ProductsViewHolder viewHolder, Product model, int position) {
                if (mProgress.getVisibility() == View.VISIBLE) {
                    mProgress.hide();
                }
                viewHolder.setData(model);
                mProducts.add(model);
            }

            @Override
            public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ProductsViewHolder holder = super.onCreateViewHolder(parent, viewType);
                holder.setOnClickListener(onProductClickListener());
                return holder;
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private RecyclerClickListener onProductClickListener() {
        return new RecyclerClickListener() {
            @Override
            public void onClickRecyclerListener(View view, int idx) {
            }

            @Override
            public void onClickRecyclerListener(View view, int position, View viewAnimation) {
                Toast.makeText(getBaseContext(), "Position:" + position, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ProductsActivity.this, DetailsProductsActivity.class);
                intent.putExtra("product", Parcels.wrap(mProducts.get(position)));
                intent.putExtra(EXTRA_PRODUCT_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(viewAnimation));

                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(ProductsActivity.this, viewAnimation, ViewCompat.getTransitionName(viewAnimation));
                ActivityCompat.startActivity(ProductsActivity.this, intent, optionsCompat.toBundle());
            }
        };
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
            getToolbar().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            getToolbar().setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
