package com.picone.core.domain.interactors.restaurantInteractors;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import java.util.List;

import javax.inject.Inject;


public class GetRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;
    @Inject
    List<Restaurant> generatorRestaurant;


    public GetRestaurantInteractor(RestaurantRepository restaurantDataSource, List<Restaurant> generatorRestaurant) {
        this.restaurantDataSource = restaurantDataSource;
        this.generatorRestaurant = generatorRestaurant;
    }

    public Restaurant getRestaurant(int position) {
        return generatorRestaurant.get(position); //restaurantDataSource.getRestaurant(position);
    }
}
