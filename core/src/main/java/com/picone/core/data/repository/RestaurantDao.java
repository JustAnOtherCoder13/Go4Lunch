package com.picone.core.data.repository;

import com.picone.core.domain.entity.Restaurant;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface RestaurantDao {

    List<Restaurant> getAllRestaurants();

    Restaurant getRestaurant(int position);

    Observable<List<Restaurant>> getRestaurantForName(String restaurantName);

    Completable addRestaurant (Restaurant restaurant);
}
