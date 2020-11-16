package com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.interactors.restaurantInteractors.RestaurantBaseInteractor;

import io.reactivex.Completable;

public class AddRestaurantInteractor extends RestaurantBaseInteractor {

    public AddRestaurantInteractor(RestaurantRepository restaurantDataSource) {
        super(restaurantDataSource);
    }

    public Completable addRestaurant(Restaurant restaurant){
        return restaurantDataSource.addRestaurant(restaurant);
    }
}
