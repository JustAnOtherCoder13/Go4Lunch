package com.picone.core.domain.interactors.restaurantInteractors.restaurant;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import javax.inject.Inject;

public class DeleteDailyScheduleForRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public DeleteDailyScheduleForRestaurantInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public void deleteDailyScheduleForRestaurant(Restaurant selectedRestaurant){
        restaurantDataSource.deleteDailySchedule(selectedRestaurant);
    }
}
