package com.picone.core.domain.interactors.restaurantsInteractors.restaurant;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetAllPersistedRestaurantsInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public GetAllPersistedRestaurantsInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<List<Restaurant>> getAllPersistedRestaurants(){
        return restaurantDataSource.getAllPersistedRestaurants();
    }
}
