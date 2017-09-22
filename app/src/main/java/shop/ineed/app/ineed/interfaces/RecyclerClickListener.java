package shop.ineed.app.ineed.interfaces;

import android.view.View;

/**
 * Created by jose on 9/8/17.
 *
 * Interface que define os métodos de clique no RecyclerView.
 */


public interface RecyclerClickListener {
    // Clique simples
    void onClickRecyclerListener(View view, int position);
    // Clique com animação de transição
    void onClickRecyclerListener(View view,  int position, View viewAnimation);
}

