package com.picone.core.domain.entity;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class Restaurant {

    private String name;
    private int distance;
    private String restaurantPhoto;
    private String foodType;
    private String address;
    private int openingHours;
    private double averageSatisfaction;
    private List<DailySchedule> dailySchedules;

    public Restaurant(){}

    public Restaurant(String name, int distance, String restaurantPhoto, String foodType,
                      String address, int openingHours,
                      double averageSatisfaction,List<DailySchedule> dailySchedules) {
        this.name = name;
        this.distance = distance;
        this.restaurantPhoto = restaurantPhoto;
        this.foodType = foodType;
        this.address = address;
        this.openingHours = openingHours;
        this.averageSatisfaction = averageSatisfaction;
        this.dailySchedules = dailySchedules;
    }

    public String getName() {
        return name;
    }

    public int getDistance() {
        return distance;
    }

    public String getRestaurantPhoto() {
        return restaurantPhoto;
    }

    public String getFoodType() {
        return foodType;
    }

    public String getAddress(){ return address;}

    public int getOpeningHours() {
        return openingHours;
    }

    public double getAverageSatisfaction() {
        return averageSatisfaction;
    }

    public List<DailySchedule> getDailySchedules() {
        return dailySchedules;
    }
}
