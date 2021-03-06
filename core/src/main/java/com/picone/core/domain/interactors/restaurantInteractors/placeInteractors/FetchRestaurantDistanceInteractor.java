package com.picone.core.domain.interactors.restaurantInteractors.placeInteractors;

import android.location.Location;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.interactors.restaurantInteractors.RestaurantBaseInteractor;

import io.reactivex.Observable;

public class FetchRestaurantDistanceInteractor extends RestaurantBaseInteractor {

    public FetchRestaurantDistanceInteractor(RestaurantRepository restaurantDataSource) {
        super(restaurantDataSource);
    }

    public Observable<Restaurant> getRestaurantDistance(Restaurant restaurant, Location currentLocation, String googleKey) {
        String currentLocationStr = String.valueOf(currentLocation.getLatitude()).concat(",").concat(String.valueOf(currentLocation.getLongitude()));
        String restaurantLocation = String.valueOf(restaurant.getRestaurantPosition().getLatitude()).concat(",").concat(String.valueOf(restaurant.getRestaurantPosition().getLongitude()));
        return restaurantDataSource.getRestaurantDistance(currentLocationStr, restaurantLocation, googleKey)
                .map(restaurantDistance -> restaurantDistance.getRows().get(0).getElements().get(0).getDistance().getText())
                .map(distance -> restaurantDistanceToRestaurantModel(restaurant, distance));
    }

    private Restaurant restaurantDistanceToRestaurantModel(Restaurant restaurant, String distance) {

        double distanceNumber = Double.parseDouble(distance.substring(0,3));
        String distanceToPass;
        if (distanceNumber < 1) distanceToPass = String.valueOf(distanceNumber * 1000).substring(0,3).concat(" m");
        else distanceToPass = String.valueOf(distanceNumber).concat(" km");
        restaurant.setDistance(distanceToPass);
        return restaurant;

    }
}
