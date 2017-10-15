package shop.ineed.app.ineed.adapter.ViewHolder;

import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.domain.Store;
import shop.ineed.app.ineed.interfaces.RecyclerClickListener;

/**
 * Created by jose on 10/12/17.
 * <p>
 * Class Adapter Stores
 */

public class StoresViewHolder extends RecyclerView.ViewHolder {

    private TextView txtNameStore;
    private TextView txtDescriptionStore;
    private CircleImageView ivStoreDetails;
    private RecyclerClickListener mClickListener;

    public StoresViewHolder(View itemView) {
        super(itemView);

        txtNameStore = itemView.findViewById(R.id.nameStore);
        ivStoreDetails = itemView.findViewById(R.id.ivStoreDetails);
        txtDescriptionStore = itemView.findViewById(R.id.txtDescriptionStore);
        itemView.setOnClickListener(view -> mClickListener.onClickRecyclerListener(view, getAdapterPosition(), ivStoreDetails));
    }

    public void setData(Store data) {
        txtNameStore.setText(data.getName());
        txtDescriptionStore.setText(data.getDescription());
        Picasso.with(itemView.getContext()).load(data.getPictures().get(0)).into(ivStoreDetails,
                PicassoPalette.with(data.getPictures().get(0), ivStoreDetails)
                        .use(PicassoPalette.Profile.VIBRANT)
                        .intoCallBack(palette -> {
                            Palette.Swatch swatch = palette.getVibrantSwatch();
                            if (swatch == null) {
                                Log.i("SWATCH", "null");
                                return;
                            }
                            ivStoreDetails.setBorderColor(swatch.getRgb());
                        })
        );

        ViewCompat.setTransitionName(ivStoreDetails, data.getName());
    }

    public void setOnClickListener(RecyclerClickListener onClickListener) {
        this.mClickListener = onClickListener;
    }
}
