package com.picone.core.domain.entity;

import com.google.firebase.database.IgnoreExtraProperties;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@IgnoreExtraProperties
public class DailySchedule {

    private final static Calendar CALENDAR = Calendar.getInstance();
    private final static int MY_DAY_OF_MONTH = CALENDAR.get(Calendar.DAY_OF_MONTH);
    private final static int MY_MONTH = CALENDAR.get(Calendar.MONTH);
    private final static int MY_YEAR = CALENDAR.get(Calendar.YEAR);

    private String date;
    private Date today;

    private List<User> interestedUsers;

    public DailySchedule() {
    }

    public DailySchedule(List<User> interestedUser) {
        try {
            today = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(MY_DAY_OF_MONTH + "/" + MY_MONTH + "/" + MY_YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.date = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(today);
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

    public String getDate() { return date; }
}