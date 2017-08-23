package shop.ineed.app.ineed.domain.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by antonio on 8/16/17.
 */

public class LibraryClass {
    private static String PREF = "shop.ineed.app.ineed.PREF";
    private static DatabaseReference firebase;

    public LibraryClass() {
    }

    public static DatabaseReference getFirebase() {
        if (firebase == null) {
            firebase = FirebaseDatabase.getInstance().getReference();
        }

        return (firebase);
    }

    static public void saveUserLogged(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public static String getUserLogged(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String token = sp.getString(key, "");
        return (token);
    }
}
