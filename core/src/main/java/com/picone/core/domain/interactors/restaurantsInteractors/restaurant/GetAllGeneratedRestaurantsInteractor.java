package com.picone.core.domain.interactors.restaurantsInteractors.restaurant;

import com.picone.core.domain.entity.Restaurant;

import java.util.List;


public class GetAllGeneratedRestaurantsInteractor {

    List<Restaurant> generatorRestaurant;

    public GetAllGeneratedRestaurantsInteractor( List<Restaurant> generatorRestaurant) {
        this.generatorRestaurant = generatorRestaurant;
    }

    public List<Restaurant> getAllGeneratedRestaurants() {
        return generatorRestaurant;
    }

}
