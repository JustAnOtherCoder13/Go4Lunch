package com.picone.core.domain.interactors.restaurant.placeInteractors;

import android.annotation.SuppressLint;
import android.location.Location;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FetchRestaurantDistanceInteractor {

    //TODO subscribe in ViewModel

    @Inject
    RestaurantRepository restaurantDataSource;

    public FetchRestaurantDistanceInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void getRestaurantDistance(Restaurant restaurant, Location currentLocation, String googleKey) {
        String currentLocationStr = String.valueOf(currentLocation.getLatitude()).concat(",").concat(String.valueOf(currentLocation.getLongitude()));
        String restaurantLocation = String.valueOf(restaurant.getRestaurantPosition().getLatitude()).concat(",").concat(String.valueOf(restaurant.getRestaurantPosition().getLongitude()));
        restaurantDataSource.getRestaurantDistance(currentLocationStr, restaurantLocation, googleKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(distance ->
                        restaurant.setDistance(distance.getRows().get(0).getElements().get(0).getDistance().getText()));
    }
}