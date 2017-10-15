package shop.ineed.app.ineed.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.activity.DetailsProductsActivity;
import shop.ineed.app.ineed.activity.StoreActivity;
import shop.ineed.app.ineed.adapter.ViewHolder.StoresViewHolder;
import shop.ineed.app.ineed.domain.Store;
import shop.ineed.app.ineed.domain.util.LibraryClass;
import shop.ineed.app.ineed.interfaces.RecyclerClickListener;

import static shop.ineed.app.ineed.activity.ProductsActivity.EXTRA_PRODUCT_IMAGE_TRANSITION_NAME;

/**
 * Listagem de lojas
 */
public class StoresFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<Store, StoresViewHolder> mAdapter;
    private AVLoadingIndicatorView mProgress;
    private List<Store> mStore = new ArrayList<>();

    public StoresFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_stores, container, false);

        recyclerView = viewRoot.findViewById(R.id.recyclerStores);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider_recycler));
        recyclerView.addItemDecoration(dividerItemDecoration);

        mProgress = viewRoot.findViewById(R.id.progressListStores);
        return viewRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DatabaseReference reference = LibraryClass.getFirebase().child("stores");
        mAdapter = new FirebaseRecyclerAdapter<Store, StoresViewHolder>(
                Store.class,
                R.layout.adapter_item_stores,
                StoresViewHolder.class,
                reference
        ) {
            @Override
            protected void populateViewHolder(StoresViewHolder viewHolder, Store model, int position) {
                viewHolder.setData(model);
                Log.i("TAG", model.getName());

                DatabaseReference ref = mAdapter.getRef(position);
                model.setId(ref.getKey());
                mStore.add(model);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if (mProgress.getVisibility() == View.VISIBLE) {
                    mProgress.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public StoresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                StoresViewHolder holder = super.onCreateViewHolder(parent, viewType);

                holder.setOnClickListener(storeClickListener);

                return holder;
            }
        };
        recyclerView.setAdapter(mAdapter);
    }

    private RecyclerClickListener storeClickListener = new RecyclerClickListener() {
        @Override
        public void onClickRecyclerListener(View view, int position) {

        }

        @Override
        public void onClickRecyclerListener(View view, int position, View viewAnimation) {
            Toast.makeText(getActivity(), "Position: " + position, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getActivity(), StoreActivity.class);
            intent.putExtra("store", mStore.get(position).getId());
            intent.putExtra(EXTRA_PRODUCT_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(viewAnimation));

            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), viewAnimation, ViewCompat.getTransitionName(viewAnimation));
            ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
