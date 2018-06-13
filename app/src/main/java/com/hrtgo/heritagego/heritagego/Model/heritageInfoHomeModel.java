package com.hrtgo.heritagego.heritagego.Model;

public class heritageInfoHomeModel {
    String locationName, imagePath;
    int ID, liked, viewed;

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

    public void setLocationImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getLocationLiked() {
        return liked;
    }

    public void setLocationLiked(int liked) {
        this.liked = liked;
    }

    public int getLocationViewed() {
        return viewed;
    }

    public void setLocationViewed(int viewed) {
        this.viewed = viewed;
    }

    public heritageInfoHomeModel(int ID, String locationName, int liked, int  viewed, String imagePath) {
        this.ID = ID;
        this.locationName = locationName;
        this.liked = liked;
        this.viewed = viewed;
        this.imagePath = imagePath;
    }
}
