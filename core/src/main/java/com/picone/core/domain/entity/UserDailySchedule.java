package com.picone.core.domain.entity;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserDailySchedule {

    private String date;
    private String restaurantKey;
    private String restaurantName;

    public UserDailySchedule() {
    }

    public UserDailySchedule(String date, String restaurantKey,String restaurantName) {
        this.date = date;
        this.restaurantKey = restaurantKey;
        this.restaurantName = restaurantName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRestaurantKey() {
        return restaurantKey;
    }

    public void setRestaurantKey(String restaurantKey) {
        this.restaurantKey = restaurantKey;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}
