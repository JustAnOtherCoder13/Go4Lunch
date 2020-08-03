package com.picone.core_.data.repository;

import android.content.Context;

import com.picone.core_.domain.entity.Restaurant;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ActivityContext;

public class RestaurantRepository {

    private final RestaurantDao restaurantDao;

    @Inject
    public RestaurantRepository(@ActivityContext Context context) {
        FirebaseDataBase firebaseDataBase = FirebaseDataBase.getInstance(context);
        restaurantDao = firebaseDataBase.restaurantDao();
    }

    public List<Restaurant> getAllRestaurants(){return restaurantDao.getAllRestaurants();}

    public Restaurant getRestaurant(int position){return restaurantDao.getRestaurant();}
}
