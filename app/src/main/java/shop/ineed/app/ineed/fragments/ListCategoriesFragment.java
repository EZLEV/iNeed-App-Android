package shop.ineed.app.ineed.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.activity.ProductsActivity;
import shop.ineed.app.ineed.adapter.CategoriesAdapter;
import shop.ineed.app.ineed.domain.Category;
import shop.ineed.app.ineed.domain.util.LibraryClass;
import shop.ineed.app.ineed.interfaces.RecyclerClickListener;

/**
 * Lista todas as categoria existente no Firebase iNeed.
 */
public class ListCategoriesFragment extends BaseFragment {

    private List<Category> mCategories;
    private ShimmerRecyclerView mRecyclerView;
    private CategoriesAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategories = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_categories, container, false);
        mRecyclerView = (ShimmerRecyclerView) view.findViewById(R.id.recyclerCategories);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CategoriesAdapter(getActivity(), mCategories, onCategoryClickListener());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.showShimmerAdapter();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Faz a chamada ao Firebase
        DatabaseReference reference = LibraryClass.getFirebase().child("categories");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCategories.removeAll(mCategories);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.i("TAG", "key" + snapshot.getKey() + " " + snapshot.getValue());
                    Category category = snapshot.getValue(Category.class);
                    category.setKey(snapshot.getKey());
                    mCategories.add(category);
                }

                mAdapter.notifyDataSetChanged();
                mRecyclerView.hideShimmerAdapter();
                Log.i("ValueList: ", mCategories.size() + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", databaseError.getMessage());
            }
        });
    }

    private RecyclerClickListener onCategoryClickListener (){
        return new RecyclerClickListener() {
            @Override
            public void onClickRecyclerListener(View view, int idx) {
                Category category = mCategories.get(idx);
                Intent intent = new Intent(getContext(), ProductsActivity.class);
                intent.putExtra("category", Parcels.wrap(category));
                startActivity(intent);
            }
            @Override
            public void onClickRecyclerListener(View view, int position, View viewAnimation) {
            }
        };
    }
}
