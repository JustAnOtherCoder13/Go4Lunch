package com.picone.core.domain.interactors;

import androidx.fragment.app.Fragment;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import java.util.List;


public class GetAllRestaurants extends Fragment {


    public RestaurantRepository restaurantDataSource;


    public GetAllRestaurants(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public List<Restaurant> getAllRestaurants(){return restaurantDataSource.getAllRestaurants();}

}
