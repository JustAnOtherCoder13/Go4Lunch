package com.picone.core.domain.interactors;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import java.util.List;

import javax.inject.Inject;


public class GetAllRestaurantsInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public GetAllRestaurantsInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantDataSource.getAllRestaurants();
    }

    public Restaurant getRestaurant(int position){
        return restaurantDataSource.getRestaurant(position);
    }

}
