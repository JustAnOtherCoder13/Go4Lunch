package com.picone.core.domain.interactors.restaurant.placeInteractors;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.predictionPOJO.PredictionResponse;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetPredictionInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public GetPredictionInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<PredictionResponse> getPredictions(String restaurantName, String googleKey, String currentPosition) {
    return restaurantDataSource.getPredictions(restaurantName, googleKey,currentPosition);
    }
    }
