package shop.ineed.app.ineed.domain.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import shop.ineed.app.ineed.domain.User;

/**
 * Created by antonio on 8/16/17.
 *
 * Classe utilitária para o gerenciamento do DatabaseReference.
 * Recupera e salva dados do usuário.
 */

public class LibraryClass {
    private static String PREF = "shop.ineed.app.ineed.PREF";
    private static DatabaseReference mFirebase;

    public LibraryClass() {
    }

    /**
     * Recupera uma instancia de DatabaseReference se a mesma não for nula
     * e retorna um objeto DatabaseReference
     * @return
     */
    public static DatabaseReference getFirebase() {
        if (mFirebase == null) {
            mFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return (mFirebase);
    }

    /**
     * Salva nas preferencias o login key do usuário
     * @param context
     * @param key
     * @param value
     */
    static public void saveUserLogged(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    /**
     * Recupera nas preferencias o login key do usuário e retorna o mesmo em forma de String
     * @param context
     * @param key
     * @return
     */
    public static String getUserLogged(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return (sp.getString(key, ""));
    }

    /**
     *
     * @param context
     * @return
     */
    public static boolean isUserLogged(Context context){
        if(getUserLogged(context, User.PROVIDER).equals("")){
            return false;
        }else{
            return true;
        }
    }
}
