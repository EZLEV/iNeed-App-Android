package shop.ineed.app.ineed.activity;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.adapter.ProductSearchAdapter;
import shop.ineed.app.ineed.domain.Product;
import shop.ineed.app.ineed.domain.SearchResultsJsonParser;

public class SearchActivity extends BaseActivity implements MaterialSearchView.OnQueryTextListener, AbsListView.OnScrollListener {

    // Algolia -> Search product
    private MaterialSearchView searchViewProducts;
    private Index index;
    private Query query;
    private SearchResultsJsonParser resultsParser;
    private int lastSearchedSeqNo;
    private int lastDisplayedSeqNo;
    private int lastRequestedPage;
    private int lastDisplayedPage;
    private boolean endReached;
    private ListView productsListView;
    private ProductSearchAdapter productsListAdapter;
    private static final int HITS_PER_PAGE = 20;
    private static final int LOAD_MORE_THRESHOLD = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        enableToolbar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchViewProducts = (MaterialSearchView) findViewById(R.id.searchViewProducts);
        searchViewProducts.setOnQueryTextListener(this);
        searchViewProducts.setVoiceSearch(true);

        // ListView init
        productsListView = (ListView) findViewById(R.id.listViewProductSearch);
        productsListView.setAdapter(productsListAdapter = new ProductSearchAdapter(this, R.layout.adapter_cell_search_product));
        productsListView.setOnScrollListener(this);

        // Config Algolia
        Client client = new Client("K3C3FZBI6P", "37baed209b1fbd4c3fe061fef6e33966");
        index = client.getIndex("products");

        // Query Algolia
        query = new Query();
        query.setAttributesToRetrieve("name", "pictures", "description", "store", "objectID", "price", "categories");
        query.setAttributesToHighlight("name");
        query.setHitsPerPage(HITS_PER_PAGE);

        resultsParser = new SearchResultsJsonParser();

    }

    private void search(String text){
        final int currentSearchSeqNo = ++lastSearchedSeqNo;
        query.setQuery(text);
        lastRequestedPage = 0;
        lastDisplayedPage = -1;
        endReached = false;
        index.searchAsync(query, (content, error) -> {
            if (content != null && error == null) {
                if (currentSearchSeqNo <= lastDisplayedSeqNo) {
                    return;
                }
                List<Product> results = resultsParser.parseResults(content);
                if (results.isEmpty()) {
                    endReached = true;
                } else {
                    productsListAdapter.clear();
                    productsListAdapter.addAll(results);
                    productsListAdapter.notifyDataSetChanged();
                    lastDisplayedSeqNo = currentSearchSeqNo;
                    lastDisplayedPage = 0;
                }
                productsListView.smoothScrollToPosition(0);
            }
        });
    }

    private void loadMore(){
        Query loadMoreQuery = new Query(query);
        loadMoreQuery.setPage(++lastRequestedPage);
        final int currentSearchSeqNo = lastSearchedSeqNo;
        index.searchAsync(loadMoreQuery, (content, error) -> {
            if (content != null && error == null) {
                // Ignore resultados se eles forem para uma consulta mais antiga.
                if (lastDisplayedSeqNo != currentSearchSeqNo) {
                    return;
                }
                List<Product> results = resultsParser.parseResults(content);
                if (results.isEmpty()) {
                    endReached = true;
                } else {
                    productsListAdapter.addAll(results);
                    productsListAdapter.notifyDataSetChanged();
                    lastDisplayedPage = lastRequestedPage;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchViewProducts.setMenuItem(searchItem);
        searchViewProducts.showSearch();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchViewProducts.setQuery(searchWrd, false);
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (searchViewProducts.isSearchOpen()) {
            searchViewProducts.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        search(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(!newText.isEmpty()){
            search(newText);
        }
        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // Aborte se a lista estiver vazia ou o final já foi alcançado.
        if (totalItemCount == 0 || endReached) {
            return;
        }

        // Ignore se uma nova página já foi solicitada.
        if (lastRequestedPage > lastDisplayedPage) {
            return;
        }

        // Carregue mais se estivermos suficientemente perto do final da lista.
        int firstInvisibleItem = firstVisibleItem + visibleItemCount;
        if (firstInvisibleItem + LOAD_MORE_THRESHOLD >= totalItemCount) {
            loadMore();
        }
    }
}
