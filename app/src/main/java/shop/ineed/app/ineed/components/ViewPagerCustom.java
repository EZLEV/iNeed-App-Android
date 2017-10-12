package shop.ineed.app.ineed.components;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by jose on 10/12/17.
 *
 * Customização do componente ViewPager para desabilitar o scroll horizontal da pagina.
 */

public class ViewPagerCustom extends ViewPager {

    private boolean isPagingEnabled;

    public ViewPagerCustom(Context context) {
        super(context);
        this.isPagingEnabled = true;
    }

    public ViewPagerCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isPagingEnabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    /**
     * Para telefones samsung para evitar que as teclas de comutação da guia mostrem no teclado
     *
     * @param event
     * @return
     */
    @Override
    public boolean executeKeyEvent(KeyEvent event) {
        return isPagingEnabled && super.executeKeyEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    /**
     * Método que habilita e desabilita o scroll horizontal.
     * @param enabled
     */
    public void setScrollHorizontal(boolean enabled) {
        this.isPagingEnabled = enabled;
    }
}
