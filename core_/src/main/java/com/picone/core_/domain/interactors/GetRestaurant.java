package com.picone.core_.domain.interactors;

import androidx.fragment.app.Fragment;

import com.picone.core_.data.repository.RestaurantRepository;
import com.picone.core_.domain.entity.Restaurant;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GetRestaurant extends Fragment {
    @Inject
    public RestaurantRepository restaurantDataSource;

    @Inject
    public GetRestaurant(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public Restaurant getRestaurant(int position){return restaurantDataSource.getRestaurant(position);}
}
