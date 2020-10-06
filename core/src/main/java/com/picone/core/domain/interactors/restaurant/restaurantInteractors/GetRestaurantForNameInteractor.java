package com.picone.core.domain.interactors.restaurant.restaurantInteractors;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetRestaurantForNameInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public GetRestaurantForNameInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<Restaurant> getRestaurantForName(String restaurantName){
        return restaurantDataSource.getRestaurantForName(restaurantName);
    }
}
