package com.picone.core.domain.interactors.restaurant.placeInteractors;

import android.location.Location;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.RestaurantPOJO.NearBySearch;
import com.picone.core.domain.entity.RestaurantPOJO.RestaurantPOJO;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class FetchRestaurantFromPlaceInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public FetchRestaurantFromPlaceInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<List<Restaurant>> fetchRestaurantFromPlace_(Location mCurrentLocation, String googleKey) {

        return restaurantDataSource
                .googlePlaceService(mCurrentLocation, googleKey)
                .map(NearBySearch::getRestaurantPOJOS)
                .map(this::restaurantsToRestaurantModel);

    }

    private List<Restaurant> restaurantsToRestaurantModel(List<RestaurantPOJO> restaurantsPojos) {

        return new ArrayList<Restaurant>();
    }
}
