package shop.ineed.app.ineed.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import livroandroid.lib.utils.AndroidUtils;
import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.activity.ProductsActivity;
import shop.ineed.app.ineed.adapter.CategoriesAdapter;
import shop.ineed.app.ineed.domain.Category;
import shop.ineed.app.ineed.domain.util.LibraryClass;
import shop.ineed.app.ineed.interfaces.RecyclerClickListener;

/**
 * Created by jose on 11/6/17.
 * <p>
 * Lista de todas as categorias cadastradas
 */

public class ListCategoriesFragment extends BaseFragment {

    private String TAG = this.getClass().getSimpleName();

    private DatabaseReference mDatabase;
    private List<Category> mCategories = new ArrayList<>();
    private CategoriesAdapter mAdapter;
    private AVLoadingIndicatorView mProgressListCategories;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerViewCategories;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = LibraryClass.getFirebase();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_list_categories, container, false);

        initViews(viewRoot);
        mRecyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CategoriesAdapter(mCategories,R.layout.adapter_item_categories, onClickCategory());
        //mRecyclerViewCategories.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerViewCategories.setHasFixedSize(true);
        mRecyclerViewCategories.setAdapter(mAdapter);

        // Swipe to Refresh
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener());
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark
        );

        return viewRoot;
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener() {
        return () -> {
            // Valida se existe conexão ao fazer o gesto de Pull to Refresh
            if (AndroidUtils.isNetworkAvailable(getActivity())) {
                loadData();
                mSwipeRefreshLayout.setRefreshing(false);
            } else {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "Não foi possivel acessar a internet", Toast.LENGTH_LONG).show();
            }
        };
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    private ValueEventListener eventLoadData = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot data : dataSnapshot.getChildren()){
                Category category = data.getValue(Category.class);

                category.setKey(data.getKey());
                Log.d(TAG, category.getKey());
                mCategories.add(category);

                if (mProgressListCategories.getVisibility() == View.VISIBLE) {
                    mProgressListCategories.hide();
                }
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.i(TAG, "Details:" + databaseError.getDetails() + ", " + "Message:" + databaseError.getMessage());
        }
    };

    private void loadData() {
        mCategories.clear();
        mDatabase.child("categories")
                .addListenerForSingleValueEvent(eventLoadData);
    }

    private void initViews(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshCategoriesFragment);
        mProgressListCategories = view.findViewById(R.id.progressListCategoriesFragment);
        mRecyclerViewCategories = view.findViewById(R.id.recyclerCategoriesFragment);
    }

    private RecyclerClickListener onClickCategory() {
        return (view, position) -> getActivity().startActivity(new Intent(getContext(), ProductsActivity.class).putExtra("category", mCategories.get(position)));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDatabase.removeEventListener(eventLoadData);
    }
}