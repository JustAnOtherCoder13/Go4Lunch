package com.picone.core.domain.interactors.restaurantsInteractors;

import com.picone.core.data.repository.RestaurantRepository;

import javax.inject.Inject;

import io.reactivex.Completable;

public class DeleteUserFromRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public DeleteUserFromRestaurantInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Completable deleteUserFromRestaurant(String currentUserName, String originalChosenRestaurant){
        return restaurantDataSource.deleteCurrentUserFromRestaurant(currentUserName, originalChosenRestaurant);
    }
}
