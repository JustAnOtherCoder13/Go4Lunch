package com.picone.core.data.repository;

import com.picone.core.domain.entity.retrofitRestaurant.NearBySearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlaceService {

    //TODO find a way to hide key
    //Observable <nearBySearch>
    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyBPfbZ_poV0QGgdifNxGzHHz2yS4L2evTI")
    Call<NearBySearch> getNearbySearch(
            @Query("type") String type,
            @Query("location") String location,
            @Query("radius") int radius);
}
