package com.picone.core.data.repository;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient{

     private final String URL = "https://maps.googleapis.com/maps/";

     public GooglePlaceService googleMethods(){
         Retrofit retrofit = new Retrofit.Builder()
                 .baseUrl(URL)
                 .client(new OkHttpClient().newBuilder().build())
                 .addConverterFactory(GsonConverterFactory.create())
                 .build();
         return retrofit.create(GooglePlaceService.class);
     }
}
