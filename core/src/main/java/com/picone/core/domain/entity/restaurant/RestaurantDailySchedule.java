package com.picone.core.domain.entity.restaurant;

import com.picone.core.domain.entity.user.User;

import java.util.List;

public class RestaurantDailySchedule {

    private String date;
    private List<User> interestedUsers;

    @SuppressWarnings("unused")
    public RestaurantDailySchedule() {
    }

    public RestaurantDailySchedule(String date, List<User> interestedUsers) {
        this.date = date;
        this.interestedUsers = interestedUsers;
    }

    public String getDate() {
        return date;
    }

    public List<User> getInterestedUsers() {
        return interestedUsers;
    }

}
