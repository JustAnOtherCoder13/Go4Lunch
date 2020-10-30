package com.picone.core.data.repository.services;

import com.google.gson.JsonObject;
import com.picone.core.BuildConfig;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationService {

    @Headers({"Authorization: key="+ BuildConfig.FCM_SERVER_KEY , "Content-Type: application/json"})
    @POST("fcm/send")
    Observable<JsonObject> sendNotification(@Body JsonObject payload);
}
