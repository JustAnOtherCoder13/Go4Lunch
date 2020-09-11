package com.picone.core.domain.entity;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserDailySchedule {

    String date;
    String restaurantKey;

    public UserDailySchedule() {
    }

    public UserDailySchedule(String date, String restaurantKey) {
        this.date = date;
        this.restaurantKey = restaurantKey;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) { this.date = date; }

    public String getRestaurantKey() {
        return restaurantKey;
    }

    public void setRestaurantKey(String restaurantKey) { this.restaurantKey = restaurantKey; }
}
