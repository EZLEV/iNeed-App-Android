package shop.ineed.app.ineed.adapter

import android.content.Context
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.slide_adapter.view.*

import com.squareup.picasso.Picasso

import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.interfaces.RecyclerClickListener

/**
 * Created by jose on 9/9/17.
 *
 * Class gerenciadora, slide.
 */

class SlideAdapter(private val mContext: Context, private val mImages: List<String>, var onClick: RecyclerClickListener) : PagerAdapter() {

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return mImages.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(mContext).inflate(R.layout.slide_adapter, container, false)

        with(view){
            Picasso.with(mContext).load(mImages[position]).into(imageSlideViewPhotoAdapter)
            Log.d("SLIDE", mImages[position])

            setOnClickListener{ onClick.onClickRecyclerListener(view, position) }
            container.addView(view, 0)
        }


        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun saveState(): Parcelable? {
        return null
    }

    fun setOnClickListener(onClick: RecyclerClickListener){
        this.onClick = onClick
    }
}
