package shop.ineed.app.ineed;

import android.app.Application;
import android.util.Log;

/**
 * Created by antonio on 8/5/17.
 *
 * Class Singleton.
 */

public class INeedApplication extends Application {

    private String TAG = this.getClass().getSimpleName();
    private static INeedApplication mInstance = null;

    public static INeedApplication getInstance(){
        return mInstance; // Singleton
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "INeedApplication.onCreate");
        // Salva a intância para termos acesso como Singleton
        mInstance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "INeedApplication.onTerminate");
    }
}
