package com.picone.core.domain.interactors.restaurant.placeInteractors;

import android.location.Location;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.RestaurantDistancePOJO.RestaurantDistance;

import javax.inject.Inject;

import io.reactivex.Observable;

public class FetchRestaurantDistanceInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public FetchRestaurantDistanceInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<Restaurant> getRestaurantDistance_(Restaurant restaurant, Location currentLocation, String googleKey) {
        String currentLocationStr = String.valueOf(currentLocation.getLatitude()).concat(",").concat(String.valueOf(currentLocation.getLongitude()));
        String restaurantLocation = String.valueOf(restaurant.getRestaurantPosition().getLatitude()).concat(",").concat(String.valueOf(restaurant.getRestaurantPosition().getLongitude()));
        return restaurantDataSource.getRestaurantDistance(currentLocationStr, restaurantLocation, googleKey)
                .map(restaurantDistance -> restaurantDistance.getRows().get(0).getElements().get(0).getDistance().getText())
                .map(distance -> restaurantDistanceToRestaurantModel(restaurant, distance));
    }

    private Restaurant restaurantDistanceToRestaurantModel(Restaurant restaurant, String distance) {
        restaurant.setDistance(distance);
        return restaurant;
    }
}
