package com.picone.core.domain.interactors;

import androidx.fragment.app.Fragment;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;


public class GetRestaurant extends Fragment {

    public RestaurantRepository restaurantDataSource;


    public GetRestaurant(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public Restaurant getRestaurant(int position){return restaurantDataSource.getRestaurant(position);}
}
