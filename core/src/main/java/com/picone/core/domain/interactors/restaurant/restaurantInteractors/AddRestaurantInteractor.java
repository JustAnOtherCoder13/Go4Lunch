package com.picone.core.domain.interactors.restaurant.restaurantInteractors;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.restaurant.Restaurant;

import javax.inject.Inject;

import io.reactivex.Completable;

public class AddRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public AddRestaurantInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Completable addRestaurant(Restaurant restaurant){
        return restaurantDataSource.addRestaurant(restaurant);
    }
}
