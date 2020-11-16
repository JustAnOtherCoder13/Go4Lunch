package com.picone.core.domain.interactors;

import com.google.gson.JsonObject;
import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.interactors.restaurantInteractors.RestaurantBaseInteractor;

import io.reactivex.Observable;

public class SendNotificationInteractor extends RestaurantBaseInteractor {

    public SendNotificationInteractor(RestaurantRepository restaurantDataSource) {
        super(restaurantDataSource);
    }

    public Observable<JsonObject> sendNotification(String token, String title, String message) {
        JsonObject payload = new JsonObject();
        payload.addProperty("to", token);
        JsonObject data = new JsonObject();
        data.addProperty("title", title);
        data.addProperty("message", message);
        payload.add("data", data);
        return restaurantDataSource.sendNotification(payload);
    }
}
