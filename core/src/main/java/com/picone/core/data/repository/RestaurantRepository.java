package com.picone.core.data.repository;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.entity.UserDailySchedule;

import java.util.List;

import javax.inject.Inject;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class RestaurantRepository {

    @Inject
    public RestaurantDaoImpl restaurantDao;
    @Inject
    public FirebaseDatabase dataBase;

    public RestaurantRepository(FirebaseDatabase dataBase, RestaurantDaoImpl dao) {
        this.dataBase = dataBase;
        restaurantDao = dao;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantDao.getAllRestaurants();
    }

    public Restaurant getRestaurant(int position) {
        return restaurantDao.getRestaurant(position);
    }

    public Observable<List<Restaurant>> getRestaurantForName(String restaurantName) {
        return restaurantDao.getRestaurantForName(restaurantName);
    }

    public Completable addRestaurant(Restaurant restaurant) {
        return restaurantDao.addRestaurant(restaurant);
    }

    public Completable updateUserChosenRestaurant(User currentUser) {
        return restaurantDao.updateUserChosenRestaurant(currentUser);
    }

    public Observable<List<Restaurant>> getRestaurantForKey(String restaurantKey) {
        return restaurantDao.getRestaurantForKey(restaurantKey);
    }
}
