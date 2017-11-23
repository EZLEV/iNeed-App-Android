package shop.ineed.app.ineed.util;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

import shop.ineed.app.ineed.domain.Product;

/**
 * Created by jose on 10/27/17.
 *
 * Retorna um objeto Java correspondente a pesquisa e marca o termo buscado.
 */

public class HighlightedResult implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.result, flags);
        dest.writeInt(this.highlights.size());
        for (Map.Entry<String, Highlight> entry : this.highlights.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
    }

    protected HighlightedResult(Parcel in) {
        this.result = in.readParcelable(Product.class.getClassLoader());
        int highlightsSize = in.readInt();
        this.highlights = new HashMap<String, Highlight>(highlightsSize);
        for (int i = 0; i < highlightsSize; i++) {
            String key = in.readString();
            Highlight value = in.readParcelable(Highlight.class.getClassLoader());
            this.highlights.put(key, value);
        }
    }

    public static final Parcelable.Creator<HighlightedResult> CREATOR = new Parcelable.Creator<HighlightedResult>() {
        @Override
        public HighlightedResult createFromParcel(Parcel source) {
            return new HighlightedResult(source);
        }

        @Override
        public HighlightedResult[] newArray(int size) {
            return new HighlightedResult[size];
        }
    };
}
