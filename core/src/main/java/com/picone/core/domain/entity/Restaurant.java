package com.picone.core.domain.entity;

public class Restaurant {

    String name;
    int distance;
    String restaurantPhoto;
    String foodType;
    int interestedColleague;
    int openingHours;
    int averageSatisfaction;

    public Restaurant(String name, int distance, String restaurantPhoto, String foodType, int interestedColleague, int openingHours, int averageSatisfaction) {
        this.name = name;
        this.distance = distance;
        this.restaurantPhoto = restaurantPhoto;
        this.foodType = foodType;
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

    public int getInterestedColleague() {
        return interestedColleague;
    }

    public void setInterestedColleague(int interestedColleague) {
        this.interestedColleague = interestedColleague;
    }

    public int getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(int openingHours) {
        this.openingHours = openingHours;
    }

    public int getAverageSatisfaction() {
        return averageSatisfaction;
    }

    public void setAverageSatisfaction(int averageSatisfaction) {
        this.averageSatisfaction = averageSatisfaction;
    }
}
