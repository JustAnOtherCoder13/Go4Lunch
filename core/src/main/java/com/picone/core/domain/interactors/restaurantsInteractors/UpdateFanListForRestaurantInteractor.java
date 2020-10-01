package com.picone.core.domain.interactors.restaurantsInteractors;

import com.picone.core.data.repository.RestaurantRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class UpdateFanListForRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public UpdateFanListForRestaurantInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Completable updateFanListForRestaurant(String restaurantName, List<String> fanList) {
        return restaurantDataSource.updateFanListForRestaurant(restaurantName, fanList);
    }

    }
