package com.picone.core.domain.interactors.restaurantInteractors.dailySchedule;

import com.picone.core.data.repository.RestaurantRepository;

import javax.inject.Inject;

import io.reactivex.Completable;

public class DeleteDailyScheduleFromRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public DeleteDailyScheduleFromRestaurantInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Completable deleteDailyScheduleFromRestaurant(String selectedRestaurantName){
        return restaurantDataSource.deleteDailyScheduleFromRestaurant(selectedRestaurantName);
    }
}
