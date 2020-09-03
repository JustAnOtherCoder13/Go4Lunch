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
    RestaurantDaoImpl restaurantDao;
    @Inject
    FirebaseDatabase dataBase;

    public RestaurantRepository(FirebaseDatabase dataBase, RestaurantDaoImpl dao) {
        this.dataBase = dataBase;
        this.restaurantDao = dao;
    }

    //--------------------------------------RESTAURANT---------------------------------
    public Observable<Restaurant> getPersistedRestaurant(String restaurantName) {
        return restaurantDao.getPersistedRestaurant(restaurantName);
    }

    public Completable addRestaurant(Restaurant restaurant) {
        return restaurantDao.addRestaurant(restaurant);
    }

    //-----------------------------------------DAILY_SCHEDULE-------------------------------------------
    public Observable<DailySchedule> getDailyScheduleForRestaurant(String selectedRestaurantName) {
        return restaurantDao.getDailyScheduleForRestaurant(selectedRestaurantName);
    }

    public Completable addDailyScheduleToRestaurant(DailySchedule dailySchedule, String selectedRestaurantName) {
        return restaurantDao.addDailyScheduleToRestaurant(dailySchedule, selectedRestaurantName);
    }

    public Completable deleteDailyScheduleFromRestaurant(String  selectedRestaurantName) {
        return restaurantDao.deleteDailyScheduleFromRestaurant(selectedRestaurantName);
    }

//----------------------------------------------INTERESTED_USER_FOR_RESTAURANT---------------------------
    public Observable<List<User>> getAllInterestedUsersForRestaurant(Date today, String restaurantName) {
        return restaurantDao.getAllInterestedUsersForRestaurant(today, restaurantName);
    }

    public Completable addCurrentUserToRestaurant(Date today, String restaurantName, User currentUser) {
        return restaurantDao.addCurrentUserToRestaurant(today, restaurantName, currentUser);
    }

    public Completable deleteCurrentUserFromRestaurant(Date today, String originalChosenRestaurantName, User currentUser) {
        return restaurantDao.deleteCurrentUserFromRestaurant(today, originalChosenRestaurantName, currentUser);
    }

    //------------------------------------------GLOBAL_INTERESTED_USER-----------------------------------------
    public Observable<User> getGlobalCurrentUser(User currentUser) {
        return restaurantDao.getGlobalCurrentUser(currentUser);
    }

    public Observable<List<User>> getAllGlobalUsers(){
        return restaurantDao.getAllGlobalInterestedUsers();
    }

    public Completable addCurrentUserToGlobalList(User persistedCurrentUserWithRestaurantSet) {
        return restaurantDao.addCurrentUserToGlobalList(persistedCurrentUserWithRestaurantSet);
    }

    public Completable deleteUserFromGlobalList(User globalPersistedUser) {
        return restaurantDao.deleteUserFromGlobalList(globalPersistedUser);
    }
}