package com.picone.core.domain.interactors.restaurantInteractors.globalUserForRestaurant;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.User;

import javax.inject.Inject;

import io.reactivex.Completable;

public class AddCurrentUserToGlobalListInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public AddCurrentUserToGlobalListInteractor(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public Completable addCurrentUserToGlobalList(User currentUser){
        return restaurantDataSource.addCurrentUserToGlobalList(currentUser);
    }
}
