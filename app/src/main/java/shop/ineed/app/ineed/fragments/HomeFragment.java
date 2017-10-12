package shop.ineed.app.ineed.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import shop.ineed.app.ineed.activity.ProductsActivity;
import shop.ineed.app.ineed.adapter.ViewHolder.CategoriesViewHolder;
import shop.ineed.app.ineed.adapter.ViewHolder.ProductsViewHolder;
import shop.ineed.app.ineed.domain.Category;
import shop.ineed.app.ineed.domain.Product;
import shop.ineed.app.ineed.domain.util.LibraryClass;
import shop.ineed.app.ineed.interfaces.RecyclerClickListener;

import static shop.ineed.app.ineed.activity.ProductsActivity.EXTRA_PRODUCT_IMAGE_TRANSITION_NAME;

public class HomeFragment extends BaseFragment {

    private RecyclerView mRecyclerViewCategories;
    private AVLoadingIndicatorView mProgressCategories;
    private FirebaseRecyclerAdapter<Category, CategoriesViewHolder> mAdapterCategory;
    private RecyclerView mRecyclerViewProducts;
    private AVLoadingIndicatorView mProgressProducts;
    private FirebaseRecyclerAdapter<Product, ProductsViewHolder> mAdapterProducts;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_home, container, false);

        // Categories
        mRecyclerViewCategories = (RecyclerView) viewRoot.findViewById(R.id.recyclerHomeCategories);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewCategories.setLayoutManager(layoutManager);
        mProgressCategories = (AVLoadingIndicatorView) viewRoot.findViewById(R.id.progressCategories);

        // Products
        mRecyclerViewProducts = (RecyclerView) viewRoot.findViewById(R.id.recyclerHomeProducts);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerViewProducts.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        } else {
            mRecyclerViewProducts.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        }
        mRecyclerViewProducts.setNestedScrollingEnabled(false);
        mProgressProducts = (AVLoadingIndicatorView) viewRoot.findViewById(R.id.progressProducts);

        return viewRoot;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DatabaseReference referenceCategories = LibraryClass.getFirebase().child("categories");
        mAdapterCategory = new FirebaseRecyclerAdapter<Category, CategoriesViewHolder>(
                Category.class,
                R.layout.adapter_recycler_categories_home,
                CategoriesViewHolder.class,
                referenceCategories
        ) {
            @Override
            protected void populateViewHolder(CategoriesViewHolder viewHolder, final Category model, int position) {
                viewHolder.setDate(model);
                if (mProgressCategories.getVisibility() == View.VISIBLE) {
                    mProgressCategories.hide();
                }

                DatabaseReference ref = mAdapterCategory.getRef(position);
                model.setKey(ref.getKey());

                viewHolder.setOnClickListener(new RecyclerClickListener() {
                    @Override
                    public void onClickRecyclerListener(View view, int position) {
                        Intent intent = new Intent(getActivity(), ProductsActivity.class);
                        intent.putExtra("category", Parcels.wrap(model));
                        startActivity(intent);
                    }

                    @Override
                    public void onClickRecyclerListener(View view, int position, View viewAnimation) {
                    }
                });
            }
        };
        mRecyclerViewCategories.setAdapter(mAdapterCategory);

        DatabaseReference referenceProducts = LibraryClass.getFirebase().child("products");
        mAdapterProducts = new FirebaseRecyclerAdapter<Product, ProductsViewHolder>(
                Product.class,
                R.layout.adapter_item_products,
                ProductsViewHolder.class,
                referenceProducts
        ) {
            @Override
            protected void populateViewHolder(ProductsViewHolder viewHolder, final Product model, int position) {
                viewHolder.setData(model);
                if (mProgressProducts.getVisibility() == View.VISIBLE) {
                    mProgressProducts.hide();
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
        mRecyclerViewProducts.setAdapter(mAdapterProducts);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapterCategory.cleanup();
        mAdapterProducts.cleanup();
    }
}
