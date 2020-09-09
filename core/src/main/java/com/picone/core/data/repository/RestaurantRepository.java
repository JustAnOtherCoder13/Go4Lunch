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
    public RestaurantDaoImpl restaurantDao;
    @Inject
    public FirebaseDatabase dataBase;

    public RestaurantRepository(FirebaseDatabase dataBase, RestaurantDaoImpl dao) {
        this.dataBase = dataBase;
        this.restaurantDao = dao;
    }

    //--------------------------------------RESTAURANT---------------------------------
    public Observable<Restaurant> getPersistedRestaurant(String restaurantName) {
        return restaurantDao.getPersistedRestaurant(restaurantName);
    }

    public Observable<List<Restaurant>> getAllPersistedRestaurants() {
        return restaurantDao.getAllPersistedRestaurants();
    }

    public Completable addRestaurant(Restaurant restaurant) {
        return restaurantDao.addRestaurant(restaurant);
    }

    public Completable deleteRestaurant(String restaurantName) {
        return restaurantDao.deleteRestaurant(restaurantName);
    }

    //----------------------------------------------INTERESTED_USER_FOR_RESTAURANT---------------------------
    public Observable<List<User>> getAllInterestedUsersForRestaurant(String restaurantName) {
        return restaurantDao.getAllInterestedUsersForRestaurant( restaurantName);
    }

    public Completable addCurrentUserToRestaurant( String restaurantName, User currentUser) {
        return restaurantDao.addCurrentUserToRestaurant( restaurantName, currentUser);
    }

    public Completable deleteCurrentUserFromRestaurant( String originalChosenRestaurantName, User currentUser) {
        return restaurantDao.deleteCurrentUserFromRestaurant( originalChosenRestaurantName, currentUser);
    }
}
