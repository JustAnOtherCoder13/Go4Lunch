package com.picone.core.domain.interactors;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import java.util.List;

import javax.inject.Inject;


public class GetAllRestaurantsInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;
    @Inject
    List<Restaurant> generatorRestaurant;

    public GetAllRestaurantsInteractor(RestaurantRepository restaurantDataSource,List<Restaurant> generatorRestaurant) {
        this.restaurantDataSource = restaurantDataSource;
        this.generatorRestaurant = generatorRestaurant;
    }

    public List<Restaurant> getAllRestaurants() {
        return generatorRestaurant;//restaurantDataSource.getAllRestaurants();
    }

    public Restaurant getRestaurant(int position){
        return generatorRestaurant.get(position); //restaurantDataSource.getRestaurant(position);
    }

}
