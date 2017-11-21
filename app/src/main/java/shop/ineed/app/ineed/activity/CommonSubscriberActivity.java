package shop.ineed.app.ineed.activity;

import android.content.Context;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.sendbird.android.SendBird;

import shop.ineed.app.ineed.R;
import shop.ineed.app.ineed.util.PreferenceUtils;

/**
 * Created by antonio on 8/17/17.
 * Classe que contem métodos e atributos comuns entre a Activity SignIn e SignUp.
 */

public abstract class CommonSubscriberActivity extends BaseActivity {

    private String TAG = this.getClass().getSimpleName();

    @NotEmpty
    @Email(message = "O e-mail não corresponde a um endereço de e-mail valido.")
    protected EditText email;
    @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_SYMBOLS, message = "A senha deve ter no mínimo 6 caracteres alpha numéricos e um caractere especial.")
    protected EditText password;
    protected SweetAlertDialog dialogProgress;
    protected Validator validator;

    protected void showSnackbar(View view, String message) {
        Snackbar.make(view,
                message,
                Snackbar.LENGTH_LONG)
                .setAction("OK", null).show();
    }

    protected void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    protected void openProgressDialog(Context context) {
        dialogProgress = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        dialogProgress.getProgressHelper().setBarColor(ContextCompat.getColor(context, R.color.colorAccent));
        dialogProgress.setTitleText(getResources().getString(R.string.wait));
        dialogProgress.setCancelable(false);
        dialogProgress.show();
    }

    protected void closeProgressDialog() {
        dialogProgress.setCancelable(true);
        dialogProgress.dismiss();
    }

    // Métodos obrigatórios para a inicialização das views e do objeto user.
    abstract protected void initViews();

    abstract protected void initUser();

    // SendBird

    public static void connectToSendBird(final String userUID, final String userNickName, Context context){
        SendBird.connect(userUID, (user, e) -> {
            if(e != null){
                Log.e("connectToSendBird", e.getMessage());

            }

            PreferenceUtils.setConnected(context, true);

            updateCurrentUserInfo(userNickName);
            updateCurrentUserPushToken();
        });
    }

    public static void updateCurrentUserPushToken(){
        SendBird.registerPushTokenForCurrentUser(FirebaseInstanceId.getInstance().getToken(), (pushTokenRegistrationStatus, e) -> {

            if(e != null){
                //Error
                //Log.e(TAG, "Update user nickname failed");
                return;
            }

            //Log.i(TAG, "Push token registered.");


        });
    }

    public static void updateCurrentUserInfo(String userNickName){
        SendBird.updateCurrentUserInfo(userNickName, null, e -> {
            if(e != null){
                //Error

                //Log.e(TAG, "Update user nickname failed");

                return;
            }
        });
    }


}
