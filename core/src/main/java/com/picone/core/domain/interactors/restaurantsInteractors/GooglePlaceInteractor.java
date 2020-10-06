package com.picone.core.domain.interactors.restaurantsInteractors;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.RestaurantPosition;
import com.picone.core.domain.entity.retrofitRestaurant.Photo;
import com.picone.core.domain.entity.retrofitRestaurant.RestaurantPOJO;
import com.picone.core.domain.entity.retrofitRestaurantDistance.Row;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GooglePlaceInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public GooglePlaceInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    @SuppressLint("CheckResult")
    public Observable<List<Restaurant>> googlePlaceService(Location mCurrentLocation) {
        List<Restaurant> restaurantsFromMap = new ArrayList<>();
        return restaurantDataSource.googlePlaceService(mCurrentLocation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(nearBySearch -> {
                    if (nearBySearch.getStatus().equals("OK")) {
                        for (RestaurantPOJO restaurantPOJO : nearBySearch.getRestaurantPOJOS()) {
                            Photo photo = restaurantPOJO.getPhotos().get(0);
                            //TODO hide apiKey
                            String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?"
                                    .concat("maxwidth=" + photo.getWidth())
                                    .concat("&photoreference=" + photo.getPhotoReference())
                                    .concat("&key=AIzaSyBPfbZ_poV0QGgdifNxGzHHz2yS4L2evTI");
                            Restaurant restaurant = new Restaurant(null,
                                    restaurantPOJO.getName(),
                                    "0",
                                    photoUrl,
                                    "",
                                    restaurantPOJO.getVicinity(),
                                    "0",
                                    restaurantPOJO.getPlaceId(),
                                    "",
                                    "",
                                    0,
                                    new RestaurantPosition(restaurantPOJO.getGeometry().getLocation().getLat(), restaurantPOJO.getGeometry().getLocation().getLng()),
                                    0,
                                    new ArrayList<>());

                            if (!restaurantsFromMap.contains(restaurant))
                                restaurantsFromMap.add(restaurant);
                        }
                    }
                    return Observable.create(emitter -> emitter.onNext(restaurantsFromMap));
                });
    }
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public Observable<List<Restaurant>> getRestaurantDetail(List<Restaurant> allRestaurants){
        return
        Observable.create(emitter -> {
            for (Restaurant rest : allRestaurants) {
                restaurantDataSource.getPlaceRestaurantDetail(rest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(restaurantDetail -> {
                            rest.setPhoneNumber(restaurantDetail.getResult().getFormattedPhoneNumber());
                            rest.setWebsite(restaurantDetail.getResult().getWebsite());
                            rest.setOpeningHours(restaurantDetail.getResult().getOpeningHours().getWeekdayText().get(0));
                        });

            }
            emitter.onNext(allRestaurants);
        });

    }

    @SuppressLint("CheckResult")
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Observable<List<Restaurant>> getRestaurantDistance(List<Restaurant> allRestaurants, Location currentLocation){
        String currentLocationStr = String.valueOf(currentLocation.getLatitude()).concat(",").concat(String.valueOf(currentLocation.getLongitude()));
        return Observable.create(emitter -> {
            for (Restaurant restaurant: allRestaurants){
                String restaurantLocation = String.valueOf(restaurant.getRestaurantPosition().getLatitude()).concat(",").concat(String.valueOf(restaurant.getRestaurantPosition().getLongitude()));
                Log.i("TAG", "getRestaurantDistance: enter observable"+currentLocationStr+" "+restaurantLocation);
                restaurantDataSource.getRestaurantDistance(currentLocationStr,restaurantLocation)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(distance -> {
                        restaurant.setDistance(distance.getRows().get(0).getElements().get(0).getDistance().getText());
                        Log.i("restaurantDistance", "getRestaurantDistance: "+distance.getRows().get(0).getElements().get(0).getDistance().getText());
                        emitter.onNext(allRestaurants);
                    });}
        });

    }
}
