package shop.ineed.app.ineed.domain;

import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by jose on 10/12/17.
 *
 * Class aux Store
 *
 * Domain class
 */
@IgnoreExtraProperties
public class Location implements Parcelable {
    private String address;
    private Double lat;
    private Double lng;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeValue(this.lat);
        dest.writeValue(this.lng);
    }

    public Location() {
    }

    protected Location(android.os.Parcel in) {
        this.address = in.readString();
        this.lat = (Double) in.readValue(Double.class.getClassLoader());
        this.lng = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(android.os.Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
