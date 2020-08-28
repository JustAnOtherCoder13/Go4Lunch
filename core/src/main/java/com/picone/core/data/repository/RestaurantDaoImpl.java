package com.picone.core.data.repository;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.domain.entity.DailySchedule;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class RestaurantDaoImpl implements RestaurantDao {

    @Inject
    protected FirebaseDatabase database;
    private DatabaseReference restaurantDatabaseReference;
    private DatabaseReference globalInterestedUsersDatabaseReference;


    public RestaurantDaoImpl(FirebaseDatabase database) {
        this.database = database;
        restaurantDatabaseReference = database.getReference().child("restaurants");
        globalInterestedUsersDatabaseReference = database.getReference().child("global_interested_users");
    }

    @Override
    public Completable addRestaurant(Restaurant restaurant) {
        Log.i("test", "addRestaurant: " + restaurant);
        return RxFirebaseDatabase.setValue(restaurantDatabaseReference
                        .child(restaurant.getName())
                , restaurant);
    }

    @Override
    public Observable<List<Restaurant>> getAllRestaurants() {
        return RxFirebaseDatabase.observeSingleValueEvent(restaurantDatabaseReference
                , DataSnapshotMapper.listOf(Restaurant.class)).toObservable();
    }

    @Override
    public Observable<Restaurant> getRestaurant(String restaurantName) {
        return RxFirebaseDatabase.observeSingleValueEvent(restaurantDatabaseReference.child(restaurantName)
                , Restaurant.class).toObservable();
    }

    @Override
    public Completable addDailyScheduleToRestaurant(DailySchedule dailySchedule, Restaurant restaurant) {
        return RxFirebaseDatabase.setValue(restaurantDatabaseReference
                        .child(restaurant.getName())
                        .child("daily_schedule")
                , dailySchedule);
    }

    @Override
    public Observable<DailySchedule> getDailyScheduleForRestaurant(String restaurantName) {
        return RxFirebaseDatabase.observeSingleValueEvent(restaurantDatabaseReference
                        .child(restaurantName)
                , DailySchedule.class).toObservable();
    }

    @Override
    public Observable<List<User>> getInterestedUsersForRestaurant(Date today, String restaurantName) {
        return RxFirebaseDatabase.observeSingleValueEvent(restaurantDatabaseReference
                        .child(restaurantName)
                        .child("daily_schedule")
                        .child(today.toString())
                , DataSnapshotMapper.listOf(User.class))
                .toObservable();
    }

    @Override
    public Completable updateInterestedUsersForRestaurant(Date today, String restaurantName, User user) {
        return RxFirebaseDatabase.setValue(restaurantDatabaseReference
                        .child(restaurantName)
                        .child("daily_schedule")
                        .child(today.toString())
                        .push()
                , user);
    }

    @Override
    public Observable<User> getGlobalInterestedUser(User user) {
        return RxFirebaseDatabase.observeSingleValueEvent(globalInterestedUsersDatabaseReference
                        .child(user.getName())
                , User.class).toObservable();
    }

    @Override
    public Completable addInterestedUserInGlobalList(User user) {
        return RxFirebaseDatabase.setValue(globalInterestedUsersDatabaseReference
                .child(user.getName()), user);
    }
}
