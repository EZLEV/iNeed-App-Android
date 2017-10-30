package shop.ineed.app.ineed.domain;

import android.support.annotation.NonNull;

/**
 * Created by jose on 10/29/17.
 */

public class Settings {

    private String titleSetting;
    private int icon;
    private int iconMore;

    public Settings(String titleSetting, int icon, @NonNull int iconMore) {
        this.titleSetting = titleSetting;
        this.icon = icon;
        this.iconMore = iconMore;
    }


    public String getTitleSetting() {
        return titleSetting;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIconMore() {
        return iconMore;
    }
}
