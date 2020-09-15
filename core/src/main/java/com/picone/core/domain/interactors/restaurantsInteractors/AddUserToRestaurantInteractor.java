package com.picone.core.domain.interactors.restaurantsInteractors;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.User;

import javax.inject.Inject;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;

public class AddUserToRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public AddUserToRestaurantInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Completable addCurrentUserToRestaurant(User currentUser, String restaurantName){
        return  restaurantDataSource.addCurrentUserToRestaurant(currentUser, restaurantName);
    }

}
