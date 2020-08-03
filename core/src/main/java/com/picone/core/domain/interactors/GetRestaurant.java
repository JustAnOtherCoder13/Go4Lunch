package com.picone.core.domain.interactors;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

public class GetRestaurant {
    private RestaurantRepository restaurantDataSource;

    public GetRestaurant(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public Restaurant getRestaurant(int position){return restaurantDataSource.getRestaurant(position);}
}
