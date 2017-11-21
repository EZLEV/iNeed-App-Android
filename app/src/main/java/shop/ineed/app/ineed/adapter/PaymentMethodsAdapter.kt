package shop.ineed.app.ineed.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.adapter_payment_methods.view.*

import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.domain.PaymentMethods

/**
 * Created by silva on 21/11/17.
 */

class PaymentMethodsAdapter(private val paymentMethods: List<PaymentMethods>): RecyclerView.Adapter<PaymentMethodsAdapter.PaymentMethodsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PaymentMethodsViewHolder {
        val viewRoot = LayoutInflater.from(parent?.context).inflate(R.layout.adapter_payment_methods, parent, false)
        return PaymentMethodsViewHolder(viewRoot)
    }

    override fun getItemCount(): Int = paymentMethods.size

    override fun onBindViewHolder(holder: PaymentMethodsViewHolder, position: Int) {
       val payment = paymentMethods[position]
        val view = holder.itemView

        with(view){
            ivIconPaymentMethodsAdapter.setImageResource(payment.icon)
            txtNamePaymentMethodsAdapter.text = payment.name
        }
    }

    class PaymentMethodsViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
    }
}
