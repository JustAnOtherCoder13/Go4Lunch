package com.picone.core.data.repository.restaurant;

import android.location.Location;

import com.google.gson.JsonObject;
import com.picone.core.domain.entity.pOJOprediction.PredictionResponse;
import com.picone.core.domain.entity.pOJOrestaurant.NearBySearch;
import com.picone.core.domain.entity.pOJOrestaurantDetail.RestaurantDetail;
import com.picone.core.domain.entity.pOJOrestaurantDistance.RestaurantDistance;
import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.entity.user.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class RestaurantRepository {

    @Inject
    protected RestaurantDaoImpl restaurantDao;

    public RestaurantRepository(RestaurantDaoImpl dao) {
        this.restaurantDao = dao;
    }

    public Completable addRestaurant(Restaurant restaurant) {
        return restaurantDao.addRestaurant(restaurant);
    }

    public Completable updateUserChosenRestaurant(User currentUser) {
        return restaurantDao.updateUserChosenRestaurant(currentUser);
    }

    public Observable<List<Restaurant>> getAllPersistedRestaurants() {
        return restaurantDao.getAllPersistedRestaurants();
    }

    public Observable<NearBySearch> getNearBySearch(Location mCurrentLocation, String googleKey) {
        return restaurantDao.getNearBySearch(mCurrentLocation, googleKey);
    }

    public Observable<RestaurantDetail> getPlaceRestaurantDetail(Restaurant restaurant, String googleKey) {
        return restaurantDao.getPlaceRestaurantDetail(restaurant, googleKey);
    }

    public Observable<RestaurantDistance> getRestaurantDistance(String currentLocation, String restaurantLocation, String googleKey) {
        return restaurantDao.getRestaurantDistance(currentLocation, restaurantLocation, googleKey);
    }

    public Observable<PredictionResponse> getPredictions(String restaurantName, String googleKey, String currentPosition) {
        return restaurantDao.getPredictions(restaurantName, googleKey, currentPosition);
    }

    public Observable<JsonObject> sendNotification(JsonObject payload) {
        return restaurantDao.sendNotification(payload);
    }
}