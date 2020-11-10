package com.picone.core.domain.interactors.restaurantInteractors.placeInteractors;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.pOJOrestaurantDetail.RestaurantDetail;
import com.picone.core.domain.entity.restaurant.Restaurant;

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
        String closingHour="Closed";
        if (restaurantDetail.getResult().getOpeningHours() != null) {
            if (restaurantDetail.getResult().getOpeningHours().getOpenNow()){
                closingHour = restaurantDetail.getResult().getOpeningHours().getPeriods().get(getWeekDayTextValue()).getClose().getTime();
                closingHour = closingHour.substring(0, 2) + ":" + closingHour.substring(2, 4);}
        }
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
