package com.picone.core.domain.entity;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Restaurant {

    //TODO add like manager
    //TODO add phone number, reorganize?

    private String key;
    private String name;
    private int distance;
    private String restaurantPhoto;
    private String foodType;
    private String address;
    private int openingHours;
    private double averageSatisfaction;
    private RestaurantPosition restaurantPosition;
    private int numberOfInterestedUsers;

    public Restaurant() {
    }

    public Restaurant(String key, String name, int distance, String restaurantPhoto, String foodType,
                      String address, int openingHours,
                      double averageSatisfaction, RestaurantPosition restaurantPosition, int numberOfInterestedUsers) {
        this.key = key;
        this.name = name;
        this.distance = distance;
        this.restaurantPhoto = restaurantPhoto;
        this.foodType = foodType;
        this.address = address;
        this.openingHours = openingHours;
        this.averageSatisfaction = averageSatisfaction;
        this.restaurantPosition = restaurantPosition;
        this.numberOfInterestedUsers = numberOfInterestedUsers;
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

    public String getAddress() { return address; }

    public int getOpeningHours() {
        return openingHours;
    }

    public double getAverageSatisfaction() {
        return averageSatisfaction;
    }

    public String getKey() { return key; }

    public void setKey(String key) {
        this.key = key;
    }

    public RestaurantPosition getRestaurantPosition() {
        return restaurantPosition;
    }

    public void setRestaurantPosition(RestaurantPosition restaurantPosition) {
        this.restaurantPosition = restaurantPosition;
    }

    public int getNumberOfInterestedUsers() {
        return numberOfInterestedUsers;
    }

    public void setNumberOfInterestedUsers(int numberOfInterestedUsers) {
        this.numberOfInterestedUsers = numberOfInterestedUsers;
    }
}
