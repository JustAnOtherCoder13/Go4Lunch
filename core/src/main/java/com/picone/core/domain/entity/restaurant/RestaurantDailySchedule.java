package com.picone.core.domain.entity.restaurant;

import com.google.firebase.database.IgnoreExtraProperties;
import com.picone.core.domain.entity.User;

import java.util.List;

@IgnoreExtraProperties

public class RestaurantDailySchedule {

    private String date;
    private List<User> interestedUsers;

    public RestaurantDailySchedule() {
    }

    public RestaurantDailySchedule(String date, List<User> interestedUsers) {
        this.date = date;
        this.interestedUsers = interestedUsers;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<User> getInterestedUsers() {
        return interestedUsers;
    }

    public void setInterestedUsers(List<User> interestedUsers) {
        this.interestedUsers = interestedUsers;
    }
}
