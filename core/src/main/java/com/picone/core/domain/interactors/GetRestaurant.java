package com.picone.core.domain.interactors;

import androidx.fragment.app.Fragment;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import java.util.List;

import javax.inject.Inject;


public class GetRestaurant extends Fragment {

    public RestaurantRepository restaurantDataSource;

    @Inject
    List<Restaurant> restaurants;

    public GetRestaurant(List<Restaurant> restaurants){
        this.restaurants = restaurants;
    }

    public Restaurant getRestaurant(int position){return restaurants.get(position);}
}
