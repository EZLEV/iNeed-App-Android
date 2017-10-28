package shop.ineed.app.ineed.util;

/**
 * Created by jose on 10/27/17.
 *
 * Retorna o valor especifico para a marcação do termo.
 */

@org.parceler.Parcel
public class Highlight {
    private String attributeName;
    private String highlightedValue;

    public Highlight(String attributeName, String highlightedValue)
    {
        this.attributeName = attributeName;
        this.highlightedValue = highlightedValue;
    }

    public Highlight(){

    }

    public String getAttributeName()
    {
        return attributeName;
    }

    public String getHighlightedValue()
    {
        return highlightedValue;
    }
}
