package com.picone.core.domain.entity;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    private String uid;
    private String name;
    private String email;
    private String avatar;
    private UserDailySchedule userDailySchedule;


    public User() {
    }

    public User(String uid, String name, String email, String avatar, UserDailySchedule userDailySchedule) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.userDailySchedule = userDailySchedule;
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

    public UserDailySchedule getUserDailySchedule() {
        return userDailySchedule;
    }

    public void setUserDailySchedule(UserDailySchedule userDailySchedule) {
        this.userDailySchedule = userDailySchedule;
    }

    public void setUid(String uid) { this.uid = uid;}
}
