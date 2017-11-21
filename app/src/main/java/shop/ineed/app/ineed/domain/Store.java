package shop.ineed.app.ineed.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;


import java.util.List;

/**
 * Created by jose on 10/12/17.
 *
 * Class domain
 */
@IgnoreExtraProperties
public class Store implements Parcelable {
    private String id;
    private List<BusinessTimes> businessTimes;
    private String cellphone;
    private String cnpj;
    private String color;
    private String description;
    private Location location;
    private String name;
    private List<String> paymentWays;
    private String phone;
    private List<String> pictures;

    public Store(){

    }

    public Store(String id, List<BusinessTimes> businessTimes, String cellphone, String cnpj, String color, String description, Location location, String name, List<String> paymentWays, String phone, List<String> pictures) {
        this.id = id;
        this.businessTimes = businessTimes;
        this.cellphone = cellphone;
        this.cnpj = cnpj;
        this.color = color;
        this.description = description;
        this.location = location;
        this.name = name;
        this.paymentWays = paymentWays;
        this.phone = phone;
        this.pictures = pictures;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<BusinessTimes> getBusinessTimes() {
        return businessTimes;
    }

    public void setBusinessTimes(List<BusinessTimes> businessTimes) {
        this.businessTimes = businessTimes;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeTypedList(this.businessTimes);
        dest.writeString(this.cellphone);
        dest.writeString(this.cnpj);
        dest.writeString(this.color);
        dest.writeString(this.description);
        dest.writeParcelable(this.location, flags);
        dest.writeString(this.name);
        dest.writeStringList(this.paymentWays);
        dest.writeString(this.phone);
        dest.writeStringList(this.pictures);
    }

    protected Store(Parcel in) {
        this.id = in.readString();
        this.businessTimes = in.createTypedArrayList(BusinessTimes.CREATOR);
        this.cellphone = in.readString();
        this.cnpj = in.readString();
        this.color = in.readString();
        this.description = in.readString();
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.name = in.readString();
        this.paymentWays = in.createStringArrayList();
        this.phone = in.readString();
        this.pictures = in.createStringArrayList();
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel source) {
            return new Store(source);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };
}
