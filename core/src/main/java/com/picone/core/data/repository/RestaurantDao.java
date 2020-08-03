package com.picone.core.data.repository;

import com.picone.core.domain.entity.Restaurant;

import java.util.List;

public interface RestaurantDao {

    List<Restaurant> getAllRestaurants();

    Restaurant getRestaurant();
}
