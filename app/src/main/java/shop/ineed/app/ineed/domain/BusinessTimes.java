package shop.ineed.app.ineed.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by silva on 21/11/17.
 */
@IgnoreExtraProperties
public class BusinessTimes implements Parcelable {

    private String close;
    private String day;
    private String open;

    public BusinessTimes(){

    }

    public BusinessTimes(String close, String day, String open) {
        this.close = close;
        this.day = day;
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String opne) {
        this.open = opne;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.close);
        dest.writeString(this.day);
        dest.writeString(this.open);
    }

    protected BusinessTimes(Parcel in) {
        this.close = in.readString();
        this.day = in.readString();
        this.open = in.readString();
    }

    public static final Parcelable.Creator<BusinessTimes> CREATOR = new Parcelable.Creator<BusinessTimes>() {
        @Override
        public BusinessTimes createFromParcel(Parcel source) {
            return new BusinessTimes(source);
        }

        @Override
        public BusinessTimes[] newArray(int size) {
            return new BusinessTimes[size];
        }
    };
}
