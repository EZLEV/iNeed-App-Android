package shop.ineed.app.ineed.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.adapter_settings_account.view.*

import shop.ineed.app.ineed.R
import shop.ineed.app.ineed.domain.Settings

/**
 * Created by jose on 10/29/17.
 *
 * Adaptador das configurações do usuário.
 */

class AdapterSettingsAccount(private val settings: List<Settings>?) : BaseAdapter() {


    override fun getCount(): Int = settings?.size ?: 0

    override fun getItem(position: Int) = settings!![position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        val viewRoot = LayoutInflater.from(viewGroup.context).inflate(R.layout.adapter_settings_account, viewGroup, false)

        val setting = settings!![position]

        with(viewRoot){
            txtSettingsAdapter.text = setting.titleSetting
            ivIconSettingsAdapter.setImageResource(setting.icon)
            if (setting.iconMore != 0) {
                ivIconMoreSettingsAdapter.setImageResource(setting.iconMore)
            }
        }
        return viewRoot
    }
}
