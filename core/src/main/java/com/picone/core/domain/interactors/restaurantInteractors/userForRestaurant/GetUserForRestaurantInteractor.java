package com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetUserForRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public GetUserForRestaurantInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<User> getUserForRestaurant(Date today, Restaurant originalChosenRestaurant, User currentUser){
        return restaurantDataSource.getUserForRestaurant(today, originalChosenRestaurant, currentUser);
    }
}
