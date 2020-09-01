package com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.User;

import java.util.Date;

import javax.inject.Inject;

public class DeleteCurrentUserFromRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public DeleteCurrentUserFromRestaurantInteractor(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public void deleteCurrentUserFromRestaurant(Date today, String originalChosenRestaurantName, User currentUser){
        restaurantDataSource.deleteCurrentUserFromRestaurant(today, originalChosenRestaurantName, currentUser);
    }
}
