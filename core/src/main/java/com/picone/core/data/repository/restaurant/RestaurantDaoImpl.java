package com.picone.core.data.repository.restaurant;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;
import com.picone.core.data.repository.services.RetrofitClient;
import com.picone.core.domain.entity.pOJOprediction.PredictionResponse;
import com.picone.core.domain.entity.pOJOrestaurant.NearBySearch;
import com.picone.core.domain.entity.pOJOrestaurantDetail.RestaurantDetail;
import com.picone.core.domain.entity.pOJOrestaurantDistance.RestaurantDistance;
import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.entity.user.User;

import java.util.List;

import javax.inject.Inject;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Observable;

import static com.picone.core.utils.ConstantParameter.RADIUS;
import static com.picone.core.utils.ConstantParameter.RESTAURANT_REF;
import static com.picone.core.utils.ConstantParameter.USER_REF;

public class RestaurantDaoImpl implements RestaurantDao {

    @Inject
    protected FirebaseDatabase database;
    @Inject
    protected RetrofitClient retrofitClient;
    private DatabaseReference restaurantsDataBaseReference;


    public RestaurantDaoImpl(@NonNull FirebaseDatabase database, RetrofitClient retrofitClient) {
        this.database = database;
        this.retrofitClient = retrofitClient;
        this.restaurantsDataBaseReference = database.getReference().child(RESTAURANT_REF);
    }

    //----------------------------------------FIREBASE---------------------------------------------------------------

    @Override
    public Completable addRestaurant(Restaurant restaurant) {
        return RxFirebaseDatabase.setValue(restaurantsDataBaseReference
                .child(restaurant.getPlaceId()), restaurant);
    }

    @Override
    public Completable updateUserChosenRestaurant(User currentUser) {
        return RxFirebaseDatabase.setValue(database.getReference().child(USER_REF).child(currentUser.getUid()), currentUser);
    }

    @Override
    public Observable<List<Restaurant>> getAllPersistedRestaurants() {
        return RxFirebaseDatabase.observeValueEvent(restaurantsDataBaseReference, DataSnapshotMapper.listOf(Restaurant.class)).toObservable();
    }

    //-----------------------------------------------GOOGLE PLACE-------------------------------------------------------
    @Override
    public Observable<NearBySearch> getNearBySearch(@NonNull Location mCurrentLocation, String googleKey) {
        return retrofitClient.googlePlaceService()
                .getNearbySearch(mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude(), RADIUS, googleKey);
    }

    @Override
    public Observable<RestaurantDetail> getPlaceRestaurantDetail(@NonNull Restaurant restaurant, String googleKey) {
        return retrofitClient.googlePlaceService()
                .getRestaurantDetail(restaurant.getPlaceId(), googleKey);
    }

    @Override
    public Observable<RestaurantDistance> getRestaurantDistance(String currentLocation, String restaurantLocation, String googleKey) {
        return retrofitClient.googlePlaceService()
                .getRestaurantDistance(currentLocation, restaurantLocation, googleKey);
    }

    @Override
    public Observable<PredictionResponse> getPredictions(String restaurantName, String googleKey, String currentPosition) {
        return retrofitClient.googlePlaceService()
                .loadPredictions(restaurantName, googleKey, currentPosition, RADIUS);
    }

    @Override
    public Observable<JsonObject> sendNotification(JsonObject payload) {
        return retrofitClient.getNotificationService().sendNotification(payload);

    }
}
