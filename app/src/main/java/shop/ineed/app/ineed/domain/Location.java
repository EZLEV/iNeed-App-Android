package shop.ineed.app.ineed.domain;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by jose on 10/12/17.
 *
 * Class aux Store
 *
 * Domain class
 */
@org.parceler.Parcel
@IgnoreExtraProperties
public class Location {
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
}
