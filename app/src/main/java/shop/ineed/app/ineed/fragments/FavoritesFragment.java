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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.wang.avi.AVLoadingIndicatorView;

import org.parceler.Parcels;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.activity.DetailsProductsActivity;
import shop.ineed.app.ineed.adapter.ViewHolder.ProductsViewHolder;
import shop.ineed.app.ineed.adapter.ViewHolder.StoresViewHolder;
import shop.ineed.app.ineed.domain.Product;
import shop.ineed.app.ineed.domain.Store;
import shop.ineed.app.ineed.domain.User;
import shop.ineed.app.ineed.domain.util.LibraryClass;
import shop.ineed.app.ineed.interfaces.RecyclerClickListener;

import static shop.ineed.app.ineed.activity.ProductsActivity.EXTRA_PRODUCT_IMAGE_TRANSITION_NAME;


public class FavoritesFragment extends Fragment {


    private RecyclerView recyclerViewListLike;
    private AVLoadingIndicatorView progressListLike;
    private String typeLike;
    private String uidUser;
    private DatabaseReference reference;

    private FirebaseRecyclerAdapter<Store, StoresViewHolder> mAdapterStores;
    private FirebaseRecyclerAdapter<Product, ProductsViewHolder> mAdapterProducts;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        typeLike = getArguments().getString("typeLike");
        uidUser = LibraryClass.getUserLogged(getActivity(), User.PROVIDER);
        reference = LibraryClass.getFirebase().child("consumers").child(uidUser).child(typeLike);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerViewListLike = viewRoot.findViewById(R.id.recyclerFavorites);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerViewListLike.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        } else {
            recyclerViewListLike.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        }
        recyclerViewListLike.setNestedScrollingEnabled(true);
        recyclerViewListLike.setHasFixedSize(true);

        progressListLike = viewRoot.findViewById(R.id.progressFavorites);

        if (typeLike.equals("liked-products")) {
            loadProducts();
        } else {
            loadStores();
        }

        return viewRoot;
    }

    private void loadProducts() {
        mAdapterProducts = new FirebaseRecyclerAdapter<Product, ProductsViewHolder>(
                Product.class,
                R.layout.adapter_item_products,
                ProductsViewHolder.class,
                reference
        ) {
            @Override
            protected void populateViewHolder(ProductsViewHolder viewHolder, final Product model, int position) {
                viewHolder.setData(model);
                if (progressListLike.getVisibility() == View.VISIBLE) {
                    progressListLike.hide();
                }

                viewHolder.setOnClickListener(new RecyclerClickListener() {
                    @Override
                    public void onClickRecyclerListener(View view, int position) {

                    }

                    @Override
                    public void onClickRecyclerListener(View view, int position, View viewAnimation) {
                        Toast.makeText(getActivity(), "Position:" + position, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), DetailsProductsActivity.class);
                        intent.putExtra("product", Parcels.wrap(model));
                        intent.putExtra(EXTRA_PRODUCT_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(viewAnimation));

                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), viewAnimation, ViewCompat.getTransitionName(viewAnimation));
                        ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());
                    }
                });
            }
        };
        recyclerViewListLike.setAdapter(mAdapterProducts);
    }


    private void loadStores() {
        mAdapterStores = new FirebaseRecyclerAdapter<Store, StoresViewHolder>(
                Store.class,
                R.layout.adapter_item_stores,
                StoresViewHolder.class,
                reference
        ) {
            @Override
            protected void populateViewHolder(StoresViewHolder viewHolder, Store model, int position) {
                viewHolder.setData(model);
                Log.i("TAG", model.getName());

                DatabaseReference ref = mAdapterStores.getRef(position);
                model.setId(ref.getKey());

                viewHolder.setOnClickListener(new RecyclerClickListener() {
                    @Override
                    public void onClickRecyclerListener(View view, int position) {

                    }

                    @Override
                    public void onClickRecyclerListener(View view, int position, View viewAnimation) {

                    }
                });
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if (progressListLike.getVisibility() == View.VISIBLE) {
                    progressListLike.setVisibility(View.INVISIBLE);
                }
            }

        };
        recyclerViewListLike.setAdapter(mAdapterStores);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapterProducts != null) {
            mAdapterProducts.cleanup();
        }

        if (mAdapterStores != null) {
            mAdapterStores.cleanup();
        }
    }
}
