package shop.ineed.app.ineed.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.List;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.domain.Product;
import shop.ineed.app.ineed.util.Base64;

/**
 * Created by Jose on 9/5/2017.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    private List<Product> mProducts;
    private Context mContext;

    public ProductsAdapter(Context mContext, List<Product> mProducts) {
        this.mProducts = mProducts;
        this.mContext = mContext;
    }


    @Override
    public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_products, parent, false);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductsViewHolder holder, int position) {
        holder.setData(mProducts.get(position));
    }

    @Override
    public int getItemCount() {
        return this.mProducts != null ? this.mProducts.size() : 0;
    }

    class ProductsViewHolder extends RecyclerView.ViewHolder {
        private TextView txtPrice;
        private TextView txtDescription;
        private ImageView ivProduct;


        ProductsViewHolder(View view) {
            super(view);
            txtPrice = (TextView) view.findViewById(R.id.priceProduct);
            txtDescription = (TextView) view.findViewById(R.id.descriptionProduct);
            ivProduct = (ImageView) view.findViewById(R.id.ivProduct);
        }

        private void setData(Product product){
            txtPrice.setText(String.valueOf(product.getPrice()));
            txtDescription.setText(product.getDescription());

            List<String> image = product.getPictures();
            ivProduct.setImageBitmap(Base64.convertToBitmap(image.get(0)));
        }
    }
}
