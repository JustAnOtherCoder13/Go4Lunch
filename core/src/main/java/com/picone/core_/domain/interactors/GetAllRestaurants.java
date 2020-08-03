package com.picone.core_.domain.interactors;

import androidx.fragment.app.Fragment;

import com.picone.core_.data.repository.RestaurantRepository;
import com.picone.core_.domain.entity.Restaurant;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GetAllRestaurants extends Fragment {

    @Inject
    public RestaurantRepository restaurantDataSource;

    @Inject
    public GetAllRestaurants(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public List<Restaurant> getAllRestaurants(){return restaurantDataSource.getAllRestaurants();}

}
