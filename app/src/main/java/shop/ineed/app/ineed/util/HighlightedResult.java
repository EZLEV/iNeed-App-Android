package shop.ineed.app.ineed.util;

import java.util.Map;

import shop.ineed.app.ineed.domain.Product;

/**
 * Created by jose on 10/27/17.
 *
 * Retorna um objeto Java correspondente a pesquisa e marca o termo buscado.
 */

@org.parceler.Parcel
public class HighlightedResult {

    private Product result;
    private Map<String, Highlight> highlights;

    public HighlightedResult(Product result, Map<String, Highlight> highlights) {
        this.result = result;
        this.highlights = highlights;
    }

    public HighlightedResult() {
    }

    public Product getResult() {
        return result;
    }

    public Highlight getHighlight(String attributeName) {
        return highlights.get(attributeName);
    }

    public void addHighlight(String attributeName, Highlight highlight) {
        highlights.put(attributeName, highlight);
    }

}
