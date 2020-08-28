package com.picone.core.domain.interactors.restaurantInteractors;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.User;

import javax.inject.Inject;

import io.reactivex.Completable;

public class AddUserInGlobalListInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public AddUserInGlobalListInteractor(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public Completable addUserInGlobalList(User currentUser){
        return restaurantDataSource.addInterestedUserInGlobalList(currentUser);
    }
}
