package com.picone.core.domain.interactors.restaurantsInteractors;

import com.picone.core.data.mocks.Generator;
import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import java.util.List;

import javax.inject.Inject;


public class GetRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    @Inject
    List<Restaurant> generatedRestaurants;


    public GetRestaurantInteractor(RestaurantRepository restaurantDataSource, List<Restaurant> generatedRestaurants) {
        this.restaurantDataSource = restaurantDataSource;
        this.generatedRestaurants = generatedRestaurants;
    }

    public Restaurant getRestaurant(int position) {
        return generatedRestaurants.get(position);
    }
}
