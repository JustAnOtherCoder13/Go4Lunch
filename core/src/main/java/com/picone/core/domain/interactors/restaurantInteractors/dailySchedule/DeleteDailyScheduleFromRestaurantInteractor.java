package com.picone.core.domain.interactors.restaurantInteractors.dailySchedule;

import com.picone.core.data.repository.RestaurantRepository;

import javax.inject.Inject;

public class DeleteDailyScheduleFromRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public DeleteDailyScheduleFromRestaurantInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public void deleteDailyScheduleFromRestaurant(String selectedRestaurantName){
        restaurantDataSource.deleteDailyScheduleFromRestaurant(selectedRestaurantName);
    }
}
