package com.picone.core.domain.interactors.restaurantInteractors.dailySchedule;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.DailySchedule;

import javax.inject.Inject;

import io.reactivex.Completable;

public class AddDailyScheduleToRestaurantInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public AddDailyScheduleToRestaurantInteractor(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public Completable addDailyScheduleToRestaurant(DailySchedule dailySchedule, String selectedRestaurantName){
        return restaurantDataSource.addDailyScheduleToRestaurant(dailySchedule, selectedRestaurantName);

    }
}
