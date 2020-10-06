package com.picone.core.data.repository;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient{

     public GooglePlaceService googlePlaceService(){
         Retrofit retrofit = new Retrofit.Builder()
                 .baseUrl("https://maps.googleapis.com/maps/")
                 .client(new OkHttpClient().newBuilder().build())
                 .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                 .addConverterFactory(GsonConverterFactory.create())
                 .build();
         return retrofit.create(GooglePlaceService.class);
     }
}
