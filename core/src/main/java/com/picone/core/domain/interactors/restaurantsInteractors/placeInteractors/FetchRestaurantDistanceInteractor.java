package com.picone.core.domain.interactors.restaurantsInteractors.placeInteractors;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FetchRestaurantDistanceInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public FetchRestaurantDistanceInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    @SuppressLint("CheckResult")
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Observable<List<Restaurant>> getRestaurantDistance(List<Restaurant> allRestaurants, Location currentLocation, String googleKey) {
        String currentLocationStr = String.valueOf(currentLocation.getLatitude()).concat(",").concat(String.valueOf(currentLocation.getLongitude()));
        return Observable.create(emitter -> {
            for (Restaurant restaurant : allRestaurants) {
                String restaurantLocation = String.valueOf(restaurant.getRestaurantPosition().getLatitude()).concat(",").concat(String.valueOf(restaurant.getRestaurantPosition().getLongitude()));
                restaurantDataSource.getRestaurantDistance(currentLocationStr, restaurantLocation, googleKey)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(distance -> {
                            restaurant.setDistance(distance.getRows().get(0).getElements().get(0).getDistance().getText());
                            emitter.onNext(allRestaurants);
                        });
            }
        });

    }
}
