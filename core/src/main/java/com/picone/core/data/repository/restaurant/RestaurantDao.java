package com.picone.core.data.repository.restaurant;

import android.location.Location;

import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.entity.RestaurantDetailPOJO.RestaurantDetail;
import com.picone.core.domain.entity.RestaurantDistancePOJO.RestaurantDistance;
import com.picone.core.domain.entity.RestaurantPOJO.NearBySearch;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.entity.predictionPOJO.PredictionResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface RestaurantDao {


    Completable addRestaurant(Restaurant restaurant);

    Completable updateUserChosenRestaurant(User currentUser);

    Observable<List<Restaurant>> getAllPersistedRestaurants();

    Observable<NearBySearch> getNearBySearch(Location mCurrentLocation, String googleKey);

    Observable<RestaurantDetail> getPlaceRestaurantDetail(Restaurant restaurant, String googleKey);

    Observable<RestaurantDistance> getRestaurantDistance(String currentLocation, String restaurantLocation, String googleKey);

    Observable<PredictionResponse> getPredictions(String restaurantName, String googleKey, String currentPosition);
}