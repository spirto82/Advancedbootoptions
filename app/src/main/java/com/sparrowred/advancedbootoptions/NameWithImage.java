package com.sparrowred.advancedbootoptions;

/**
 * Created by sparr on 10/1/2017.
 */

public class NameWithImage {
    private int imageID;
    private String info;

    public NameWithImage(int imageID, String info){
        this.imageID = imageID;
        this.info = info;
    }

    public void setImage(int picture){
        this.imageID = picture;
    }

    public void setInfo(String name){
        this.info = name;
    }

    public int getImageID(){
        return this.imageID;
    }

    public String getInfo(){
        return this.info;
    }
}
