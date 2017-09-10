package shop.ineed.app.ineed.domain;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Jose on 8/27/2017.
 */

@org.parceler.Parcel
@IgnoreExtraProperties
public class Category {

    private String value;
    @Exclude
    private String key;

    public Category(){
    }

    public  Category(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}
