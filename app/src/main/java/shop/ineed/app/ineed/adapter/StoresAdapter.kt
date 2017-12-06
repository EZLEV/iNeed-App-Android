package shop.ineed.app.ineed.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.adapter_item_stores.view.*
import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.domain.Store
import shop.ineed.app.ineed.interfaces.RecyclerClickListener
import shop.ineed.app.ineed.util.ImageUtils

/**
 * Created by jose on 11/3/17.
 */

class StoresAdapter(private val stores: List<Store>, var onClick: RecyclerClickListener) : RecyclerView.Adapter<StoresAdapter.StoresViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoresViewHolder {
        val viewRoot = LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_stores, parent, false)
        return StoresViewHolder(viewRoot)
    }

    override fun onBindViewHolder(holder: StoresViewHolder, position: Int) {
        val store = this.stores[position]

        val view = holder.itemView

        with(view) {
            nameStoreAdapter.text = store.name
            txtDescriptionStoreAdapter.text = store.description
            ImageUtils.displayImageFromUrl(context, store.pictures[0], ivStoreDetailsAdapter)

            setOnClickListener { onClick.onClickRecyclerListener(view, position) }
        }
    }

    override fun getItemCount(): Int = this.stores.size

    class StoresViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    fun setOnClickListener(onClick: RecyclerClickListener) {
        this.onClick = onClick
    }
}
