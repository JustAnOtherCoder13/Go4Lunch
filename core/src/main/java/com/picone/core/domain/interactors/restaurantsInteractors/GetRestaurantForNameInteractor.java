package com.picone.core.domain.interactors.restaurantsInteractors;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import java.util.List;

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
