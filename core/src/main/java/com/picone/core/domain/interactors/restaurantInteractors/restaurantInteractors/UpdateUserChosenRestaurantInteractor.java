package com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.user.User;

import javax.inject.Inject;

import io.reactivex.Completable;

public class UpdateUserChosenRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public UpdateUserChosenRestaurantInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Completable updateUserChosenRestaurant(User currentUser){
        return restaurantDataSource.updateUserChosenRestaurant(currentUser);
    }
}
