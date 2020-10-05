package com.picone.core.data.repository;

import android.location.Location;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.entity.retrofitRestaurant.NearBySearch;

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


    public RestaurantDaoImpl(FirebaseDatabase database, RetrofitClient retrofitClient) {
        this.database = database;
        this.retrofitClient = retrofitClient;
        this.restaurantsDataBaseReference = database.getReference().child("restaurants");
    }

    @Override
    public Observable<Restaurant> getRestaurantForName(String restaurantName) {
        return RxFirebaseDatabase.observeSingleValueEvent(restaurantsDataBaseReference.child(restaurantName), Restaurant.class).toObservable();
    }

    @Override
    public Observable<List<Restaurant>> getRestaurantForKey(String restaurantKey) {
        Query query = restaurantsDataBaseReference.orderByChild("key").equalTo(restaurantKey);
        return RxFirebaseDatabase.observeSingleValueEvent(query, DataSnapshotMapper.listOf(Restaurant.class)).toObservable();
    }

    @Override
    public Completable addRestaurant(Restaurant restaurant) {
        restaurant.setKey(restaurantsDataBaseReference.child(restaurant.getName()).push().getKey());
        return RxFirebaseDatabase.setValue(restaurantsDataBaseReference
                .child(restaurant.getName()), restaurant);
    }

    @Override
    public Completable updateUserChosenRestaurant(User currentUser) {
        return RxFirebaseDatabase.setValue(database.getReference().child("users").child(currentUser.getUid()), currentUser);
    }

    @Override
    public Completable updateNumberOfInterestedUsersForRestaurant(String restaurantName, int numberOfInterestedUsers) {
        return RxFirebaseDatabase.setValue(restaurantsDataBaseReference.child(restaurantName).child("numberOfInterestedUsers"), numberOfInterestedUsers);
    }

    @Override
    public Observable<List<Restaurant>> getAllPersistedRestaurants() {
        return RxFirebaseDatabase.observeValueEvent(restaurantsDataBaseReference, DataSnapshotMapper.listOf(Restaurant.class)).toObservable();
    }

    public Observable<NearBySearch> googlePlaceService(Location mCurrentLocation) {

        return Observable.create(emitter ->
                retrofitClient.googlePlaceService()
                .getNearbySearch("restaurant"
                                , mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude()
                                , 700)
                .enqueue(new Callback<NearBySearch>() {
                    @Override
                    public void onResponse(Call<NearBySearch> call, Response<NearBySearch> response) {
                        emitter.onNext(response.body());
                    }

                    @Override
                    public void onFailure(Call<NearBySearch> call, Throwable t) {
                        emitter.onError(t);
                        Log.d("onFailure", t.toString());
                    }
                }));


    }

    public Completable updateFanListForRestaurant(String restaurantName, List<String> fanList) {
        return RxFirebaseDatabase.setValue(restaurantsDataBaseReference.child(restaurantName).child("fanList"), fanList);
    }

    public Observable<List<String>> getFanListForRestaurant(String restaurantName) {
        return RxFirebaseDatabase.observeSingleValueEvent(restaurantsDataBaseReference.child(restaurantName).child("fanList"), DataSnapshotMapper.listOf(String.class)).toObservable();
    }
}
