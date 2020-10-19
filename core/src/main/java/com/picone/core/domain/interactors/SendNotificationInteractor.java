package com.picone.core.domain.interactors;

import com.google.gson.JsonObject;
import com.picone.core.data.repository.notification.ApiClient;
import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.entity.notificationPOJO.NotificationToSend;
import com.picone.core.domain.entity.restaurant.Restaurant;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class SendNotificationInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public SendNotificationInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<JsonObject> sendNotification(String token, String restaurantName, String restaurantAddress, List<User> interestedUsers) {
        // compose notification json payload
        JsonObject payload = new JsonObject();
        payload.addProperty("to", token);

        // compose data payload here
        JsonObject data = new JsonObject();
        data.addProperty("title", "Today's Lunch");
        data.addProperty("message", createMessage(restaurantName, restaurantAddress, UserListToString(interestedUsers)));
        // add data payload
        payload.add("data", data);
        return restaurantDataSource.sendNotification(payload);
    }

    private String createMessage(String restaurantName, String restaurantAddress, String interestedUsers) {
        return ("You are eating in : " + restaurantName + " at : " + restaurantAddress + " with : " + interestedUsers);
    }

    private String UserListToString(List<User> interestedUsers) {
        String interestedUsersStr = null;
        for (User interestedUser : interestedUsers)
            if (interestedUsersStr == null)
                interestedUsersStr = interestedUser.getName();
            else
                interestedUsersStr = interestedUsersStr.concat(", ").concat(interestedUser.getName());
        return interestedUsersStr;
    }
}
