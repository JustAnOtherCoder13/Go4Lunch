package com.picone.core.data.repository;

import com.picone.core.domain.entity.Restaurant;

import java.util.List;

import javax.inject.Inject;

public class RestaurantRepository {

    private final RestaurantDao restaurantDao;
    @Inject
    public DataBase dataBase;

    public RestaurantRepository(DataBase dataBase) {
        this.dataBase = dataBase;
        restaurantDao = dataBase.restaurantDao();
    }

    public List<Restaurant> getAllRestaurants(){return restaurantDao.getAllRestaurants();}

    public Restaurant getRestaurant(int position){return restaurantDao.getRestaurant();}
}
