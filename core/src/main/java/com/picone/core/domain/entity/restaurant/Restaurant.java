package com.picone.core.domain.entity.restaurant;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
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
    private int numberOfInterestedUsers;
    private List<String> fanList;

    public Restaurant() {
    }

    public Restaurant(String name, String distance, String restaurantPhoto, RestaurantPosition restaurantPosition,
                      String address, String placeId, String openingHours,  String phoneNumber, String website,
                      double averageSatisfaction,  int numberOfInterestedUsers, List<String> fanList,List<RestaurantDailySchedule> restaurantDailySchedules) {
        this.name = name;
        this.distance = distance;
        this.restaurantPhoto = restaurantPhoto;
        this.address = address;
        this.openingHours = openingHours;
        this.averageSatisfaction = averageSatisfaction;
        this.restaurantPosition = restaurantPosition;
        this.numberOfInterestedUsers = numberOfInterestedUsers;
        this.fanList = fanList;
        this.website = website;
        this.phoneNumber = phoneNumber;
        this.placeId = placeId;
        this.restaurantDailySchedules = restaurantDailySchedules;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public String getDistance() {
        return distance;
    }

    public String getRestaurantPhoto() {
        return restaurantPhoto;
    }

    public String getAddress() { return address; }

    public String getOpeningHours() {
        return openingHours;
    }

    public double getAverageSatisfaction() {
        return averageSatisfaction;
    }

    public RestaurantPosition getRestaurantPosition() {
        return restaurantPosition;
    }

    public int getNumberOfInterestedUsers() {
        return numberOfInterestedUsers;
    }

    public void setNumberOfInterestedUsers(int numberOfInterestedUsers) {
        this.numberOfInterestedUsers = numberOfInterestedUsers;
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

