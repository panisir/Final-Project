package com.example.asus.finalproject;

import android.content.Context;

/**
 * Created by ASUS on 25.04.2017.
 */

public class User {
    private String firstName;
    private String email;
    private String image;
    private String surName;

    public User(){

    }

    public User(String firstName, String email, String image, String surName) {
        this.firstName = firstName;
        this.email = email;
        this.image = image;
        this.surName = surName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(Context ctx, String image) {
        this.image = image;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }
}
