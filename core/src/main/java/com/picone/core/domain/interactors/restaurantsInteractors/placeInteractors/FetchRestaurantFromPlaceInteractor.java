package com.picone.core.domain.interactors.restaurantsInteractors.placeInteractors;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.RestaurantPosition;
import com.picone.core.domain.entity.RestaurantPOJO.Photo;
import com.picone.core.domain.entity.RestaurantPOJO.RestaurantPOJO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FetchRestaurantFromPlaceInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public FetchRestaurantFromPlaceInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    @SuppressLint("CheckResult")
    public Observable<List<Restaurant>> fetchRestaurantFromPlace(Location mCurrentLocation, String googleKey) {
        List<Restaurant> restaurantsFromMap = new ArrayList<>();
        return restaurantDataSource.googlePlaceService(mCurrentLocation, googleKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(nearBySearch -> {
                    if (nearBySearch.getStatus().equals("OK")) {
                        for (RestaurantPOJO restaurantPOJO : nearBySearch.getRestaurantPOJOS()) {
                            Restaurant restaurant = createRestaurant(restaurantPOJO, googleKey);
                            if (!restaurantsFromMap.contains(restaurant))
                                restaurantsFromMap.add(restaurant);
                        }
                    }
                    return Observable.create(emitter -> emitter.onNext(restaurantsFromMap));
                });
    }

    private String createPhotoUrl(String googleKey, RestaurantPOJO restaurantPOJO) {
        Photo photo = restaurantPOJO.getPhotos().get(0);
        return "https://maps.googleapis.com/maps/api/place/photo?"
                .concat("maxwidth=" + photo.getWidth())
                .concat("&photoreference=" + photo.getPhotoReference())
                .concat("&key=")
                .concat(googleKey);
    }

    private Restaurant createRestaurant(RestaurantPOJO restaurantPOJO, String googleKey) {
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
}
