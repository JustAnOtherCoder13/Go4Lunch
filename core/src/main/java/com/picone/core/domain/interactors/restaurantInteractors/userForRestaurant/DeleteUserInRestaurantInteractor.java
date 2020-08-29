package com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.Date;

import javax.inject.Inject;

public class DeleteUserInRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public DeleteUserInRestaurantInteractor(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public void deleteUserForRestaurant(Date today, Restaurant originalChosenRestaurant, User currentUser){
        restaurantDataSource.deleteUserInRestaurant(today, originalChosenRestaurant, currentUser);
    }
}
