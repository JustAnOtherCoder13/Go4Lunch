package com.picone.core.data.repository.services;

import com.picone.core.domain.entity.predictionPOJO.PredictionResponse;
import com.picone.core.domain.entity.restaurantDetailPOJO.RestaurantDetail;
import com.picone.core.domain.entity.restaurantDistancePOJO.RestaurantDistance;
import com.picone.core.domain.entity.restaurantPOJO.NearBySearch;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlaceService {



    @GET("api/place/nearbysearch/json?sensor=true&type=restaurant&fields=photos,formatted_address,name,place_id,opening_hours,geometry")
    Observable<NearBySearch> getNearbySearch(
            @Query("location") String location,
            @Query("radius") String radius,
            @Query("key") String key
    );

    @GET("api/place/details/json?sensor=true&fields=formatted_phone_number,opening_hours,website")
    Observable<RestaurantDetail> getRestaurantDetail(
            @Query("place_id") String placeId,
            @Query("key") String key
    );

    @GET("api/distancematrix/json?sensor=true")
    Observable<RestaurantDistance> getRestaurantDistance(
            @Query("origins")String currentLocation,
            @Query("destinations") String restaurantLocation,
            @Query("key") String key
    );

    @GET("api/place/autocomplete/json?sensor=true&restaurant=address&strictbounds")
    Observable<PredictionResponse> loadPredictions(
            @Query("input") String restaurantName,
            @Query("key") String key,
            @Query("location") String currentLocation,
            @Query("radius") String radius
    );
}


