package com.picone.core.data.repository;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.domain.entity.Restaurant;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class RestaurantRepository {

    @Inject
    public RestaurantDaoImpl restaurantDao;
    @Inject
    public FirebaseDatabase dataBase;

    public RestaurantRepository(FirebaseDatabase dataBase,RestaurantDaoImpl dao) {
        this.dataBase = dataBase;
        restaurantDao = dao;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantDao.getAllRestaurants();
    }

    public Restaurant getRestaurant(int position) {
        return restaurantDao.getRestaurant(position);
    }

    public Observable<Restaurant> getRestaurantForName(String restaurantName) {
        return restaurantDao.getRestaurantForName(restaurantName);
    }

    public Completable addRestaurant (Restaurant restaurant) {
        return restaurantDao.addRestaurant(restaurant);
    }
    }
