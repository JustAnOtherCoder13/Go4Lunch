package com.picone.core.domain.interactors.restaurantsInteractors;

import android.location.Location;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GooglePlaceInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public GooglePlaceInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<List<Restaurant>> googleMethods(Location mCurrentLocation) {
        return restaurantDataSource.googleMethods(mCurrentLocation);
        //return list restaurant. map observable NearBySearch en list Restaurant
    }
}
