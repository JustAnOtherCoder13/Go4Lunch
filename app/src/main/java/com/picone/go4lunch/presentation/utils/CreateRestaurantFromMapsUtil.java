package com.picone.go4lunch.presentation.utils;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.RestaurantDetailPOJO.RestaurantDetail;
import com.picone.core.domain.entity.RestaurantPOJO.Photo;
import com.picone.core.domain.entity.RestaurantPOJO.RestaurantPOJO;
import com.picone.core.domain.entity.RestaurantPosition;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateRestaurantFromMapsUtil {


    private static String createPhotoUrl(String googleKey, RestaurantPOJO restaurantPOJO) {
        Photo photo = restaurantPOJO.getPhotos().get(0);
        return "https://maps.googleapis.com/maps/api/place/photo?"
                .concat("maxwidth=" + photo.getWidth())
                .concat("&photoreference=" + photo.getPhotoReference())
                .concat("&key=")
                .concat(googleKey);
    }

    public static Restaurant createRestaurant(RestaurantPOJO restaurantPOJO, String googleKey) {
        return new Restaurant(
                null,
                restaurantPOJO.getName(),
                "0",
                createPhotoUrl(googleKey, restaurantPOJO),
                new RestaurantPosition(restaurantPOJO.getGeometry().getLocation().getLat(), restaurantPOJO.getGeometry().getLocation().getLng()),
                restaurantPOJO.getVicinity(),
                restaurantPOJO.getPlaceId(),
                "0",
                "",
                "",
                0,
                0,
                new ArrayList<>());
    }

    public static String formatOpeningHours(RestaurantDetail restaurantDetail) {
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
