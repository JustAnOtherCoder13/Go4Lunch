package com.picone.core.domain.entity;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class RestaurantPosition {

    private double latitude;
    private double Longitude;

    public RestaurantPosition() {
    }

    public RestaurantPosition(double latitude, double longitude) {
        this.latitude = latitude;
        this.Longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
