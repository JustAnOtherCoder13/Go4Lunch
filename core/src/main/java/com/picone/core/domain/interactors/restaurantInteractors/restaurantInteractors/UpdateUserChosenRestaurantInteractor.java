package com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.user.User;
import com.picone.core.domain.interactors.restaurantInteractors.RestaurantBaseInteractor;

import io.reactivex.Completable;

public class UpdateUserChosenRestaurantInteractor extends RestaurantBaseInteractor {

    public UpdateUserChosenRestaurantInteractor(RestaurantRepository restaurantDataSource) {
        super(restaurantDataSource);
    }

    public Completable updateUserChosenRestaurant(User currentUser){
        return restaurantDataSource.updateUserChosenRestaurant(currentUser);
    }
}
