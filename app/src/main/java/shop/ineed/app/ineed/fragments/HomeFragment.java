package shop.ineed.app.ineed.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.florent37.viewanimator.ViewAnimator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import livroandroid.lib.utils.AndroidUtils;
import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.activity.ListCategoriesActivity;
import shop.ineed.app.ineed.activity.ProductsActivity;
import shop.ineed.app.ineed.activity.SearchActivity;
import shop.ineed.app.ineed.adapter.CategoriesAdapter;
import shop.ineed.app.ineed.domain.Category;
import shop.ineed.app.ineed.domain.util.LibraryClass;
import shop.ineed.app.ineed.interfaces.RecyclerClickListener;
import shop.ineed.app.ineed.util.Base64;


public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ValueEventListener {

    private String TAG = this.getClass().getSimpleName();

    private RecyclerView mRecyclerViewCategories;
    private LinearLayout mContentHome;
    private SwipeRefreshLayout mSwipeToRefreshHome;
    private EditText mEditSearch;

    private CategoriesAdapter mAdapterCategory;
    private List<Category> mCategories;
    private DatabaseReference mDatabase;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = LibraryClass.getFirebase();
        mCategories = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(viewRoot);

        //Categories
        mRecyclerViewCategories.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mAdapterCategory = new CategoriesAdapter(mCategories, R.layout.adapter_recycler_categories_home, onClickCategory);
        //mRecyclerViewCategories.addItemDecoration(new ItemOffsetDecoration(2));
        mRecyclerViewCategories.setNestedScrollingEnabled(false);
        mRecyclerViewCategories.setAdapter(mAdapterCategory);


        // Swipe to Refresh
        mSwipeToRefreshHome.setOnRefreshListener(this);
        mSwipeToRefreshHome.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark
        );
        mSwipeToRefreshHome.setRefreshing(true);

        mEditSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getContext(), SearchActivity.class));
                Log.i(TAG, "editSearchProductsHomeFragment");
            }
        });
        ViewCompat.setElevation(mEditSearch, 10);


        return viewRoot;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadCategories();
    }

    private void initViews(View view) {
        mRecyclerViewCategories = view.findViewById(R.id.recyclerHomeCategories);
        mContentHome = view.findViewById(R.id.contentHome);
        mSwipeToRefreshHome = view.findViewById(R.id.swipeToRefreshHome);
        mEditSearch = view.findViewById(R.id.editSearchProductsHomeFragment);
    }

    private void loadCategories() {
        mCategories.clear();
        mDatabase.child("categories").addListenerForSingleValueEvent(this);

        HashMap<String, Object> ad = new HashMap<>();
        mDatabase.updateChildren(ad).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }


    private RecyclerClickListener onClickCategory = new RecyclerClickListener() {
        @Override
        public void onClickRecyclerListener(View view, int position) {
            if (position == 5) {
                getActivity().startActivity(new Intent(getContext(), ListCategoriesActivity.class));
            } else {
                getActivity().startActivity(new Intent(getContext(), ProductsActivity.class).putExtra("category", mCategories.get(position)));
            }
        }
    };


    @Override
    public void onRefresh() {
        if (AndroidUtils.isNetworkAvailable(getActivity())) {
            mCategories.clear();
            loadCategories();
            mSwipeToRefreshHome.setRefreshing(false);
        } else {
            mSwipeToRefreshHome.setRefreshing(false);
            //snack("Não foi possivel acessar a internet");
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
        for (int count = 0; count < dataSnapshot.getChildrenCount(); count++) {
            Category category = iterator.next().getValue(Category.class);

            Log.d(TAG, category.getValue());
            category.setKey(dataSnapshot.getKey());
            mCategories.add(category);

            mContentHome.setVisibility(View.VISIBLE);
            mSwipeToRefreshHome.setRefreshing(false);
        }

        Category plus = new Category();
        plus.setKey("plus");
        plus.setValue(getActivity().getResources().getString(R.string.more_categories));


        String s = Base64.convertToBase64(Base64.convertDrawableToBitmap(getActivity(), R.drawable.ic_plus));

        Log.i(TAG, s);

        plus.setIcon(s);

        mCategories.add(plus);

        mAdapterCategory.notifyDataSetChanged();

        ViewAnimator
                .animate(mRecyclerViewCategories)
                .dp().translationY(200, 0)
                .accelerate()
                .start();

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        FirebaseCrash.log(databaseError.getMessage());
    }
}
