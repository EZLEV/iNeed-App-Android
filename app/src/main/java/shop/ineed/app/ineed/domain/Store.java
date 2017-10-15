package shop.ineed.app.ineed.domain;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by jose on 10/12/17.
 *
 * Class domain
 */
@org.parceler.Parcel
@IgnoreExtraProperties
public class Store {
    private String id;
    private String cellphone;
    private String cnpj;
    private String color;
    private String description;
    private Location location;
    private String name;
    private List<String> paymentWays;
    private String phone;
    private List<String> pictures;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCellPhone() {
        return cellphone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellphone = cellPhone;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPaymentWays() {
        return paymentWays;
    }

    public void setPaymentWays(List<String> paymentWays) {
        this.paymentWays = paymentWays;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }
}
