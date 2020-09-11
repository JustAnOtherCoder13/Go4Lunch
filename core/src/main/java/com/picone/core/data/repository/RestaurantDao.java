package com.picone.core.data.repository;

import com.picone.core.domain.entity.Restaurant;

import java.util.List;

import io.reactivex.Observable;

public interface RestaurantDao {

    Observable<List<Restaurant>> getAllRestaurants();

    Observable<List<Restaurant>> getRestaurant(String restaurantKey);
}
