package shop.ineed.app.ineed.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
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
import java.util.EventListener;
import java.util.List;

import livroandroid.lib.utils.AndroidUtils;
import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.activity.StoreActivity;
import shop.ineed.app.ineed.adapter.StoresAdapter;
import shop.ineed.app.ineed.domain.Store;
import shop.ineed.app.ineed.domain.util.LibraryClass;
import shop.ineed.app.ineed.interfaces.RecyclerClickListener;


/**
 * Listagem de lojas
 */
public class StoresFragment extends BaseFragment {

    private String TAG = this.getClass().getSimpleName();

    private RecyclerView mRecyclerView;
    private StoresAdapter mAdapter;
    private AVLoadingIndicatorView mProgress;
    private List<Store> mStores = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DatabaseReference mDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = LibraryClass.getFirebase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_stores, container, false);

        initViews(viewRoot);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_recycler));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = new StoresAdapter(mStores, onClickStore);
        mRecyclerView.setAdapter(mAdapter);

        // Swipe to Refresh
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener());
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark
        );

        return viewRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
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

    private ValueEventListener eventLoadData = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot data : dataSnapshot.getChildren()) {

                Store store = data.getValue(Store.class);
                Log.d(TAG, store.getName());
                mStores.add(store);

                if (mProgress.getVisibility() == View.VISIBLE) {
                    mProgress.hide();
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
        mStores.clear();
        mDatabase.child("stores").addListenerForSingleValueEvent(eventLoadData);
    }

    private void initViews(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshStoresFragment);
        mRecyclerView = view.findViewById(R.id.recyclerStoresFragment);
        mProgress = view.findViewById(R.id.progressListStoresFragment);
    }

    RecyclerClickListener onClickStore = (view, position) -> {
        Intent intent = new Intent(getActivity(), StoreActivity.class);
        intent.putExtra("store", mStores.get(position).getId());
        getActivity().startActivity(intent);
    };

    @Override
    public void onDetach() {
        super.onDetach();
        mDatabase.removeEventListener(eventLoadData);
    }
}
