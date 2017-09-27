package shop.ineed.app.ineed.util;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import shop.ineed.app.ineed.R;

/**
 * Created by jose on 8/22/17.
 *
 * Classe existente que verifica a existência de erros que podem ocorrer no login e na inscrição do
 * usuário.
 *
 * A classe tem um método estático onde recebe como parâmetro uma instancia da Activity (Contexto)
 * e uma instancia de Task<AuthResult>.
 */

public class FirebaseError {

    /**
     * Dependendo da instancia do erro e retornado uma mensagem em forma de string para a entidade que
     * a invocou. A instancia que a invocou trata essa String da melhor forma.
     *
     * @param activity
     * @param task
     * @return
     */
    public static String verify(Activity activity, @NonNull Task<AuthResult> task) {
        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
            //If email already registered.
            return (activity.getResources().getString(R.string.firebase_email_already_registered));
        } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
            //If email are in incorret  format
            return (activity.getResources().getString(R.string.firebase_email_invalid));
        } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
            //if password not 'stronger'
            return (activity.getResources().getString(R.string.firebase_password_invalid));
        } else {
            //OTHER THING
            return (activity.getResources().getString(R.string.firebase_other));
        }
    }
}
