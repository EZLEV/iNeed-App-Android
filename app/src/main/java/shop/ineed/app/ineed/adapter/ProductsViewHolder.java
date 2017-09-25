package shop.ineed.app.ineed.adapter;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.domain.Product;
import shop.ineed.app.ineed.interfaces.RecyclerClickListener;
import shop.ineed.app.ineed.util.Base64;

/**
 * Created by Jose on 9/5/2017.
 * <p>
 * Class Adapter Products
 */

public class ProductsViewHolder extends RecyclerView.ViewHolder {

    private TextView txtTitle;
    private TextView txtPrice;
    private TextView txtDescription;
    private ImageView ivProduct;
    private RecyclerClickListener mClickListener;

    ProductsViewHolder(View view) {
        super(view);
        txtTitle = (TextView) view.findViewById(R.id.titleProductAdapter);
        txtPrice = (TextView) view.findViewById(R.id.priceProductAdapter);
        txtDescription = (TextView) view.findViewById(R.id.descriptionProductAdapter);
        ivProduct = (ImageView) view.findViewById(R.id.imageProductAdapter);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onClickRecyclerListener(view, getAdapterPosition(), ivProduct);
            }
        });
    }

    public void setData(Product product) {
        txtTitle.setText(product.getName());
        txtPrice.setText("R$" + String.valueOf(product.getPrice()));
        txtDescription.setText(product.getDescription());

        List<String> image = product.getPictures();
        ivProduct.setImageBitmap(Base64.convertToBitmap(image.get(0)));
        ViewCompat.setTransitionName(ivProduct, product.getName());
    }

    public void setOnClickListener(RecyclerClickListener onClickListener){
        this.mClickListener = onClickListener;
    }
}

