package com.picone.core.data.repository;

import com.picone.core.domain.entity.DailySchedule;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface RestaurantDao {

    Observable<List<Restaurant>> getAllRestaurants();

    Observable<Restaurant> getRestaurant(String restaurantName);

    Completable addRestaurant (Restaurant restaurant);

    Observable<DailySchedule> getDailyScheduleForRestaurant(String restaurantName);

    Completable addDailyScheduleToRestaurant(DailySchedule dailySchedule,Restaurant restaurant);

    void deleteDailyScheduleForRestaurant(Restaurant selectedRestaurant);

    Observable<User> getUserForRestaurant(Date today, Restaurant originalChosenRestaurant,User currentUser);

    Observable<List<User>> getInterestedUsersForRestaurant(Date today,String restaurantName );

    Completable updateInterestedUsersForRestaurant(Date today, String restaurantName, User user);

    void deleteUserInRestaurant(Date today, Restaurant originalChosenRestaurant, User currentUser);

    Observable<User> getGlobalInterestedUser(User user);

    Completable addInterestedUserInGlobalList(User user);

}
