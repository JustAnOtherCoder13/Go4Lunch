package com.picone.core.data.repository;

import android.location.Location;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.entity.retrofitRestaurant.NearBySearch;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class RestaurantRepository {

    @Inject
    protected RestaurantDaoImpl restaurantDao;
    @Inject
    protected FirebaseDatabase dataBase;

    public RestaurantRepository(FirebaseDatabase dataBase, RestaurantDaoImpl dao) {
        this.dataBase = dataBase;
        this.restaurantDao = dao;
    }

    public Observable<Restaurant> getRestaurantForName(String restaurantName) {
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
    public Completable updateNumberOfInterestedUsersForRestaurant(String restaurantName, int numberOfInterestedUsers){
        return restaurantDao.updateNumberOfInterestedUsersForRestaurant(restaurantName, numberOfInterestedUsers);
    }
    public Observable<List<Restaurant>> getAllPersistedRestaurants (){
        return restaurantDao.getAllPersistedRestaurants();
    }

    public Observable<NearBySearch> googlePlaceService(Location mCurrentLocation) {
        return restaurantDao.googlePlaceService(mCurrentLocation);
    }
    public Completable updateFanListForRestaurant(String restaurantName,List<String> fanList) {
        return restaurantDao.updateFanListForRestaurant(restaurantName, fanList);
    }

    public Observable<List<String>> getFanListForRestaurant(String restaurantName) {
        return restaurantDao.getFanListForRestaurant(restaurantName);
    }
}
