package com.picone.core.domain.interactors.restaurantsInteractors;

import com.picone.core.data.repository.RestaurantRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetFanListForRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public GetFanListForRestaurantInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<List<String>> getFanListForRestaurant(String restaurantName) {
        return restaurantDataSource.getFanListForRestaurant(restaurantName);
    }

    }
