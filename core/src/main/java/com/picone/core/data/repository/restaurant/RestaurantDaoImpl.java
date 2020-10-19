package com.picone.core.data.repository.restaurant;

import android.location.Location;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.picone.core.data.repository.notification.ApiClient;
import com.picone.core.data.repository.notification.NotificationService;
import com.picone.core.data.repository.place.RetrofitClient;
import com.picone.core.domain.entity.notificationPOJO.Message;
import com.picone.core.domain.entity.notificationPOJO.Notification;
import com.picone.core.domain.entity.notificationPOJO.NotificationToSend;
import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.entity.RestaurantDetailPOJO.RestaurantDetail;
import com.picone.core.domain.entity.RestaurantDistancePOJO.RestaurantDistance;
import com.picone.core.domain.entity.RestaurantPOJO.NearBySearch;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.entity.predictionPOJO.PredictionResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDaoImpl implements RestaurantDao {

    @Inject
    protected FirebaseDatabase database;
    @Inject
    protected RetrofitClient retrofitClient;
    private DatabaseReference restaurantsDataBaseReference;

    private final String RADIUS = "400";


    public RestaurantDaoImpl(FirebaseDatabase database, RetrofitClient retrofitClient) {
        this.database = database;
        this.retrofitClient = retrofitClient;
        this.restaurantsDataBaseReference = database.getReference().child("restaurants");
    }

    //----------------------------------------FIREBASE---------------------------------------------------------------

    @Override
    public Completable addRestaurant(Restaurant restaurant) {
        return RxFirebaseDatabase.setValue(restaurantsDataBaseReference
                .child(restaurant.getPlaceId()), restaurant);
    }

    @Override
    public Completable updateUserChosenRestaurant(User currentUser) {
        return RxFirebaseDatabase.setValue(database.getReference().child("users").child(currentUser.getUid()), currentUser);
    }

    @Override
    public Observable<List<Restaurant>> getAllPersistedRestaurants() {
        return RxFirebaseDatabase.observeValueEvent(restaurantsDataBaseReference, DataSnapshotMapper.listOf(Restaurant.class)).toObservable();
    }

//-----------------------------------------------GOOGLE PLACE-------------------------------------------------------
    @Override
    public Observable<NearBySearch> getNearBySearch(Location mCurrentLocation, String googleKey) {
        return retrofitClient.googlePlaceService()
                .getNearbySearch(mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude(), RADIUS, googleKey);
    }

    @Override
    public Observable<RestaurantDetail> getPlaceRestaurantDetail(Restaurant restaurant, String googleKey) {
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

    public Observable<NotificationToSend> sendNotification(String token){
        return Observable.create(emitter -> {
            ApiClient apiClient = new ApiClient();
            NotificationToSend notificationToSend = new NotificationToSend();
            notificationToSend.setMessage(new Message());
            notificationToSend.getMessage().setNotification(new Notification());
            notificationToSend.getMessage().getNotification().setTitle("Today's lunch");
            notificationToSend.getMessage().setToken(token);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(new Gson().toJson(notificationToSend));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            apiClient.getApiService().sendNotification(jsonObject).enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    Log.i("TAG", "onResponse: "+response.raw());
                   //emitter.onNext(response.body());
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {

                }
            });
        });


    }
}
