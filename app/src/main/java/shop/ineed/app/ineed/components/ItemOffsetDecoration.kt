package shop.ineed.app.ineed.components

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by jose on 11/13/17.
 *
 * Gerenciamento do espaçamento entre produtos do AdapterProducts.
 * Implementa a classe abstract ItemDecoration().
 */

class ItemOffsetDecoration(private val offset: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State?) {
        val bottonIndex: Int

        if (parent.adapter.itemCount % 2 == 0) {
            bottonIndex = parent.adapter.itemCount - 2
        } else {
            bottonIndex = parent.adapter.itemCount - 1
        }

        if (parent.getChildAdapterPosition(view) < bottonIndex) {
            outRect.bottom = offset
        } else {
            outRect.bottom = 0
        }

        if (parent.getChildAdapterPosition(view) > 1) {
            outRect.top = offset
        } else {
            outRect.top = 0
        }

        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.right = offset
            outRect.left = 0
        } else {
            outRect.right = 0
            outRect.left = offset
        }
    }
}