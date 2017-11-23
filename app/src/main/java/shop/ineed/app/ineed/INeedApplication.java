package shop.ineed.app.ineed;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.FirebaseApp;
import com.sendbird.android.SendBird;

/**
 * Created by antonio on 8/5/17.
 * <p>
 * Class Singleton.
 */

public class INeedApplication extends Application {

    private String TAG = this.getClass().getSimpleName();
    private static INeedApplication mInstance = null;

    private static final String APP_ID = "0AE653E2-CB57-4945-A496-00C12C0BC0B8"; // US-1 Demo
    public static final String VERSION = "3.0.36";

    public static INeedApplication getInstance() {
        return mInstance; // Singleton
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        Log.d(TAG, "INeedApplication.onCreate");

        // SendBird
        SendBird.init(APP_ID, getApplicationContext());

        // Firebase
        FirebaseApp.initializeApp(getApplicationContext());

        // Salva a intância para termos acesso como Singleton
        mInstance = this;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "INeedApplication.onTerminate");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(INeedApplication.this);
    }
}
