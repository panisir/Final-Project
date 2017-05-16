package com.example.asus.finalproject;

/**
 * Created by ASUS on 26.04.2017.
 */

public class Posts {
    private String description;
    private String image;
    private String title;
    private String uid;
    private String username;
    private String date;

    public Posts(){

    }

    public Posts(String description, String image, String title, String uid, String username, String date) {
        this.description = description;
        this.image = image;
        this.title = title;
        this.uid = uid;
        this.username = username;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
