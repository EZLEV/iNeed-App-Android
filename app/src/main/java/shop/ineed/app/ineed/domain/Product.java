package shop.ineed.app.ineed.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;


import java.util.List;

/**
 * Created by Jose on 9/5/2017.
 * <p>
 * Class domain Product
 */

@IgnoreExtraProperties
public class Product implements Parcelable {

    private String id;
    private List<String> categories = null;
    private String description;
    private List<String> downVotes;
    private int downVotesCount;
    private String name;
    private List<String> pictures = null;
    private String store;
    private double price;
    private List<String> upVotes;
    private int upVotesCount;

    public Product() {
    }

    public Product(String id, List<String> categories, String description, List<String> downVotes, int downVotesCount, String name, List<String> pictures, String store, double price, List<String> upVotes, int upVotesCount) {
        this.id = id;
        this.categories = categories;
        this.description = description;
        this.downVotes = downVotes;
        this.downVotesCount = downVotesCount;
        this.name = name;
        this.pictures = pictures;
        this.store = store;
        this.price = price;
        this.upVotes = upVotes;
        this.upVotesCount = upVotesCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(List<String> downVotes) {
        this.downVotes = downVotes;
    }

    public long getDownVotesCount() {
        return downVotesCount;
    }

    public void setDownVotesCount(int downVotesCount) {
        this.downVotesCount = downVotesCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(List<String> upVotes) {
        this.upVotes = upVotes;
    }

    public long getUpVotesCount() {
        return upVotesCount;
    }

    public void setUpVotesCount(int upVotesCount) {
        this.upVotesCount = upVotesCount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeStringList(this.categories);
        dest.writeString(this.description);
        dest.writeStringList(this.downVotes);
        dest.writeInt(this.downVotesCount);
        dest.writeString(this.name);
        dest.writeStringList(this.pictures);
        dest.writeString(this.store);
        dest.writeDouble(this.price);
        dest.writeStringList(this.upVotes);
        dest.writeInt(this.upVotesCount);
    }

    protected Product(Parcel in) {
        this.id = in.readString();
        this.categories = in.createStringArrayList();
        this.description = in.readString();
        this.downVotes = in.createStringArrayList();
        this.downVotesCount = in.readInt();
        this.name = in.readString();
        this.pictures = in.createStringArrayList();
        this.store = in.readString();
        this.price = in.readDouble();
        this.upVotes = in.createStringArrayList();
        this.upVotesCount = in.readInt();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
