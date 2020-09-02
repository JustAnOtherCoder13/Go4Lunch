package com.picone.core.domain.interactors.restaurantInteractors.globalUserForRestaurant;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.User;

import javax.inject.Inject;

import io.reactivex.Completable;

public class DeleteUserFromGlobalListInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public DeleteUserFromGlobalListInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Completable deleteUserFromGlobalList(User globalPersistedUser) {
        return restaurantDataSource.deleteUserFromGlobalList(globalPersistedUser);
    }
}
