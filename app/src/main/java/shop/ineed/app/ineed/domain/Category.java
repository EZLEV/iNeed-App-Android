package shop.ineed.app.ineed.domain;

import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Jose on 8/27/2017.
 *
 * Class domain Category
 */

@IgnoreExtraProperties
public class Category implements Parcelable {

    private String value;
    private String icon;
    //private String color;
    @Exclude
    private String key;

    public Category(){
    }

    public  Category(String value, String icon){
        this.value = value;
        this.icon = icon;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    //public String getColor() {
    //    return color;
    //}

    //public void setColor(String color) {
    //    this.color = color;
    //}

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(this.value);
        dest.writeString(this.icon);
        dest.writeString(this.key);
    }

    protected Category(android.os.Parcel in) {
        this.value = in.readString();
        this.icon = in.readString();
        this.key = in.readString();
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(android.os.Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
