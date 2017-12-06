package shop.ineed.app.ineed.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.adapter_comments.view.*
import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.domain.Comments
import shop.ineed.app.ineed.interfaces.RecyclerClickListener

/**
 * Created by silva on 21/11/17.
 */
class CommentsAdapter(private val comments: List<Comments>, var onClick: RecyclerClickListener) : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    override fun onBindViewHolder(holder: CommentsAdapter.CommentsViewHolder, position: Int) {
        val comment = this.comments[position]
        val view = holder.itemView

        with(view){
            txtAuthorAdapterComment.text = comment.author
            txtDateAdapterComment.text = comment.date
            txtBodyAdapterComment.text = comment.body
            ratingAdapterComment.rating = comment.rating.toFloat()

            setOnClickListener { onClick.onClickRecyclerListener(view, position) }
        }
    }

    override fun getItemCount(): Int = comments.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsAdapter.CommentsViewHolder {
        val viewRoot = LayoutInflater.from(parent.context).inflate(R.layout.adapter_comments, parent, false)

        val holder = CommentsViewHolder(viewRoot)

        return holder
    }

    class CommentsViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
    }

    fun setOnClickListener(onClick: RecyclerClickListener) {
        this.onClick = onClick
    }

}