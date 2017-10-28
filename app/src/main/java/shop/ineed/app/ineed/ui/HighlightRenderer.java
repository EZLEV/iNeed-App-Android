package shop.ineed.app.ineed.ui;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import shop.ineed.app.ineed.R;

/**
 * Created by jose on 10/27/17.
 *
 *
 * NOTA: Este padrão não é à prova de balas (principalmente contra etiquetas aninhadas), mas é
 * suficiente para nossos propósitos.
 *
 * Marca o termo buscado, deixando o mesmo laranja.
 */

public class HighlightRenderer {
    private Context context;


    static final Pattern HIGHLIGHT_PATTERN = Pattern.compile("<em>([^<]*)</em>");

    public HighlightRenderer(Context context)
    {
        this.context = context;
    }

    public Spannable renderHighlights(String markupString)
    {
        SpannableStringBuilder result = new SpannableStringBuilder();
        Matcher matcher = HIGHLIGHT_PATTERN.matcher(markupString);
        int positionNow = 0;
        int positionAfter = 0;
        // For each highlight...
        while (matcher.find()) {
            // Append text before.
            result.append(markupString.substring(positionNow, matcher.start()));
            positionAfter += matcher.start() - positionNow;
            positionNow = matcher.start();

            // Anexar texto destacado.
            String highlightString = matcher.group(1);
            result.append(highlightString);
            result.setSpan(new BackgroundColorSpan(context.getResources().getColor(R.color.colorAccent)), positionAfter, positionAfter + highlightString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            positionAfter += highlightString.length();
            positionNow = matcher.end();
        }

        // Anexar texto depois.
        result.append(markupString.substring(positionNow));
        return result;
    }
}
