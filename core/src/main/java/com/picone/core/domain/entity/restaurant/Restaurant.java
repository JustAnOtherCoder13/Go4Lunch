package com.picone.core.domain.entity.restaurant;

import com.google.firebase.database.Exclude;

import java.util.List;


public class Restaurant {


    private String name;
    private String address;
    private RestaurantPosition restaurantPosition;
    private String placeId;
    private String restaurantPhoto;

    private String phoneNumber;
    private String website;
    private String openingHours;

    private String distance;

    private double averageSatisfaction;
    private List<RestaurantDailySchedule> restaurantDailySchedules;
    private List<String> fanList;

    public Restaurant() {
    }

    public Restaurant(String name, String distance, String restaurantPhoto, RestaurantPosition restaurantPosition,
                      String address, String placeId, String openingHours,  String phoneNumber, String website,
                      double averageSatisfaction, List<String> fanList,List<RestaurantDailySchedule> restaurantDailySchedules) {
        this.name = name;
        this.distance = distance;
        this.restaurantPhoto = restaurantPhoto;
        this.address = address;
        this.openingHours = openingHours;
        this.averageSatisfaction = averageSatisfaction;
        this.restaurantPosition = restaurantPosition;
        this.fanList = fanList;
        this.website = website;
        this.phoneNumber = phoneNumber;
        this.placeId = placeId;
        this.restaurantDailySchedules = restaurantDailySchedules;
    }



    @Exclude
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Exclude
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Exclude
    public String getName() {
        return name;
    }

    @Exclude
    public String getDistance() {
        return distance;
    }

    @Exclude
    public String getRestaurantPhoto() {
        return restaurantPhoto;
    }

    @Exclude
    public String getAddress() { return address; }

    @Exclude
    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public double getAverageSatisfaction() {
        return averageSatisfaction;
    }

    @Exclude
    public RestaurantPosition getRestaurantPosition() {
        return restaurantPosition;
    }

    public String getPlaceId() {
        return placeId;
    }

    public List<String> getFanList() {
        return fanList;
    }

    public void setFanList(List<String> fanList) {
        this.fanList = fanList;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<RestaurantDailySchedule> getRestaurantDailySchedules() {
        return restaurantDailySchedules;
    }

    public void setRestaurantDailySchedules(List<RestaurantDailySchedule> restaurantDailySchedules) {
        this.restaurantDailySchedules = restaurantDailySchedules;
    }
}

