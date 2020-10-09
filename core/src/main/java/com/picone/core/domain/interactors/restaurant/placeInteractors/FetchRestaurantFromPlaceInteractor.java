package com.picone.core.domain.interactors.restaurant.placeInteractors;

import android.location.Location;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.RestaurantPOJO.NearBySearch;

import javax.inject.Inject;

import io.reactivex.Observable;

public class FetchRestaurantFromPlaceInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public FetchRestaurantFromPlaceInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<NearBySearch> fetchRestaurantFromPlace_(Location mCurrentLocation, String googleKey) {
        return restaurantDataSource.googlePlaceService(mCurrentLocation, googleKey);
    }
}
