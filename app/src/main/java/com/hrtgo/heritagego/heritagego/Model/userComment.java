package com.hrtgo.heritagego.heritagego.Model;

public class userComment {

    String userName, content, postTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public userComment(String userName, String content, String postTime) {

        this.userName = userName;
        this.content = content;
        this.postTime = postTime;
    }
}
