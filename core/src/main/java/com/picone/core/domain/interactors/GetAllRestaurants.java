package com.picone.core.domain.interactors;

import androidx.fragment.app.Fragment;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import java.util.List;

import javax.inject.Inject;


public class GetAllRestaurants extends Fragment {


    @Inject
    List<Restaurant> restaurants;

    public RestaurantRepository restaurantDataSource;


    public GetAllRestaurants(List<Restaurant> restaurants){
        this.restaurants = restaurants;
    }

    public List<Restaurant> getAllRestaurants(){return restaurants;}

}
