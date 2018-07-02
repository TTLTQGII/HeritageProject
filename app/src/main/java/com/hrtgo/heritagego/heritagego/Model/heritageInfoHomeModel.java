package com.hrtgo.heritagego.heritagego.Model;

import android.util.Log;

public class heritageInfoHomeModel {

    String locationName, imagePath;
    int ID;
    long  liked, viewed;

    public int getID() {
        return ID;
    }

    public void setID(int    ID) {
        this.ID = ID;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }


    public String getLocationImagePath() {
        return imagePath;
    }

    //        String original1 = imagePath;
//        String original2 = imagePath;
//        String temp1 = original1.substring(0, 14);
//        String temp2 = original2.substring(14);
//        Log.e("temp2", temp2);
//        String url = temp1 + "hergo" + "/" + temp2;
//        Log.e("imagePatch", url);
//        return  url;

    public void setLocationImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public long getLocationLiked() {
        return liked;
    }

    public void setLocationLiked(long liked) {
        this.liked = liked;
    }

    public long getLocationViewed() {
        return viewed;
    }

    public void setLocationViewed(int viewed) {
        this.viewed = viewed;
    }

    public heritageInfoHomeModel(int ID, String locationName, long liked, long  viewed, String imagePath) {
        this.ID = ID;
        this.locationName = locationName;
        this.liked = liked;
        this.viewed = viewed;
        this.imagePath = imagePath;
    }
}
