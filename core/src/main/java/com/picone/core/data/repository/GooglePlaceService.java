package com.picone.core.data.repository;

import com.picone.core.domain.entity.retrofitRestaurant.NearBySearch;
import com.picone.core.domain.entity.RetrofitRestaurantDetail.RestaurantDetail;
import com.picone.core.domain.entity.retrofitRestaurantDistance.Distance;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlaceService {

    //TODO find a way to hide key
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyBPfbZ_poV0QGgdifNxGzHHz2yS4L2evTI")
    Call<NearBySearch> getNearbySearch(
            @Query("type") String type,
            @Query("location") String location,
            @Query("radius") int radius);

    @GET("api/place/details/json?sensor=true&key=AIzaSyBPfbZ_poV0QGgdifNxGzHHz2yS4L2evTI&fields=formatted_phone_number,opening_hours,website")
    Call<RestaurantDetail> getRestaurantDetail(
            @Query("place_id") String placeId
    );

    @GET("api/distancematrix/json?sensor=true&key=AIzaSyBPfbZ_poV0QGgdifNxGzHHz2yS4L2evTI")
    Call<Distance> getRestaurantDistance(
            @Query("origins")String currentLocation,
            @Query("destinations") String restaurantLocation
    );
}


