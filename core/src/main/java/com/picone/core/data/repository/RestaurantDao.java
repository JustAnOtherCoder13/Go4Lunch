package com.picone.core.data.repository;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface RestaurantDao {

    //--------------------------RESTAURANT---------------------------------
    Observable<Restaurant> getPersistedRestaurant(String restaurantName);

    Observable<List<Restaurant>> getAllPersistedRestaurants();

    Completable addRestaurant(Restaurant selectedRestaurant);

    Completable deleteRestaurant(String restaurantName);

    //------------------------------USER_FOR_RESTAURANT-----------------------------
    Observable<List<User>> getAllInterestedUsersForRestaurant(String selectedRestaurantName);

    Completable addCurrentUserToRestaurant(String selectedRestaurantName, User currentUser);

    Completable deleteCurrentUserFromRestaurant(String originalChosenRestaurantName, User currentUser);

}
