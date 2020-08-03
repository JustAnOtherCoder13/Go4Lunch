package com.picone.core_.domain.interactors;

import com.picone.core_.data.repository.RestaurantRepository;
import com.picone.core_.domain.entity.Restaurant;

public class GetRestaurant {
    private RestaurantRepository restaurantDataSource;

    public GetRestaurant(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public Restaurant getRestaurant(int position){return restaurantDataSource.getRestaurant(position);}
}
