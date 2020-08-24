package com.picone.core.data.repository;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface RestaurantDao {

    Observable<List<Restaurant>> getAllRestaurants();

    Restaurant getRestaurant(int position);

    Observable<List<User>> interestedColleague(Restaurant restaurant);

    Completable updateInterestedColleague(User user);
}
