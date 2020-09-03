package com.picone.core.data.repository;

import com.picone.core.domain.entity.DailySchedule;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface RestaurantDao {

    //--------------------------RESTAURANT---------------------------------
    Observable<Restaurant> getPersistedRestaurant(String restaurantName);

    Completable addRestaurant(Restaurant selectedRestaurant);

    //------------------------------DAILY_SCHEDULE--------------------------------
    Observable<DailySchedule> getDailyScheduleForRestaurant(String selectedRestaurantName);

    Completable addDailyScheduleToRestaurant(DailySchedule dailySchedule, String selectedRestaurantName);

    Completable deleteDailyScheduleFromRestaurant(String selectedRestaurantName);

    //------------------------------USER_FOR_RESTAURANT-----------------------------
    Observable<List<User>> getAllInterestedUsersForRestaurant(Date today, String selectedRestaurantName);

    Completable addCurrentUserToRestaurant(Date today, String selectedRestaurantName, User currentUser);

    Completable deleteCurrentUserFromRestaurant(Date today, String originalChosenRestaurantName, User currentUser);

    //------------------------------GLOBAL_USER_FOR_RESTAURANT------------------------
    Observable<User> getGlobalCurrentUser(User currentUser);

    Observable<List<User>> getAllGlobalInterestedUsers();

    Completable addCurrentUserToGlobalList(User persistedInterestedUserWithRestaurantSet);

    Completable deleteUserFromGlobalList(User globalPersistedUser);

}
