package shop.ineed.app.ineed.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import livroandroid.lib.utils.AndroidUtils;
import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.adapter.ProductsAdapter;
import shop.ineed.app.ineed.components.EndlessRecyclerOnScrollListener;
import shop.ineed.app.ineed.components.ItemOffsetDecoration;
import shop.ineed.app.ineed.domain.Category;
import shop.ineed.app.ineed.domain.Product;
import shop.ineed.app.ineed.interfaces.RecyclerClickListener;
import shop.ineed.app.ineed.domain.SearchResultsJsonParser;

public class ProductsActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();

    private Category mCategory;
    private SwipeRefreshLayout mSwipeLayout;
    private AVLoadingIndicatorView mProgress;
    private ProgressBar progressbarMoreItems;
    private List<Product> mProducts = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ProductsAdapter productsListAdapter;
    private Index index;
    private Query query;

    private SearchResultsJsonParser resultsParser = new SearchResultsJsonParser();
    // Pagination RecyclerView
    private int lastSearchedSeqNo;
    private int lastDisplayedSeqNo;
    private int lastRequestedPage;
    private int lastDisplayedPage;
    private boolean endReached;

    // Constants
    private static final int HITS_PER_PAGE = 20;
    private static final int LOAD_MORE_THRESHOLD = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        enableToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Inicialização das views
        initViews();

        mCategory = getIntent().getParcelableExtra("category");
        Log.i(TAG, mCategory.getKey());
        getSupportActionBar().setTitle(mCategory.getValue());

        StaggeredGridLayoutManager manager;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        } else {
            manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        }
        mRecyclerView.setLayoutManager(manager);
        productsListAdapter = new ProductsAdapter(mProducts, onClick());
        mRecyclerView.setAdapter(productsListAdapter);
        mRecyclerView.addItemDecoration(new ItemOffsetDecoration(8));
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore(int firstVisibleItem, int visibleItemCount, int totalItemCount, int dy) {

                progressbarMoreItems.setVisibility(View.VISIBLE);

                // Abort if list is empty or the end has already been reached.
                if (totalItemCount == 0 || endReached) {
                    return;
                }

                // Ignore if a new page has already been requested.
                if (lastRequestedPage > lastDisplayedPage) {
                    return;
                }

                // Load more if we are sufficiently close to the end of the list.
                int firstInvisibleItem = firstVisibleItem + visibleItemCount;
                if (firstInvisibleItem + LOAD_MORE_THRESHOLD >= totalItemCount) {
                    loadMore();
                } else {
                    snack("Não");
                }
            }
        });

        // Swipe to Refresh
        mSwipeLayout.setOnRefreshListener(OnRefreshListener());
        mSwipeLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark
        );

        // Config Algolia
        Client client = new Client("K3C3FZBI6P", "37baed209b1fbd4c3fe061fef6e33966");
        index = client.getIndex("products");

        // Query Algolia
        query = new Query();
        query.setAttributesToRetrieve("name", "pictures", "description", "store", "objectID", "price", "categories");
        query.setAttributesToHighlight("name");
        query.setHitsPerPage(HITS_PER_PAGE);

        findViewById(R.id.fabSearchProducts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductsActivity.this, SearchActivity.class));
            }
        });

    }

    private void initViews() {
        mSwipeLayout = findViewById(R.id.swipeToRefreshProducts);
        mProgress = findViewById(R.id.progress);
        mRecyclerView = findViewById(R.id.recyclerProducts);
        progressbarMoreItems = findViewById(R.id.progressbarMoreItems);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.search(mCategory.getKey());
    }

    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return () -> {
            // Valida se existe conexão ao fazer o gesto de Pull to Refresh
            if (AndroidUtils.isNetworkAvailable(getBaseContext())) {
                this.search(mCategory.getKey());
                mSwipeLayout.setRefreshing(false);
            } else {
                mSwipeLayout.setRefreshing(false);
                snack("Não foi possivel acessar a internet");
            }
        };
    }


    private void search(String s) {
        final int currentSearchSeqNo = ++lastSearchedSeqNo;
        query.setQuery(s);
        lastRequestedPage = 0;
        lastDisplayedPage = -1;
        endReached = false;
        index.searchAsync(query, (content, error) -> {
            if (content != null && error == null) {
                // NOTE: Check that the received results are newer that the last displayed results.
                //
                // Rationale: Although TCP imposes a server to send responses in the same order as
                // requests, nothing prevents the system from opening multiple connections to the
                // same server, nor the Algolia client to transparently switch to another server
                // between two requests. Therefore the order of responses is not guaranteed.
                if (currentSearchSeqNo <= lastDisplayedSeqNo) {
                    return;
                }

                List<Product> results = resultsParser.parseResults(content);
                Toast.makeText(getBaseContext(), results.size() + "SEARCH", Toast.LENGTH_LONG).show();
                if (results.isEmpty()) {
                    endReached = true;
                } else {
                    mProducts.clear();
                    mProducts.addAll(results);
                    Log.i("TAF", results.size() + "");
                    productsListAdapter.notifyDataSetChanged();

                    lastDisplayedSeqNo = currentSearchSeqNo;
                    lastDisplayedPage = 0;

                    if (mProgress.getVisibility() == View.VISIBLE) {
                        mProgress.setVisibility(View.GONE);
                    }
                }

                // Scroll the list back to the top.
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
    }

    private void loadMore() {
        Query loadMoreQuery = new Query(query);
        loadMoreQuery.setPage(++lastRequestedPage);
        final int currentSearchSeqNo = lastSearchedSeqNo;
        index.searchAsync(loadMoreQuery, (content, error) -> {
            if (content != null && error == null) {
                // Ignore results if they are for an older query.
                if (lastDisplayedSeqNo != currentSearchSeqNo) {
                    return;
                }

                List<Product> results = resultsParser.parseResults(content);
                Toast.makeText(getBaseContext(), results.size() + "MORE", Toast.LENGTH_LONG).show();
                if (results.isEmpty()) {
                    endReached = true;
                } else {
                    mProducts.addAll(results);
                    productsListAdapter.notifyDataSetChanged();
                    lastDisplayedPage = lastRequestedPage;
                }
            }
            progressbarMoreItems.setVisibility(View.GONE);
        });
    }

    private void snack(String msg) {
        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.swipeToRefreshProducts),
                msg, Snackbar.LENGTH_SHORT);
        mySnackbar.setAction("OK", null);
        mySnackbar.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private RecyclerClickListener onClick() {
        return (view, position) -> {
            Intent intent = new Intent(ProductsActivity.this, DetailsProductsActivity.class);
            intent.putExtra("product", mProducts.get(position));
            startActivity(intent);
        };
    }
}
