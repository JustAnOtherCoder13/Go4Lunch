package com.picone.core.domain.interactors.restaurantsInteractors.restaurant;

import com.picone.core.domain.entity.Restaurant;

import java.util.List;


public class GetGeneratedRestaurantInteractor {

    List<Restaurant> generatorRestaurant;


    public GetGeneratedRestaurantInteractor(List<Restaurant> generatorRestaurant) {
        this.generatorRestaurant = generatorRestaurant;
    }

    public Restaurant getGeneratedRestaurant(int position) {
        return generatorRestaurant.get(position);
    }
}
