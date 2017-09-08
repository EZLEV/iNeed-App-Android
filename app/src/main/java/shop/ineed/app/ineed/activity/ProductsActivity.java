package shop.ineed.app.ineed.activity;

import android.content.res.Configuration;
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
import android.widget.ProgressBar;

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

public class ProductsActivity extends AppCompatActivity {

    private List<Product> mProducts = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private Category category;
    private ProductsAdapter mAdapter;
    private SwipeRefreshLayout mSwipeLayout;
    private ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        category = Parcels.unwrap(getIntent().getParcelableExtra("category"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(category.getValue());

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerProducts);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }else{
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        }
        mAdapter = new ProductsAdapter(this, mProducts);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        Log.d("TAG", category.getKey());

        if(mProducts.size() == 0){
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
                    // snack(mRecyclerView, R.string.error_conexao_indisponivel);
                }
            }
        };
    }

    private void loadData(){
        DatabaseReference reference = LibraryClass.getFirebase().child("products-categories").child(category.getKey());
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
                progress.setVisibility(View.GONE);
                Log.i("ValueList: ", mProducts.size() + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", databaseError.getMessage());
                mSwipeLayout.setRefreshing(false);
                progress.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
