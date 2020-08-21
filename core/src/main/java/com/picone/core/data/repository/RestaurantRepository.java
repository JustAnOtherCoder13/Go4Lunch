package com.picone.core.data.repository;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class RestaurantRepository {

    @Inject
    public RestaurantDao restaurantDao;
    @Inject
    public FirebaseDatabase dataBase;

    public RestaurantRepository(FirebaseDatabase dataBase, RestaurantDao dao) {
        this.dataBase = dataBase;
        restaurantDao = dao;
    }

    public Observable<List<Restaurant>> getAllRestaurants() {
        return restaurantDao.getAllRestaurants();
    }

    public Restaurant getRestaurant(int position) {
        return restaurantDao.getRestaurant(position);
    }

    public Completable addRestaurant(Restaurant restaurant) {
        return restaurantDao.AddRestaurant(restaurant);
    }

    public Completable updateInterestedColleague(User user) {
        return restaurantDao.updateInterestedColleague(user);
    }

    public Observable<List<User>> getInterestedColleague(Restaurant restaurant) {
        return restaurantDao.interestedColleague(restaurant);
    }
}
