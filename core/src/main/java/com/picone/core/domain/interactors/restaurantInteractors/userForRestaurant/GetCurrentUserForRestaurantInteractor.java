package com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetCurrentUserForRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public GetCurrentUserForRestaurantInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<User> getCurrentUserForRestaurant(Date today, String selectedRestaurantName, User currentUser){
        return restaurantDataSource.getCurrentUserForRestaurant(today, selectedRestaurantName, currentUser);
    }
}
