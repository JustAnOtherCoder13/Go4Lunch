package com.picone.core.domain.interactors.restaurantsInteractors;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.RestaurantPosition;
import com.picone.core.domain.entity.retrofitRestaurant.Photo;
import com.picone.core.domain.entity.retrofitRestaurant.RestaurantPOJO;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GooglePlaceInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public GooglePlaceInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public Observable<List<Restaurant>> googlePlaceService(Location mCurrentLocation) {
        List<Restaurant> restaurantsFromMap = new ArrayList<>();
        return restaurantDataSource.googlePlaceService(mCurrentLocation)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(nearBySearch -> {
                            if (nearBySearch.getStatus().equals("OK")) {
                                for (RestaurantPOJO restaurantPOJO : nearBySearch.getRestaurantPOJOS()) {
                                    restaurantDataSource.getPlaceRestaurantDetail(restaurantPOJO)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())

                                            .subscribe(restaurantPOJO1 -> {
                                                Log.i("TAG", "googlePlaceService: "+restaurantPOJO1.getWebsite());
                                            });
                                    String name = restaurantPOJO.getName();
                                    int distance = 0;
                                    Photo photo = restaurantPOJO.getPhotos().get(0);
                                    //TODO hide apiKey
                                    String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?"
                                            .concat("maxwidth=" + photo.getWidth())
                                            .concat("&photoreference=" + photo.getPhotoReference())
                                            .concat("&key=AIzaSyBPfbZ_poV0QGgdifNxGzHHz2yS4L2evTI");

                                    Double lat = restaurantPOJO.getGeometry().getLocation().getLat();
                                    Double lng = restaurantPOJO.getGeometry().getLocation().getLng();
                                    String address = restaurantPOJO.getVicinity();
                                    Restaurant restaurant = new Restaurant(null, name, distance, photoUrl, "", address, 0, 0, new RestaurantPosition(lat, lng), 0, new ArrayList<>());
                                    if (!restaurantsFromMap.contains(restaurant))
                                        restaurantsFromMap.add(restaurant);
                                }
                            }
                            return Observable.create(emitter -> emitter.onNext(restaurantsFromMap));
                        });
    }
}
