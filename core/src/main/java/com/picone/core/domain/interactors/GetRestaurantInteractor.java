package com.picone.core.domain.interactors;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import javax.inject.Inject;


public class GetRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;


    public GetRestaurantInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Restaurant getRestaurant(int position) {
        return restaurantDataSource.getRestaurant(position);
    }
}
