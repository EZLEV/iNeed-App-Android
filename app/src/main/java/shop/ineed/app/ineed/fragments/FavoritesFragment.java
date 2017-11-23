package shop.ineed.app.ineed.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;


import java.util.ArrayList;
import java.util.List;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.activity.DetailsProductsActivity;

import shop.ineed.app.ineed.activity.StoreActivity;
import shop.ineed.app.ineed.adapter.ProductsAdapter;
import shop.ineed.app.ineed.adapter.StoresAdapter;
import shop.ineed.app.ineed.domain.Product;
import shop.ineed.app.ineed.domain.Store;
import shop.ineed.app.ineed.domain.User;
import shop.ineed.app.ineed.domain.util.LibraryClass;
import shop.ineed.app.ineed.interfaces.RecyclerClickListener;


public class FavoritesFragment extends Fragment {

    private String TAG = this.getClass().getSimpleName();

    private RecyclerView mRecyclerViewFavoritesList;
    private AVLoadingIndicatorView mProgressFavoritesList;
    private DatabaseReference mReference;

    private ProductsAdapter mAdapterProducts;
    private List<Product> mProducts = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String uidUser = LibraryClass.getUserLogged(getActivity(), User.PROVIDER);
        mReference = LibraryClass.getFirebase().child("consumers").child(uidUser).child("liked-products");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.fragment_favorites, container, false);

        initViews(viewRoot);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerViewFavoritesList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        } else {
            mRecyclerViewFavoritesList.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        }

        mAdapterProducts = new ProductsAdapter(mProducts, onClickProduct);
        mRecyclerViewFavoritesList.setAdapter(mAdapterProducts);
        loadProducts();

        return viewRoot;
    }

    private void initViews(View view) {
        mRecyclerViewFavoritesList = view.findViewById(R.id.recyclerFavorites);
        mProgressFavoritesList = view.findViewById(R.id.progressFavorites);
    }

    private void loadProducts() {
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    Product product = data.getValue(Product.class);
                    Log.d(TAG, product.getName());
                    mProducts.add(product);

                    if (mProgressFavoritesList.getVisibility() == View.VISIBLE) {
                        mProgressFavoritesList.hide();
                    }

                    mAdapterProducts.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "Details:" + databaseError.getDetails() + ", " + "Message:" + databaseError.getMessage());
            }
        });
    }

    RecyclerClickListener onClickProduct = (view, position) -> {
        Intent intent = new Intent(getActivity(), DetailsProductsActivity.class);
        intent.putExtra("product", mProducts.get(position));
        startActivity(intent);
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
