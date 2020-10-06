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
    public Observable<List<Restaurant>> getRestaurantDetail(List<Restaurant> allRestaurants, String googleKey) {
        return
                Observable.create(emitter -> {
                    for (Restaurant rest : allRestaurants) {
                        restaurantDataSource.getPlaceRestaurantDetail(rest, googleKey)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(restaurantDetail -> {
                                    rest.setPhoneNumber(restaurantDetail.getResult().getFormattedPhoneNumber());
                                    rest.setWebsite(restaurantDetail.getResult().getWebsite());
                                    rest.setOpeningHours(restaurantDetail.getResult().getOpeningHours().getWeekdayText().get(getWeekDayTextValue()));
                                });

                    }
                    emitter.onNext(allRestaurants);
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
