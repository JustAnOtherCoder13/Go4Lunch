package com.picone.core.domain.interactors.restaurantInteractors;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.DailySchedule;
import com.picone.core.domain.entity.Restaurant;

import javax.inject.Inject;

import io.reactivex.Completable;

public class AddDailyScheduleInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public AddDailyScheduleInteractor (RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public Completable addDailySchedule (DailySchedule dailySchedule, Restaurant restaurant){
        return restaurantDataSource.addDailySchedule(dailySchedule, restaurant);

    }
}
