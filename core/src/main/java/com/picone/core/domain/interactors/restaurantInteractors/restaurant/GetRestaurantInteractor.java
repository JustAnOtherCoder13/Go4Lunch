package com.picone.core.domain.interactors.restaurantInteractors.restaurant;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;


public class GetRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;
    @Inject
    List<Restaurant> generatorRestaurant;


    public GetRestaurantInteractor(RestaurantRepository restaurantDataSource, List<Restaurant> generatorRestaurant) {
        this.restaurantDataSource = restaurantDataSource;
        this.generatorRestaurant = generatorRestaurant;
    }

    public Restaurant getGeneratorRestaurant(int position) {
        return generatorRestaurant.get(position);
    }

    public Observable<Restaurant> getPersistedRestaurant(String restaurantName){
        return restaurantDataSource.getPersistedRestaurant(restaurantName);
    }
}
