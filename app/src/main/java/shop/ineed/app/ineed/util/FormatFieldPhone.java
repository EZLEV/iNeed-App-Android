package shop.ineed.app.ineed.util;

import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import java.util.Locale;

/**
 * Created by jose on 10/29/17.
 */

public class FormatFieldPhone implements TextWatcher {
    private boolean mFormatting;
    private int mAfter;

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mAfter = after;
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!mFormatting) {
            mFormatting = true;
            if (mAfter != 0) {
                String num = s.toString();
                String data = "";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    data = PhoneNumberUtils.formatNumber(num, Locale.getDefault().getCountry());
                } else {
                    data = PhoneNumberUtils.formatNumber(num);
                }
                if (data != null) {
                    s.clear();
                    s.append(data);
                    Log.i("Number", data);
                }
            }
            mFormatting = false;
        }
    }
}
