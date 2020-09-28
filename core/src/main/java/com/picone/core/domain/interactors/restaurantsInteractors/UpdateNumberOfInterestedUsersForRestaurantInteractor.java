package com.picone.core.domain.interactors.restaurantsInteractors;

import com.picone.core.data.repository.RestaurantRepository;

import javax.inject.Inject;

import io.reactivex.Completable;

public class UpdateNumberOfInterestedUsersForRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public UpdateNumberOfInterestedUsersForRestaurantInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Completable updateNumberOfInterestedUsersForRestaurant(String restaurantName, int numberOfInterestedUsers){
        return restaurantDataSource.updateNumberOfInterestedUsersForRestaurant(restaurantName, numberOfInterestedUsers);
    }
}
