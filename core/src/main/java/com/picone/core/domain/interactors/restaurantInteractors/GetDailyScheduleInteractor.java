package com.picone.core.domain.interactors.restaurantInteractors;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.DailySchedule;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetDailyScheduleInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public GetDailyScheduleInteractor(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<DailySchedule> getDailyScheduleForRestaurant(String restaurantName){
        return restaurantDataSource.getDailyScheduleForRestaurant(restaurantName);
    }
}
