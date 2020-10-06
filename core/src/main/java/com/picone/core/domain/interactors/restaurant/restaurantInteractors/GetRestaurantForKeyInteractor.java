package com.picone.core.domain.interactors.restaurant.restaurantInteractors;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetRestaurantForKeyInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public GetRestaurantForKeyInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<List<Restaurant>> getRestaurantForKey(String restaurantKey) {
        return restaurantDataSource.getRestaurantForKey(restaurantKey);
    }
}
