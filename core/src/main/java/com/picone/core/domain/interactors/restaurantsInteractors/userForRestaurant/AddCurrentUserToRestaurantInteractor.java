package com.picone.core.domain.interactors.restaurantsInteractors.userForRestaurant;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.User;

import javax.inject.Inject;

import io.reactivex.Completable;

public class AddCurrentUserToRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public AddCurrentUserToRestaurantInteractor(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public Completable addCurrentUserToRestaurant(String restaurantName, User interestedUser){
        return restaurantDataSource.addCurrentUserToRestaurant(restaurantName,interestedUser);
    }
}