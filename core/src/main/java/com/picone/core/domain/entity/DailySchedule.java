package com.picone.core.domain.entity;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.List;

@IgnoreExtraProperties
public class DailySchedule {

    private String today;
    private List<User> interestedUser;

    public DailySchedule(){}

    public DailySchedule(String today, List<User> interestedUser) {
        this.today = today;
        this.interestedUser = interestedUser;
    }

    public String getToday() {
        return today;
    }

    public List<User> getInterestedUsers() {
        return interestedUser;
    }
}
