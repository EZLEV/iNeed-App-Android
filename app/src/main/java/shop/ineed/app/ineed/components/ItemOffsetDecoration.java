package shop.ineed.app.ineed.components;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by jose on 11/13/17.
 */

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
    private int offset;

    public ItemOffsetDecoration(int offset) {
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        int bottonIndex;
        if (parent.getAdapter().getItemCount() % 2 == 0) {
            bottonIndex = parent.getAdapter().getItemCount() - 2;
        } else {
            bottonIndex = parent.getAdapter().getItemCount() - 1;
        }

        if (parent.getChildAdapterPosition(view) < bottonIndex) {
            outRect.bottom = offset;
        } else {
            outRect.bottom = 0;
        }

        if (parent.getChildAdapterPosition(view) > 1) {
            outRect.top = offset;
        } else {
            outRect.top = 0;
        }

        if ((parent.getChildAdapterPosition(view) % 2) == 0) {
            outRect.right = offset;
            outRect.left = 0;
        } else {
            outRect.right = 0;
            outRect.left = offset;
        }

    }
}