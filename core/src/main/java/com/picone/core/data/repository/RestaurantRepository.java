package com.picone.core.data.repository;

import com.picone.core.domain.entity.Restaurant;

import java.util.List;

import javax.inject.Inject;

public class RestaurantRepository {

    private final RestaurantDao restaurantDao;
    @Inject
    public FirebaseDataBase firebaseDataBase;

    public RestaurantRepository() {
        restaurantDao = firebaseDataBase.restaurantDao();
    }

    public List<Restaurant> getAllRestaurants(){return restaurantDao.getAllRestaurants();}

    public Restaurant getRestaurant(int position){return restaurantDao.getRestaurant();}
}
