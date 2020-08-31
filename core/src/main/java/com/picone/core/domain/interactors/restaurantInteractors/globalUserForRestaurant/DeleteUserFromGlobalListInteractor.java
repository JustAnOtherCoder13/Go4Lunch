package com.picone.core.domain.interactors.restaurantInteractors.globalUserForRestaurant;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.User;

import javax.inject.Inject;

public class DeleteUserFromGlobalListInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public DeleteUserFromGlobalListInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public void deleteUserFromGlobalList(User globalPersistedUser){
        restaurantDataSource.deleteUserFromGlobalList(globalPersistedUser);
    }
}
