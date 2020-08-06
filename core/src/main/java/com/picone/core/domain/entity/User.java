package com.picone.core.domain.entity;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String Name;
    public Restaurant selectedRestaurant;

    public User() {    }

    public User(String name, Restaurant selectedRestaurant) {
        this.Name = name;
        this.selectedRestaurant = selectedRestaurant;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Restaurant getSelectedRestaurant() {
        return selectedRestaurant;
    }

    public void setSelectedRestaurant(Restaurant selectedRestaurant) {
        this.selectedRestaurant = selectedRestaurant;
    }
}
