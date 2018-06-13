package com.hrtgo.heritagego.heritagego.Model;

import java.util.ArrayList;

public class heritageLocationDetail {

    String Name, timeOfBuild, Address, Contents, Description, Destination;
    int liked, viewed, amountOfComment;

    ArrayList<String> imagePath;

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public int getAmountOfComment() {
        return amountOfComment;
    }

    public void setAmountOfComment(int amountOfComment) {
        this.amountOfComment = amountOfComment;
    }

    public heritageLocationDetail(String name, String timeOfBuild, String address, String contents, String description, int liked, int viewed, int comment, ArrayList<String> imagePath, String destination) {
        Name = name;
        this.timeOfBuild = timeOfBuild;
        Address = address;
        Contents = contents;
        Description = description;
        this.liked = liked;
        this.viewed = viewed;
        this.amountOfComment = comment;
        this.imagePath = imagePath;
        this.Destination = destination;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTimeOfBuild() {
        return timeOfBuild;
    }

    public void setTimeOfBuild(String timeOfBuild) {
        this.timeOfBuild = timeOfBuild;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getContents() {
        return Contents;
    }

    public void setContents(String contents) {
        Contents = contents;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public ArrayList<String> getImagePath() {
        return imagePath;
    }

    public void setImagePath(ArrayList<String> imagePath) {
        this.imagePath = imagePath;
    }
}
