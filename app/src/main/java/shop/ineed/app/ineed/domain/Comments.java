package shop.ineed.app.ineed.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;


/**
 * Created by silva on 21/11/17.
 */

@IgnoreExtraProperties
public class Comments implements Parcelable {

    private String author;
    private String uidAuthor;
    private String authorPicture;
    private String body;
    private String date;
    private String title;
    private int rating;

    public Comments(){

    }

    public Comments(String author, String authorPicture, String body, String date, String title, int rating) {
        this.author = author;
        this.authorPicture = authorPicture;
        this.body = body;
        this.date = date;
        this.title = title;
        this.rating = rating;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUidAuthor() {
        return uidAuthor;
    }

    public void setUidAuthor(String uidAuthor) {
        this.uidAuthor = uidAuthor;
    }

    public String getAuthorPicture() {
        return authorPicture;
    }

    public void setAuthorPicture(String authorPicture) {
        this.authorPicture = authorPicture;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeString(this.uidAuthor);
        dest.writeString(this.authorPicture);
        dest.writeString(this.body);
        dest.writeString(this.date);
        dest.writeString(this.title);
        dest.writeInt(this.rating);
    }

    protected Comments(Parcel in) {
        this.author = in.readString();
        this.uidAuthor = in.readString();
        this.authorPicture = in.readString();
        this.body = in.readString();
        this.date = in.readString();
        this.title = in.readString();
        this.rating = in.readInt();
    }

    public static final Creator<Comments> CREATOR = new Creator<Comments>() {
        @Override
        public Comments createFromParcel(Parcel source) {
            return new Comments(source);
        }

        @Override
        public Comments[] newArray(int size) {
            return new Comments[size];
        }
    };
}
