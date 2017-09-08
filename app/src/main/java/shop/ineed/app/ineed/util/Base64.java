package shop.ineed.app.ineed.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by jose on 9/8/17.
 */

public class Base64 {

    public static Bitmap convertToBitmap(String encodedString){
        String pureBase64Encoded;
        if(encodedString.contains("data:image/png;base64,")){
            pureBase64Encoded = encodedString.replace("data:image/png;base64,", "");
        }else if(encodedString.contains("data:image/jpeg;base64,")){
            pureBase64Encoded = encodedString.replace("data:image/jpeg;base64,", "");
        }else{
            pureBase64Encoded = encodedString;
        }
        byte[] decodedString = android.util.Base64.decode(pureBase64Encoded, android.util.Base64.CRLF);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        return (decodedByte);
    }
}
