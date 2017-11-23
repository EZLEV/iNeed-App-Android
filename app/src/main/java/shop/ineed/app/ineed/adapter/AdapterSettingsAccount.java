package shop.ineed.app.ineed.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.domain.Settings;

/**
 * Created by jose on 10/29/17.
 */

public class AdapterSettingsAccount extends BaseAdapter {

    private Context context;
    private List<Settings> settings;

    public AdapterSettingsAccount(Context context, List<Settings> settings){
        super();
        this.context = context;
        this.settings = settings;
    }


    @Override
    public int getCount() {
        return settings != null ? settings.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return settings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View viewRoot = LayoutInflater.from(context).inflate(R.layout.adapter_settings_account, viewGroup, false);

        TextView txtTitleSetting = (TextView) viewRoot.findViewById(R.id.txtSettings);
        ImageView icon = (ImageView) viewRoot.findViewById(R.id.ivIconSettings);
        ImageView iconMore = (ImageView) viewRoot.findViewById(R.id.ivIconMoreSettings);

        Settings s = settings.get(position);

        txtTitleSetting.setText(s.getTitleSetting());
        icon.setImageResource(s.getIcon());
        if(s.getIconMore() != 0){
            iconMore.setImageResource(s.getIconMore());
        }

        return viewRoot;
    }
}
