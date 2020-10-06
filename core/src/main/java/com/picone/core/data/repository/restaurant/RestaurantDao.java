package com.picone.core.data.repository.restaurant;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface RestaurantDao {

    Observable<Restaurant> getRestaurantForName(String restaurantName);

    Observable<List<Restaurant>> getRestaurantForKey(String restaurantKey);

    Completable addRestaurant(Restaurant restaurant);

    Completable updateUserChosenRestaurant(User currentUser);

    Completable updateNumberOfInterestedUsersForRestaurant(String restaurantName, int numberOfInterestedUsers);

    Observable<List<Restaurant>> getAllPersistedRestaurants();
}