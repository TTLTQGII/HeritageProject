package com.hrtgo.heritagego.heritagego.Model;

public class heritageInfoHomeModel {
    String ID, locationName, imagePath, viewed;
    int liked;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
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

    public String getLocationViewed() {
        return viewed;
    }

    public void setLocationViewed(String viewed) {
        this.viewed = viewed;
    }

    public heritageInfoHomeModel(String ID, String locationName, int liked, String  viewed) {
        this.ID = ID;
        this.locationName = locationName;
        this.liked = liked;

        this.viewed = viewed;
        //this.imagePath = imagePath;
    }
}
