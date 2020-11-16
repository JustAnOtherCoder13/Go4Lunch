package com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.interactors.restaurantInteractors.RestaurantBaseInteractor;

import java.util.List;

import io.reactivex.Observable;

public class GetAllPersistedRestaurantsInteractor extends RestaurantBaseInteractor {

    public GetAllPersistedRestaurantsInteractor(RestaurantRepository restaurantDataSource) {
        super(restaurantDataSource);
    }

    public Observable<List<Restaurant>> getAllPersistedRestaurants(){
        return restaurantDataSource.getAllPersistedRestaurants();
    }
}
