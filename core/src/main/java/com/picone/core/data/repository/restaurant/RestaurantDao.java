package com.picone.core.data.repository.restaurant;

import android.location.Location;

import com.google.gson.JsonObject;
import com.picone.core.domain.entity.predictionPOJO.PredictionResponse;
import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.entity.restaurantDetailPOJO.RestaurantDetail;
import com.picone.core.domain.entity.restaurantDistancePOJO.RestaurantDistance;
import com.picone.core.domain.entity.restaurantPOJO.NearBySearch;
import com.picone.core.domain.entity.user.User;

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

    Observable<JsonObject> sendNotification(JsonObject payload);
}