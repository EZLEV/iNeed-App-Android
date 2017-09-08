package shop.ineed.app.ineed.domain;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by Jose on 9/5/2017.
 */

@IgnoreExtraProperties
public class Product {
    private List<String> categories = null;
    private String description;
    private String name;
    private List<String> pictures = null;
    private String store;
    private double price;

    public Product(){}

    public Product(List<String> categories, String description, String name, List<String> pictures, String store, double price) {
        this.categories = categories;
        this.description = description;
        this.name = name;
        this.pictures = pictures;
        this.store = store;
        this.price = price;
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
}
