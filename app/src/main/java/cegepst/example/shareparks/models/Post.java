package cegepst.example.shareparks.models;

import android.graphics.Bitmap;

public class Post {
    private Bitmap image;
    private String author;
    private String caption;
    private int nbrLikes;

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
}
