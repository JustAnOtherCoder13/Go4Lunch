package com.picone.core.data.repository;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.domain.entity.Restaurant;

import java.util.List;

import javax.inject.Inject;

public class RestaurantRepository {

    @Inject
    public RestaurantDao restaurantDao;
    @Inject
    public FirebaseDatabase dataBase;

    public RestaurantRepository(FirebaseDatabase dataBase,RestaurantDao dao) {
        this.dataBase = dataBase;
        restaurantDao = dao;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantDao.getAllRestaurants();
    }

    public Restaurant getRestaurant(int position) {
        return restaurantDao.getRestaurant(position);
    }
}
