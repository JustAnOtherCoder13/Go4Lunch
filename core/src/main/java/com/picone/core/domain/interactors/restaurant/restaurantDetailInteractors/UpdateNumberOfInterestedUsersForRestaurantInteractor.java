package com.picone.core.domain.interactors.restaurant.restaurantDetailInteractors;

import com.picone.core.data.repository.restaurant.RestaurantRepository;

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
