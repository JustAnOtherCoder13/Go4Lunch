package com.picone.core.domain.interactors.restaurant.placeInteractors;

import android.location.Location;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.RestaurantDistancePOJO.RestaurantDistance;

import javax.inject.Inject;

import io.reactivex.Observable;

public class FetchRestaurantDistanceInteractor {

    //TODO subscribe in ViewModel

    @Inject
    RestaurantRepository restaurantDataSource;

    public FetchRestaurantDistanceInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<RestaurantDistance> getRestaurantDistance_(Restaurant restaurant, Location currentLocation, String googleKey) {
        String currentLocationStr = String.valueOf(currentLocation.getLatitude()).concat(",").concat(String.valueOf(currentLocation.getLongitude()));
        String restaurantLocation = String.valueOf(restaurant.getRestaurantPosition().getLatitude()).concat(",").concat(String.valueOf(restaurant.getRestaurantPosition().getLongitude()));
        return restaurantDataSource.getRestaurantDistance(currentLocationStr, restaurantLocation, googleKey);
    }
}
