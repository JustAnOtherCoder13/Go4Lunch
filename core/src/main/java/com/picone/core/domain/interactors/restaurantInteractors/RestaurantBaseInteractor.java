package com.picone.core.domain.interactors.restaurantInteractors;

import com.picone.core.data.repository.restaurant.RestaurantRepository;

import javax.inject.Inject;

public class RestaurantBaseInteractor {
    @Inject
    protected RestaurantRepository restaurantDataSource;

    public RestaurantBaseInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }
}
