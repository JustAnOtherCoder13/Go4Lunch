package com.picone.core.domain.entity.restaurant;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class RestaurantDailySchedule {

    private String date;
    private int numberOfInterestedUsers;

    public RestaurantDailySchedule() {
    }

    public RestaurantDailySchedule(String date, int numberOfInterestedUsers) {
        this.date = date;
        this.numberOfInterestedUsers = numberOfInterestedUsers;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumberOfInterestedUsers() {
        return numberOfInterestedUsers;
    }

    public void setNumberOfInterestedUsers(int numberOfInterestedUsers) {
        this.numberOfInterestedUsers = numberOfInterestedUsers;
    }
}
