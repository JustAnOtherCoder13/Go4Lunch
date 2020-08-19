package com.picone.core.data.repository;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.domain.entity.Restaurant;

import java.util.List;

import javax.inject.Inject;

public class RestaurantDaoImpl implements RestaurantDao{

    @Inject
    FirebaseDatabase database;

    public RestaurantDaoImpl(FirebaseDatabase database){
        this.database = database;
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return null;
    }

    @Override
    public Restaurant getRestaurant(int position) {
        return null;
    }
}
