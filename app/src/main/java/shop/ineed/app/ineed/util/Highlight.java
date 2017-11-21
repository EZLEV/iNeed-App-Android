package shop.ineed.app.ineed.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jose on 10/27/17.
 *
 * Retorna o valor especifico para a marcação do termo.
 */

public class Highlight implements Parcelable {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.attributeName);
        dest.writeString(this.highlightedValue);
    }

    protected Highlight(Parcel in) {
        this.attributeName = in.readString();
        this.highlightedValue = in.readString();
    }

    public static final Parcelable.Creator<Highlight> CREATOR = new Parcelable.Creator<Highlight>() {
        @Override
        public Highlight createFromParcel(Parcel source) {
            return new Highlight(source);
        }

        @Override
        public Highlight[] newArray(int size) {
            return new Highlight[size];
        }
    };
}
