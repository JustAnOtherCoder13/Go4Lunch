package com.picone.core.domain.entity.user;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class User {

    private String uid;
    private String name;
    private String email;
    private String avatar;
    private List<UserDailySchedule> userDailySchedules;


    public User() {
    }

    public User(String uid, String name, String email, String avatar, List<UserDailySchedule> userDailySchedules) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.userDailySchedules = userDailySchedules;
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

    public List<UserDailySchedule> getUserDailySchedules() {
        return userDailySchedules;
    }

    public void setUserDailySchedules(List<UserDailySchedule> userDailySchedules) {
        this.userDailySchedules = userDailySchedules;
    }

    public void setUid(String uid) { this.uid = uid;}
}
