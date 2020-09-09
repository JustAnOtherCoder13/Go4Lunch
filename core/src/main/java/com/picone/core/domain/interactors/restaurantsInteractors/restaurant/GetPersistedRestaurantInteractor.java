package com.picone.core.domain.interactors.restaurantsInteractors.restaurant;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetPersistedRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public GetPersistedRestaurantInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<Restaurant> getPersistedRestaurant(String restaurantName){
        return restaurantDataSource.getPersistedRestaurant(restaurantName);
    }
}
