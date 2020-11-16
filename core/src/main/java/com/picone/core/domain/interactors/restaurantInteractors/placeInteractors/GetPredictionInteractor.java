package com.picone.core.domain.interactors.restaurantInteractors.placeInteractors;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.pOJOprediction.PredictionResponse;
import com.picone.core.domain.interactors.restaurantInteractors.RestaurantBaseInteractor;

import io.reactivex.Observable;

public class GetPredictionInteractor extends RestaurantBaseInteractor {

    public GetPredictionInteractor(RestaurantRepository restaurantDataSource) {
        super(restaurantDataSource);
    }

    public Observable<PredictionResponse> getPredictions(String restaurantName, String googleKey, String currentPosition) {
        return restaurantDataSource.getPredictions(restaurantName, googleKey, currentPosition);
    }
}
