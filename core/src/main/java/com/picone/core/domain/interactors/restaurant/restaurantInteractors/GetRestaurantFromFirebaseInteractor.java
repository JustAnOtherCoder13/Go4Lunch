package com.picone.core.domain.interactors.restaurant.restaurantInteractors;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.restaurant.Restaurant;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetRestaurantFromFirebaseInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public GetRestaurantFromFirebaseInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<Restaurant> getRestaurantFromFirebase(String restaurantPlaceId){
        return restaurantDataSource.getRestaurantFromFirebase(restaurantPlaceId);
    }
}