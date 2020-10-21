package com.picone.core.domain.interactors;

import com.google.gson.JsonObject;
import com.picone.core.data.repository.restaurant.RestaurantRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

public class SendNotificationInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public SendNotificationInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<JsonObject> sendNotification(String token, String message) {
        // compose notification json payload
        JsonObject payload = new JsonObject();
        payload.addProperty("to", token);

        // compose data payload here
        JsonObject data = new JsonObject();
        data.addProperty("title", "Today's Lunch");
        data.addProperty("message", message);
        // add data payload
        payload.add("data", data);
        return restaurantDataSource.sendNotification(payload);
    }


}
