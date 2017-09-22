package shop.ineed.app.ineed.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by jose on 9/8/17.
 *
 * Classe utilitária para a conversão de imagens na Base64 para Bitmap.
 * Para a possível utilização na mesma em um ImageView.
 */

public class Base64 {

    /**
     * Recebe um objeto String contendo uma sequencia de caracteres
     * que formam uma imagem na Base64 e retorna um Bitmap.
     *
     * @param encodedString
     * @return
     */
    public static Bitmap convertToBitmap(String encodedString) {
        String pureBase64Encoded;
        if (encodedString.contains("data:image/png;base64,")) {
            pureBase64Encoded = encodedString.replace("data:image/png;base64,", "");
        } else if (encodedString.contains("data:image/jpeg;base64,")) {
            pureBase64Encoded = encodedString.replace("data:image/jpeg;base64,", "");
        } else {
            pureBase64Encoded = encodedString;
        }
        byte[] decodedString = android.util.Base64.decode(pureBase64Encoded, android.util.Base64.CRLF);
        return (BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
    }
}
