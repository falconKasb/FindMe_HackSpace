package sk.com.findme.Classes;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import java.net.URL;


/**
 * Created by sanchirkartiev on 21/03/15.
 */
public class People {
    private Bitmap picture;
    private String name;

    public People()
    {
        picture = null;
        name = "";
    }
    public People(String name)
    {
        this.picture = null;
        this.name = name;
    }
    public People(Bitmap picture, String name)
    {
        this.picture = picture;
        this.name = name;
    }
    public void setPicture(Bitmap picture)
    {
        this.picture = picture;
    }
    public void setName(String firstName)
    {
        this.name = firstName;
    }
    public Bitmap getPicture()
    {
        return picture;
    }
    public String getName() { return name; }

}
