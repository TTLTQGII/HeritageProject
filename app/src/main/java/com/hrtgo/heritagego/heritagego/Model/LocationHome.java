package com.hrtgo.heritagego.heritagego.Model;

import java.util.Comparator;

public class LocationHome{

    private int locationImage;
    private String locationViewed, locationName;

    public int getLocationImage() {
        return locationImage;
    }

    public void setLocationImage(int locationImage) {
        this.locationImage = locationImage;
    }

    public String getLocationViewed() {
        return locationViewed;
    }

    public void setLocationViewed(String locationViewed) {
        this.locationViewed = locationViewed;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public LocationHome(int locationImage, String locationViewed, String locationName) {

        this.locationImage = locationImage;
        this.locationViewed = locationViewed;
        this.locationName = locationName;
    }

}
