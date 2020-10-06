package com.picone.core.domain.interactors.restaurantsInteractors.placeInteractors;

import android.annotation.SuppressLint;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FetchRestaurantDetailFromPlaceInteractor {
    @Inject
    RestaurantRepository restaurantDataSource;

    public FetchRestaurantDetailFromPlaceInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void getRestaurantDetail(Restaurant restaurant, String googleKey) {
        restaurantDataSource.getPlaceRestaurantDetail(restaurant, googleKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restaurantDetail -> {
                    restaurant.setPhoneNumber(restaurantDetail.getResult().getFormattedPhoneNumber());
                    restaurant.setWebsite(restaurantDetail.getResult().getWebsite());
                    restaurant.setOpeningHours(restaurantDetail.getResult().getOpeningHours().getWeekdayText().get(getWeekDayTextValue()));
                });
    }


    private int getWeekDayTextValue() {
        int weekDayTextValue = Calendar.DAY_OF_WEEK + 1;
        if (weekDayTextValue == 7)
            weekDayTextValue = 0;
        if (weekDayTextValue == 8)
            weekDayTextValue = 1;
        return weekDayTextValue;
    }
}
