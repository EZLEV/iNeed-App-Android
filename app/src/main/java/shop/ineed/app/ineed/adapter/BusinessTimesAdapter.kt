package shop.ineed.app.ineed.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.adapter_business_times.view.*

import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.domain.BusinessTimes

/**
 * Created by silva on 21/11/17.
 */

class BusinessTimesAdapter(private val businessTimes: List<BusinessTimes>) : RecyclerView.Adapter<BusinessTimesAdapter.BusinessTimesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BusinessTimesViewHolder {
        val viewRoot = LayoutInflater.from(parent?.context).inflate(R.layout.adapter_business_times, parent, false)
        return BusinessTimesViewHolder(viewRoot)
    }

    override fun getItemCount(): Int = businessTimes.size

    override fun onBindViewHolder(holder: BusinessTimesViewHolder, position: Int) {
        val store = businessTimes[position]
        val view = holder.itemView

        with(view) {
            txtDayAdapterBusinessTimes.text = store.day
            txtOpenAdapterBusinessTimes.text = store.open
            txtCloseAdapterBusinessTimes.text = store.close
        }
    }

    class BusinessTimesViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
    }
}
