package com.picone.core.data.repository;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.domain.entity.DailySchedule;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.Date;
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

    public Observable<List<Restaurant>> getAllRestaurants() {
        return restaurantDao.getAllRestaurants();
    }

    public Observable<Restaurant> getRestaurant (String restaurantName){
        return restaurantDao.getRestaurant(restaurantName);
    }

    public Completable addRestaurant (Restaurant restaurant){
        return restaurantDao.addRestaurant(restaurant);
    }

    public Completable addDailySchedule(DailySchedule dailySchedule, Restaurant restaurant){
        return restaurantDao.addDailyScheduleToRestaurant(dailySchedule, restaurant);
    }
    public Observable<DailySchedule> getDailyScheduleForRestaurant(String restaurantName){
        return restaurantDao.getDailyScheduleForRestaurant(restaurantName);}

    public Observable<List<User>> getInterestedUsersForRestaurant(Date today, String restaurantName){
        return restaurantDao.getInterestedUsersForRestaurant(today, restaurantName);
    }

    public Completable updateInterestedUsers(Date today, String restaurantName, User user){
        return restaurantDao.updateInterestedUsersForRestaurant(today,restaurantName,user);}




}
