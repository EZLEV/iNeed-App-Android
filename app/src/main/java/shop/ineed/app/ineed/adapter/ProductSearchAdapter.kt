package shop.ineed.app.ineed.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.adapter_cell_search_product.view.*

import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.activity.DetailsProductsActivity
import shop.ineed.app.ineed.domain.Product
import shop.ineed.app.ineed.util.HighlightRenderer
import shop.ineed.app.ineed.util.ImageUtils

/**
 * Created by jose on 10/27/17.
 *
 * ArrayAdapter para gerenciar os elementos da pesquisa de produtos.
 */

class ProductSearchAdapter(private val baseContext: Context, resource: Int) : ArrayAdapter<Product>(baseContext, resource) {

    private val highlightRenderer: HighlightRenderer = HighlightRenderer(baseContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var cell = convertView as ViewGroup?

        if (cell == null) {
            cell = LayoutInflater.from(baseContext).inflate(R.layout.adapter_cell_search_product, parent, false) as ViewGroup
        }

        // Result
        val result = getItem(position)

        with(cell){
            txtNameProductSearchAdapter.text = highlightRenderer.renderHighlights(result!!.name)
            txtDescriptionProductSearchAdapter.text = result.description
            txtPriceProductSearchAdapter.text = "R$ " + result.price
            ImageUtils.displayImageFromUrl(context, result.pictures[0], ivProductSearchAdapter)

            setOnClickListener {
                val intent = Intent(context, DetailsProductsActivity::class.java)
                intent.putExtra("product", result)
                context.startActivity(intent)
            }
        }
        return cell
    }
}
