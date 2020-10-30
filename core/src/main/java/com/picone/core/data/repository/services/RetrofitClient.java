package com.picone.core.data.repository.services;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public GooglePlaceService googlePlaceService() {
        return new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/")
                .client(new OkHttpClient().newBuilder().build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GooglePlaceService.class);
    }

    public NotificationService getNotificationService() {
        return new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/")
                .client(new OkHttpClient().newBuilder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(NotificationService.class);
    }
}