package shop.ineed.app.ineed.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.domain.Category;

/**
 * Created by Jose on 8/27/2017.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    private String TAG = this.getClass().getSimpleName();
    private List<Category> categories;
    private Context context;

    public CategoriesAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item_categories, parent, false);

        CategoriesViewHolder holder = new CategoriesViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(CategoriesViewHolder holder, int position) {

        Category category = categories.get(position);

        holder.txtCategory.setText(category.getValue());

    }

    @Override
    public int getItemCount() {
        return this.categories != null ? this.categories.size() : 0;
    }

    static class CategoriesViewHolder extends RecyclerView.ViewHolder {

        TextView txtCategory;
        ProgressBar progressBar;

        CategoriesViewHolder(View view) {
            super(view);

            txtCategory = (TextView) view.findViewById(R.id.txtTitleCategory);
        }

    }

}
