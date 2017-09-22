package shop.ineed.app.ineed.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.domain.Category;
import shop.ineed.app.ineed.interfaces.RecyclerClickListener;
import shop.ineed.app.ineed.util.Base64;

/**
 * Created by Jose on 8/27/2017.
 *
 * Class Adapter Categories
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    private String TAG = this.getClass().getSimpleName();
    private List<Category> mCategories;
    private Context mContext;
    private RecyclerClickListener mRecyclerClickListener;

    public CategoriesAdapter(Context context, List<Category> categories, RecyclerClickListener recyclerClickListener) {
        this.mContext = context;
        this.mCategories = categories;
        this.mRecyclerClickListener = recyclerClickListener;
    }

    @Override
    public CategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_categories, parent, false);

        CategoriesViewHolder holder = new CategoriesViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final CategoriesViewHolder holder, final int position) {

        Category category = mCategories.get(position);

        holder.txtCategory.setText(category.getValue());
        holder.ivIconCategory.setImageBitmap(Base64.convertToBitmap(category.getIcon()));

        if (mRecyclerClickListener != null) {
            holder.itemView.setOnClickListener(view -> mRecyclerClickListener.onClickRecyclerListener(holder.itemView, position));
        }
    }

    @Override
    public int getItemCount() {
        return this.mCategories != null ? this.mCategories.size() : 0;
    }

    static class CategoriesViewHolder extends RecyclerView.ViewHolder {

        TextView txtCategory;
        ImageView ivIconCategory;

        CategoriesViewHolder(View view) {
            super(view);
            txtCategory = (TextView) view.findViewById(R.id.txtTitleCategory);
            ivIconCategory = (ImageView) view.findViewById(R.id.ivIconCategory);
        }

    }

}
