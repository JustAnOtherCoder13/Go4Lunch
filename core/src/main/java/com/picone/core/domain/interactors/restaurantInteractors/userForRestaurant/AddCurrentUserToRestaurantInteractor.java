package com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.User;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Completable;

public class AddCurrentUserToRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public AddCurrentUserToRestaurantInteractor(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public Completable addCurrentUserToRestaurant(Date today, String restaurantName, User interestedUser){
        return restaurantDataSource.addCurrentUserToRestaurant(today,restaurantName,interestedUser);
    }
}
