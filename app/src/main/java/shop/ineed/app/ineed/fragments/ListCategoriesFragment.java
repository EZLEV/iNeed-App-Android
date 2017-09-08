package shop.ineed.app.ineed.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * A simple {@link Fragment} subclass.
 */
public class ListCategoriesFragment extends Fragment {


    private DatabaseReference reference;
    private List<Category> categories = new ArrayList<>();
    private ShimmerRecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_categories, container, false);

        recyclerView = (ShimmerRecyclerView) view.findViewById(R.id.recyclerCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final CategoriesAdapter adapter = new CategoriesAdapter(getActivity(), categories, onCategoryClickListener());
        recyclerView.setAdapter(adapter);
        recyclerView.showShimmerAdapter();

        reference = LibraryClass.getFirebase().child("categories");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categories.removeAll(categories);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.i("TAG", "key" + snapshot.getKey() + " " + snapshot.getValue());
                    Category category = snapshot.getValue(Category.class);
                    category.setKey(snapshot.getKey());
                    categories.add(category);
                }

                adapter.notifyDataSetChanged();
                recyclerView.hideShimmerAdapter();
                Log.i("ValueList: ", categories.size() + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", databaseError.getMessage());
            }
        });
        return view;
    }

    private RecyclerClickListener onCategoryClickListener (){
        return new RecyclerClickListener() {
            @Override
            public void onClickRecyclerListener(View view, int idx) {
                Category category = categories.get(idx);
                Intent intent = new Intent(getContext(), ProductsActivity.class);
                intent.putExtra("category", Parcels.wrap(category));
                startActivity(intent);
            }
        };
    }
}
