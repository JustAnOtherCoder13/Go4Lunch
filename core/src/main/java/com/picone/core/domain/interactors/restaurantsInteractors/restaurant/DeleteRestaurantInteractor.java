package com.picone.core.domain.interactors.restaurantsInteractors.restaurant;

import com.picone.core.data.repository.RestaurantRepository;

import javax.inject.Inject;

import io.reactivex.Completable;

public class DeleteRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public DeleteRestaurantInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Completable deleteRestaurant(String restaurantName){
        return restaurantDataSource.deleteRestaurant(restaurantName);
    }
}
