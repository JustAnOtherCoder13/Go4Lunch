package com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors;

import android.util.Log;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.restaurant.Restaurant;

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
