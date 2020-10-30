package com.picone.core.domain.entity.restaurant;

public class RestaurantPosition {

    private double latitude;
    private double Longitude;

    @SuppressWarnings("unused")
    public RestaurantPosition() {
    }

    public RestaurantPosition(double latitude, double longitude) {
        this.latitude = latitude;
        this.Longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return Longitude;
    }
}
