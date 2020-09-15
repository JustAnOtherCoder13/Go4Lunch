package com.picone.core.domain.entity;

import com.google.firebase.database.IgnoreExtraProperties;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@IgnoreExtraProperties
public class RestaurantDailySchedule {



    private String date;

    private List<User> interestedUsers;

    public RestaurantDailySchedule() throws ParseException {
    }

    public RestaurantDailySchedule(String date, List<User> interestedUser) {
        this.date = date;
        this.interestedUsers = interestedUser;
    }

    public List<User> getInterestedUsers() {
        return interestedUsers;
    }

    public void addUser(User interestedUser) {
        this.interestedUsers.add(interestedUser);
    }

    public void deleteUser(User uninterestedUser) {
        this.interestedUsers.remove(uninterestedUser);
    }

}