package com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.User;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetAllInterestedUsersForRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public GetAllInterestedUsersForRestaurantInteractor(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<List<User>> getAllInterestedUsersForRestaurant(Date today, String restaurantName){
        return restaurantDataSource.getAllInterestedUsersForRestaurant(today,restaurantName);
    }
}
