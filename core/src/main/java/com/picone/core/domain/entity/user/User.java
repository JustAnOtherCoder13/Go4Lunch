package com.picone.core.domain.entity.user;

import java.util.List;

public class User {

    private String uid;
    private String name;
    private String email;
    private String avatar;
    private List<UserDailySchedule> userDailySchedules;
    private SettingValues settingValues;

    public User() {
    }

    public User(String uid, String name, String email, String avatar, List<UserDailySchedule> userDailySchedules,SettingValues settingValues) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.userDailySchedules = userDailySchedules;
        this.settingValues = settingValues;
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

    public SettingValues getSettingValues() {
        return settingValues;
    }

    public void setSettingValues(SettingValues settingValues) {
        this.settingValues = settingValues;
    }
}
