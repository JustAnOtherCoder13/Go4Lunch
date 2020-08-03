package com.picone.core.domain.interactors;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import java.util.List;

public class GetAllRestaurants {

    private RestaurantRepository restaurantDataSource;

    public GetAllRestaurants(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public List<Restaurant> getAllRestaurants(){return restaurantDataSource.getAllRestaurants();}

}
