package shop.ineed.app.ineed.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.domain.Category
import shop.ineed.app.ineed.interfaces.RecyclerClickListener
import shop.ineed.app.ineed.util.Base64

/**
 * Created by jose on 11/3/17.
 */

class CategoriesAdapter(
        val categories: List<Category>,
        val layout: Int,
        var onClick: RecyclerClickListener) : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    class CategoriesViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        var txtTitleCategoryAdapter: TextView
        var ivIconCategoryAdapter: ImageView

        init {
            txtTitleCategoryAdapter = view.findViewById(R.id.txtTitleCategoryAdapter)
            ivIconCategoryAdapter = view.findViewById(R.id.ivIconCategoryAdapter)
        }

        fun setData(category: Category) {
            txtTitleCategoryAdapter.text = category.value
            ivIconCategoryAdapter.setImageBitmap(Base64.convertToBitmap(category.icon))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val viewRoot = LayoutInflater.from(parent.context).inflate(layout, parent, false)

        val holder = CategoriesViewHolder(viewRoot)

        return holder
    }


    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = this.categories[position]

        val view = holder.itemView

        with(view) {
            holder.setData(category)
            setOnClickListener { onClick.onClickRecyclerListener(view, position) }
        }
    }

    override fun getItemCount(): Int = this.categories.size

    fun setOnClickListener(onClick: RecyclerClickListener) {
        this.onClick = onClick
    }
}
