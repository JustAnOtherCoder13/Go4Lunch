package com.picone.core.data.repository;

import android.content.Context;

import com.picone.core.domain.entity.Restaurant;

import java.util.List;

public class RestaurantRepository {

    private final RestaurantDao restaurantDao;

    public RestaurantRepository(Context context) {
        FirebaseDataBase firebaseDataBase = FirebaseDataBase.getInstance(context);
        restaurantDao = firebaseDataBase.restaurantDao();
    }

    public List<Restaurant> getAllRestaurants(){return restaurantDao.getAllRestaurants();}

    public Restaurant getRestaurant(int position){return restaurantDao.getRestaurant();}
}
