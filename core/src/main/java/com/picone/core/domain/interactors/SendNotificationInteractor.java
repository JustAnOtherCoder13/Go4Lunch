package com.picone.core.domain.interactors;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.notificationPOJO.NotificationToSend;

import javax.inject.Inject;

import io.reactivex.Observable;

public class SendNotificationInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public SendNotificationInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<NotificationToSend> sendNotification(String token) {
        return restaurantDataSource.sendNotification(token);
    }
}
