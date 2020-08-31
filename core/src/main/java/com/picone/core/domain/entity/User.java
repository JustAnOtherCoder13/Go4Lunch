package com.picone.core.domain.entity;

import com.google.firebase.database.IgnoreExtraProperties;

import java.security.Key;

@IgnoreExtraProperties
public class User {

    private String uid;
    private String name;
    private String email;
    private String avatar;
    private Restaurant selectedRestaurant;

    public User() {
    }

    public User(String uid, String name, String email, String avatar,Restaurant selectedRestaurant) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.selectedRestaurant = selectedRestaurant;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public Restaurant getSelectedRestaurant(){return selectedRestaurant;}

    public void setSelectedRestaurant(Restaurant selectedRestaurant) {
        this.selectedRestaurant = selectedRestaurant;
    }
}
