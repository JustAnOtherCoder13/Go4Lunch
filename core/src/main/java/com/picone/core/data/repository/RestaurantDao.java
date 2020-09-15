package com.picone.core.data.repository;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.UserDailySchedule;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface RestaurantDao {

    List<Restaurant> getAllRestaurants();

    Restaurant getRestaurant(int position);

    Observable<List<Restaurant>> getRestaurantForName(String restaurantName);

    Observable<List<Restaurant>> getRestaurantForKey(String restaurantKey);

    Completable addRestaurant(Restaurant restaurant);

    Completable updateUserChosenRestaurant(String currentUserEmail, UserDailySchedule userDailySchedule);
}
