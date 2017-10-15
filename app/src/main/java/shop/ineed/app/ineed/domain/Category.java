package shop.ineed.app.ineed.domain;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Jose on 8/27/2017.
 *
 * Class domain Category
 */

@org.parceler.Parcel
@IgnoreExtraProperties
public class Category {

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
}
