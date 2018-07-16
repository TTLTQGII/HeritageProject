package com.hrtgo.heritagego.heritagego.Model;

public class exhibitionInfoHome {

    String mExhibitionName, imgPath;

    public exhibitionInfoHome(String mExhibitionName, String imgPath) {
        this.mExhibitionName = mExhibitionName;
        this.imgPath = imgPath;
    }

    public String getExhibitionName() {
        return mExhibitionName;
    }

    public void setExhibitionName(String mExhibitionName) {
        this.mExhibitionName = mExhibitionName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
