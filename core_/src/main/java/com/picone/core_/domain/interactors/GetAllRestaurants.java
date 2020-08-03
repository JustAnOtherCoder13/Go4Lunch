package com.picone.core_.domain.interactors;

import com.picone.core_.data.repository.RestaurantRepository;
import com.picone.core_.domain.entity.Restaurant;

import java.util.List;

public class GetAllRestaurants {

    private RestaurantRepository restaurantDataSource;

    public GetAllRestaurants(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public List<Restaurant> getAllRestaurants(){return restaurantDataSource.getAllRestaurants();}

}
