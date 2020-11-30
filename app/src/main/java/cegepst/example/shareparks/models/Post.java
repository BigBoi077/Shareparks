package cegepst.example.shareparks.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {

    private Bitmap image;
    private String author;
    private String caption;
    private int nbrLikes;

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public Post() {
    }

    protected Post(Parcel in) {
        image = in.readParcelable(Bitmap.class.getClassLoader());
        author = in.readString();
        caption = in.readString();
        nbrLikes = in.readInt();
    }

    public Bitmap getImage() {
        return image;
    }

    public String getAuthor() {
        return author;
    }

    public String getCaption() {
        return caption;
    }

    public int getNbrLikes() {
        return nbrLikes;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setNbrLikes(int nbrLikes) {
        this.nbrLikes = nbrLikes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(image, i);
        parcel.writeString(author);
        parcel.writeString(caption);
        parcel.writeInt(nbrLikes);
    }
}
