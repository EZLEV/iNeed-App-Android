package shop.ineed.app.ineed.components

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

/**
 * Created by jose on 11/3/17.
 *
 * O RecyclerView retorna o ultimo elemento da pilha, o total de elementos e o primeiro elemento da pilha.
 */

abstract class EndlessRecyclerOnScrollListener(private val mLinearLayoutManager: StaggeredGridLayoutManager) : RecyclerView.OnScrollListener() {

    private var previousTotal = 0
    private var loading = true
    private val visibleThreshold = 5
    private var firstVisibleItem: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0

    private var current_page = 1

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView!!.childCount
        totalItemCount = mLinearLayoutManager.itemCount

        var firstVisibleItems: IntArray? = null
        firstVisibleItems = mLinearLayoutManager.findFirstVisibleItemPositions(firstVisibleItems)
        if (firstVisibleItems != null && firstVisibleItems.size > 0) {
            firstVisibleItem = firstVisibleItems[0]
        }

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        // Fim alcançado
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {

            // Faz alguma coisa
            current_page++

            onLoadMore(firstVisibleItem, visibleItemCount, totalItemCount, dy)

            loading = true
        }
    }

    abstract fun onLoadMore(firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int, dy: Int)

    companion object {
        var TAG = EndlessRecyclerOnScrollListener::class.java.simpleName
    }
}

