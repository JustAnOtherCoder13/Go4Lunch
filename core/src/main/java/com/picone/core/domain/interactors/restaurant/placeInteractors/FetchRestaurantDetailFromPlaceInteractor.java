package com.picone.core.domain.interactors.restaurant.placeInteractors;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.entity.RestaurantDetailPOJO.RestaurantDetail;

import java.util.Calendar;

import javax.inject.Inject;

import io.reactivex.Observable;

public class FetchRestaurantDetailFromPlaceInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public FetchRestaurantDetailFromPlaceInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<Restaurant> getRestaurantDetail(Restaurant restaurant, String googleKey) {
        return restaurantDataSource.getPlaceRestaurantDetail(restaurant, googleKey)
                .map(restaurantDetail -> restaurantDetailToRestaurantModel(restaurant, restaurantDetail));
    }

    private Restaurant restaurantDetailToRestaurantModel(Restaurant restaurant, RestaurantDetail restaurantDetail) {
        restaurant.setPhoneNumber(restaurantDetail.getResult().getFormattedPhoneNumber());
        restaurant.setWebsite(restaurantDetail.getResult().getWebsite());
        restaurant.setOpeningHours(formatOpeningHours(restaurantDetail));
        return restaurant;
    }

    private String formatOpeningHours(RestaurantDetail restaurantDetail) {
        String closingHour = restaurantDetail.getResult().getOpeningHours().getPeriods().get(getWeekDayTextValue()).getClose().getTime();
        if (restaurantDetail.getResult().getOpeningHours().getOpenNow())
            closingHour = "Open until : " + closingHour.substring(0, 2) + ":" + closingHour.substring(2, 4);
        else
            closingHour = "Closed";

        return closingHour;
    }

    @SuppressWarnings("ConstantConditions")
    private static int getWeekDayTextValue() {
        int weekDayTextValue = Calendar.DAY_OF_WEEK + 1;
        if (weekDayTextValue == 7)
            weekDayTextValue = 0;
        if (weekDayTextValue == 8)
            weekDayTextValue = 1;
        return weekDayTextValue;
    }


}
