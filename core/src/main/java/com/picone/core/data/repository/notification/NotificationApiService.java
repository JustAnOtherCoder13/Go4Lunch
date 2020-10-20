package com.picone.core.data.repository.notification;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationApiService {

    @Headers({
            "Authorization: key=AAAAzIipKD4:APA91bGVoW6aLJpiM89dpRiKQ44PP-2YK8GbArcZ3nim5m-yCEcPo6HD97ln3x5VQum64JUaQU5sK3lq3kycpu8tOw_ElbqaKAzi_75YueGGpazJlA_XFTKyC0f5F31CzBPVYZtGKcfT",
            "Content-Type: application/json"
    })
    @POST("fcm/send")
    Observable<JsonObject> sendNotification(@Body JsonObject payload);
}
