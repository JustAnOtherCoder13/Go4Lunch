package com.picone.core.domain.entity;

import java.util.List;

public class Restaurant {

    String name;
    int distance;
    String restaurantPhoto;
    String foodType;
    String address;
    List<User> interestedColleague;
    int openingHours;
    double averageSatisfaction;

    public Restaurant(String name, int distance, String restaurantPhoto, String foodType,
                      String address, List<User> interestedColleague, int openingHours,
                      double averageSatisfaction) {
        this.name = name;
        this.distance = distance;
        this.restaurantPhoto = restaurantPhoto;
        this.foodType = foodType;
        this.address = address;
        this.interestedColleague = interestedColleague;
        this.openingHours = openingHours;
        this.averageSatisfaction = averageSatisfaction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getRestaurantPhoto() {
        return restaurantPhoto;
    }

    public void setRestaurantPhoto(String restaurantPhoto) {
        this.restaurantPhoto = restaurantPhoto;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getAddress(){ return address;}

    public List<User> getInterestedColleague() {
        return interestedColleague;
    }

    public void setInterestedColleague(List<User> interestedColleague) {
        this.interestedColleague = interestedColleague;
    }

    public int getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(int openingHours) {
        this.openingHours = openingHours;
    }

    public double getAverageSatisfaction() {
        return averageSatisfaction;
    }

    public void setAverageSatisfaction(double averageSatisfaction) {
        this.averageSatisfaction = averageSatisfaction;
    }
}
