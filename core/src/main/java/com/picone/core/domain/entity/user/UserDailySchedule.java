package com.picone.core.domain.entity.user;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserDailySchedule {

    private String date;
    private String restaurantPlaceId;
    private String restaurantName;

    public UserDailySchedule() {
    }

    public UserDailySchedule(String date, String restaurantPlaceId, String restaurantName) {
        this.date = date;
        this.restaurantPlaceId = restaurantPlaceId;
        this.restaurantName = restaurantName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRestaurantPlaceId() {
        return restaurantPlaceId;
    }

    public void setRestaurantPlaceId(String restaurantPlaceId) {
        this.restaurantPlaceId = restaurantPlaceId;
    }

    public String getRestaurantName() { return restaurantName; }

    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName;}
}
