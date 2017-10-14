package shop.ineed.app.ineed.adapter.ViewHolder;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.domain.Category;
import shop.ineed.app.ineed.interfaces.RecyclerClickListener;
import shop.ineed.app.ineed.util.Base64;

/**
 * Created by Jose on 8/27/2017.
 * <p>
 * Class Adapter Categories
 */

public class CategoriesViewHolder extends RecyclerView.ViewHolder {

    private TextView txtCategory;
    private ImageView ivIconCategory;
    private RecyclerClickListener mClickListener;

    CategoriesViewHolder(View view) {
        super(view);
        txtCategory = (TextView) view.findViewById(R.id.txtTitleCategory);
        ivIconCategory = (ImageView) view.findViewById(R.id.ivIconCategory);
        itemView.setOnClickListener(view1 -> mClickListener.onClickRecyclerListener(view1, getAdapterPosition()));
    }

    public void setDate(Category category){
        txtCategory.setText(category.getValue());
        ivIconCategory.setImageBitmap(Base64.convertToBitmap(category.getIcon()));
        ivIconCategory.setColorFilter(Color.parseColor(category.getColor()));
    }

    public void setOnClickListener(RecyclerClickListener onClickListener){
        this.mClickListener = onClickListener;
    }

}
