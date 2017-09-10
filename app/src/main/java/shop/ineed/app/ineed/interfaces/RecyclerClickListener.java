package shop.ineed.app.ineed.interfaces;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by jose on 9/8/17.
 */


public interface RecyclerClickListener {
    void onClickRecyclerListener(View view, int position);
    void onClickRecyclerListener(View view,  int position, ImageView imageView);
}

