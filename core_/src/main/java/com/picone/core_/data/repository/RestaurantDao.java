package com.picone.core_.data.repository;

import com.picone.core_.domain.entity.Restaurant;

import java.util.List;

public interface RestaurantDao {

    List<Restaurant> getAllRestaurants();

    Restaurant getRestaurant();
}
