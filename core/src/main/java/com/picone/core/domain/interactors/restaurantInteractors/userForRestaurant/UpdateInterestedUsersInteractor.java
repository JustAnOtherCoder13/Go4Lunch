package com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.User;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Completable;

public class UpdateInterestedUsersInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public UpdateInterestedUsersInteractor(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public Completable updateInterestedUser (Date today, String restaurantName, User interestedUser){
        return restaurantDataSource.updateInterestedUsers(today,restaurantName,interestedUser);
    }
}
