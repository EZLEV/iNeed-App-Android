package shop.ineed.app.ineed.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.v4.content.ContextCompat;

import java.io.ByteArrayOutputStream;

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

    /**
     * Faz a conversão de um Bitmap para Base64.
     * @param bitmap
     * @return
     */
    public static String convertToBase64(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        byte[] imageBytes = baos.toByteArray();

        String base64String = android.util.Base64.encodeToString(imageBytes, android.util.Base64.NO_WRAP);

        return base64String;
    }

    /**
     * Converte um objeto Drawable para um Bitmap.
     * @param context
     * @param drawableID
     * @return
     */
    public static Bitmap convertDrawableToBitmap (Context context, int drawableID) {

            Drawable drawable = ContextCompat.getDrawable(context, drawableID);
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            } else if (drawable instanceof VectorDrawable) {
                return getBitmap((VectorDrawable) drawable);
            } else {
                throw new IllegalArgumentException("unsupported drawable type");
            }

    }

    /**
     * Recupera um objeto bitmap através do recebimento de um VectorDrawable.
     *
     * @param vectorDrawable
     * @return
     */
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }
}
