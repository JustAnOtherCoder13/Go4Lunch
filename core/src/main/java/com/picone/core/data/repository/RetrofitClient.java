package com.picone.core.data.repository;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient{

     public GooglePlaceService googlePlaceService(){
         Retrofit retrofit = new Retrofit.Builder()
                 .baseUrl("https://maps.googleapis.com/maps/")
                 .client(new OkHttpClient().newBuilder().build())
                 .addConverterFactory(GsonConverterFactory.create())
                 .build();
         return retrofit.create(GooglePlaceService.class);
     }
}
