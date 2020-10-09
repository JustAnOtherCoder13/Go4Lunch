package com.picone.core.domain.interactors.restaurant.placeInteractors;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.RestaurantDetailPOJO.RestaurantDetail;

import javax.inject.Inject;

import io.reactivex.Observable;

public class FetchRestaurantDetailFromPlaceInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public FetchRestaurantDetailFromPlaceInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<RestaurantDetail> getRestaurantDetail_(Restaurant restaurant, String googleKey) {
        return restaurantDataSource.getPlaceRestaurantDetail(restaurant, googleKey);
    }
}
