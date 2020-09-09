package com.picone.core.domain.interactors.restaurantsInteractors.restaurant;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import javax.inject.Inject;

import io.reactivex.Completable;

public class AddRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public AddRestaurantInteractor(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public Completable addRestaurant(Restaurant restaurant){
        return restaurantDataSource.addRestaurant(restaurant);
    }
}
