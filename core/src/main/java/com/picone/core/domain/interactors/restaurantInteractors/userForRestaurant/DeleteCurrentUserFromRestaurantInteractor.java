package com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.User;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Completable;

public class DeleteCurrentUserFromRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public DeleteCurrentUserFromRestaurantInteractor(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public Completable deleteCurrentUserFromRestaurant(String originalChosenRestaurantName, User currentUser){
        return restaurantDataSource.deleteCurrentUserFromRestaurant(originalChosenRestaurantName, currentUser);
    }
}
