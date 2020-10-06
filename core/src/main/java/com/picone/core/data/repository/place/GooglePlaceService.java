package com.picone.core.data.repository.place;

import com.picone.core.domain.entity.RestaurantDetailPOJO.RestaurantDetail;
import com.picone.core.domain.entity.RestaurantDistancePOJO.RestaurantDistance;
import com.picone.core.domain.entity.RestaurantPOJO.NearBySearch;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlaceService {

    //TODO find a way to hide key
    @GET("api/place/nearbysearch/json?sensor=true&radius=400&type=restaurant&fields=photos,formatted_address,name,place_id,opening_hours,geometry")
    Observable<NearBySearch> getNearbySearch(
            @Query("location") String location,
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
}


