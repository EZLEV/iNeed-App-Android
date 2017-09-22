package shop.ineed.app.ineed.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import livroandroid.lib.utils.AndroidUtils;
import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.adapter.CategoriesAdapter;
import shop.ineed.app.ineed.adapter.ProductsAdapter;
import shop.ineed.app.ineed.domain.Category;
import shop.ineed.app.ineed.domain.Product;
import shop.ineed.app.ineed.domain.util.LibraryClass;
import shop.ineed.app.ineed.interfaces.RecyclerClickListener;

public class ProductsActivity extends BaseActivity {

    private List<Product> mProducts = new ArrayList<>();
    private Category mCategory;
    private ProductsAdapter mAdapter;
    private SwipeRefreshLayout mSwipeLayout;
    private ProgressBar mProgress;
    public static final String EXTRA_PRODUCT_IMAGE_TRANSITION_NAME = "product_image_transition";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_products);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mCategory = Parcels.unwrap(getIntent().getParcelableExtra("category"));
        getSupportActionBar().setTitle(mCategory.getValue());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerProducts);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        } else {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        }
        mAdapter = new ProductsAdapter(this, mProducts, onProductClickListener());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mProgress = (ProgressBar) findViewById(R.id.progress);
        mProgress.setVisibility(View.VISIBLE);

        if (mProducts.size() == 0) {
            loadData();
        }

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
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Valida se existe conexão ao fazer o gesto de Pull to Refresh
                if (AndroidUtils.isNetworkAvailable(getBaseContext())) {
                    // // taskCarros(true);
                    loadData();
                } else {
                    mSwipeLayout.setRefreshing(false);
                    snack("Não foi possivel acessar a internet");
                }
            }
        };
    }

    private void snack(String msg){
        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.activityProducts),
                msg, Snackbar.LENGTH_SHORT);
        mySnackbar.setAction("OK", null);
        mySnackbar.show();
    }

    private void loadData() {
        DatabaseReference reference = LibraryClass.getFirebase().child("products-categories").child(mCategory.getKey());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProducts.removeAll(mProducts);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.i("TAG", "key" + snapshot.getKey() + " " + snapshot.getValue());
                    Product p = snapshot.getValue(Product.class);
                    // Log.d("Valor", snapshot.getValue(Product.class).toString());
                    mProducts.add(p);
                }
                mAdapter.notifyDataSetChanged();
                mSwipeLayout.setRefreshing(false);
                mProgress.setVisibility(View.GONE);
                Log.i("ValueList: ", mProducts.size() + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", databaseError.getMessage());
                mSwipeLayout.setRefreshing(false);
                mProgress.setVisibility(View.GONE);
            }
        });
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
}
