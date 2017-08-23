package shop.ineed.app.ineed.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by antonio on 8/16/17.
 */

public class Network {
    public static boolean verify(Context context) {
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
