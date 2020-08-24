package com.picone.core.data.repository;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.List;

import javax.inject.Inject;

import durdinapps.rxfirebase2.DataSnapshotMapper;
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

    public Observable<List<Restaurant>> getAllRestaurants() {
        return restaurantDao.getAllRestaurants();
    }

    public Restaurant getRestaurant(int position) {
        return restaurantDao.getRestaurant(position);
    }

    public Completable updateInterestedColleague(User user) {
        return restaurantDao.updateInterestedColleague(user);
    }

    public Observable<List<User>> getInterestedColleague(Restaurant restaurant) {
        return restaurantDao.interestedColleague(restaurant);
    }
}
