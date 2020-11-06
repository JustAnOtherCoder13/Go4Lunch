package com.picone.core.domain.interactors.restaurantInteractors.placeInteractors;

import android.location.Location;

import com.picone.core.data.repository.restaurant.RestaurantRepository;
import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.entity.restaurant.RestaurantPosition;
import com.picone.core.domain.entity.restaurantPOJO.NearBySearch;
import com.picone.core.domain.entity.restaurantPOJO.RestaurantPOJO;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class FetchRestaurantFromPlaceInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public FetchRestaurantFromPlaceInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<List<Restaurant>> fetchRestaurantFromPlace(Location mCurrentLocation, String googleKey) {
        return restaurantDataSource
                .getNearBySearch(mCurrentLocation, googleKey)
                .map(NearBySearch::getRestaurantPOJOS)
                .map(restaurantPOJOS -> restaurantsToRestaurantModel(restaurantPOJOS, googleKey));
    }

    private List<Restaurant> restaurantsToRestaurantModel(List<RestaurantPOJO> restaurantsPojos, String googleKey) {
        List<Restaurant> restaurantsFromMap = new ArrayList<>();
        for (RestaurantPOJO restaurantPOJO : restaurantsPojos) {
            Restaurant restaurant = createRestaurant(restaurantPOJO, googleKey);
            if (!restaurantsFromMap.contains(restaurant))
                restaurantsFromMap.add(restaurant);
        }
        return restaurantsFromMap;
    }

    private String createPhotoUrl(String googleKey, RestaurantPOJO restaurantPOJO) {
        if (!restaurantPOJO.getPhotos().isEmpty())
        return "https://maps.googleapis.com/maps/api/place/photo?"
                .concat("maxwidth=" + restaurantPOJO.getPhotos().get(0).getWidth())
                .concat("&photoreference=" + restaurantPOJO.getPhotos().get(0).getPhotoReference())
                .concat("&key=")
                .concat(googleKey);
        else return "";
    }

    private Restaurant createRestaurant(RestaurantPOJO restaurantPOJO, String googleKey) {
        return new Restaurant(
                restaurantPOJO.getName(),
                "",
                createPhotoUrl(googleKey, restaurantPOJO),
                new RestaurantPosition(restaurantPOJO.getGeometry().getLocation().getLat(), restaurantPOJO.getGeometry().getLocation().getLng()),
                restaurantPOJO.getVicinity(),
                restaurantPOJO.getPlaceId(),
                "",
                "",
                "",
                new ArrayList<>(),
                new ArrayList<>());
    }
}
