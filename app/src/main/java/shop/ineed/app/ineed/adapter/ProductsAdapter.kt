package shop.ineed.app.ineed.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.adapter_item_products.view.*

import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.domain.Product
import shop.ineed.app.ineed.interfaces.RecyclerClickListener
import shop.ineed.app.ineed.util.ImageUtils

/**
 * Created by jose on 11/3/17.
 */

class ProductsAdapter(val products: List<Product>, var onClick: RecyclerClickListener) : RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val viewRoot = LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_products, parent, false)
        return ProductsViewHolder(viewRoot)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = this.products[position]

        val view = holder.itemView

        with(view) {
            ImageUtils.displayImageFromUrl(context, product.pictures[0], ivProductAdapter)
            titleProductAdapter.text = product.name
            priceProductAdapter.text = "R$ " + product.price
            descriptionProductAdapter.text = product.description

            setOnClickListener { onClick.onClickRecyclerListener(view, position) }
        }
    }

    override fun getItemCount(): Int = this.products.size

    fun setOnClickListener(onClick: RecyclerClickListener){
        this.onClick = onClick
    }

    class ProductsViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
    }
}
