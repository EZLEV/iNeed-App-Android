package shop.ineed.app.ineed.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.wang.avi.AVLoadingIndicatorView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.activity.ProductsActivity;
import shop.ineed.app.ineed.adapter.CategoriesViewHolder;
import shop.ineed.app.ineed.domain.Category;
import shop.ineed.app.ineed.domain.util.LibraryClass;
import shop.ineed.app.ineed.interfaces.RecyclerClickListener;

/**
 * Lista todas as categoria existente no Firebase iNeed.
 */
public class ListCategoriesFragment extends BaseFragment {

    private List<Category> mCategories;
    private RecyclerView mRecyclerView;
    private AVLoadingIndicatorView mProgressListCategories;
    FirebaseRecyclerAdapter<Category, CategoriesViewHolder> mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategories = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_categories, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerCategories);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        }

        mProgressListCategories = (AVLoadingIndicatorView) view.findViewById(R.id.progressListCategories);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Faz a chamada ao Firebase
        DatabaseReference reference = LibraryClass.getFirebase().child("categories");
        mAdapter = new FirebaseRecyclerAdapter<Category, CategoriesViewHolder>(
                Category.class,
                R.layout.adapter_item_categories,
                CategoriesViewHolder.class,
                reference
        ) {
            @Override
            protected void populateViewHolder(CategoriesViewHolder viewHolder, Category model, int position) {
                if(mProgressListCategories.getVisibility() == View.VISIBLE){
                    mProgressListCategories.hide();
                }
                viewHolder.setDate(model);
                DatabaseReference ref = mAdapter.getRef(position);
                model.setKey(ref.getKey());
                mCategories.add(model);
            }

            @Override
            public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                CategoriesViewHolder holder = super.onCreateViewHolder(parent, viewType);

                holder.setOnClickListener(new RecyclerClickListener() {
                    @Override
                    public void onClickRecyclerListener(View view, int position) {
                        Category category = mCategories.get(position);
                        Intent intent = new Intent(getContext(), ProductsActivity.class);
                        intent.putExtra("category", Parcels.wrap(category));
                        startActivity(intent);
                    }

                    @Override
                    public void onClickRecyclerListener(View view, int position, View viewAnimation) {
                        Toast.makeText(getActivity(), "asdas", Toast.LENGTH_LONG).show();
                    }
                });

                return holder;
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
