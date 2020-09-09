package com.picone.core.data.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.List;

import javax.inject.Inject;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class RestaurantDaoImpl implements RestaurantDao{

    @Inject
    FirebaseDatabase database;

    private DatabaseReference restaurantDatabaseReference;
    private DatabaseReference globalInterestedUsersDatabaseReference;
    private final String DAILY_SCHEDULE = "dailySchedule";
    private final String INTERESTED_USERS = "interestedUsers";

    public RestaurantDaoImpl(FirebaseDatabase database) {
        this.database = database;
        restaurantDatabaseReference = database.getReference().child("restaurants");
        globalInterestedUsersDatabaseReference = database.getReference().child("globalInterestedUsers");
    }

    //-----------------------RESTAURANT-------------------------------
    @Override
    public Observable<Restaurant> getPersistedRestaurant(String restaurantName) {
        return RxFirebaseDatabase.observeSingleValueEvent(restaurantDatabaseReference.child(restaurantName), Restaurant.class).toObservable();
    }

    @Override
    public Observable<List<Restaurant>> getAllPersistedRestaurants() {
        return RxFirebaseDatabase.observeSingleValueEvent(restaurantDatabaseReference,DataSnapshotMapper.listOf(Restaurant.class)).toObservable();
    }

    @Override
    public Completable addRestaurant(Restaurant restaurant) {
        return RxFirebaseDatabase.setValue(restaurantDatabaseReference.child(restaurant.getName()), restaurant);
    }

    @Override
    public Completable deleteRestaurant(String restaurantName) {
        return Completable.create(emitter -> restaurantDatabaseReference.child(restaurantName).removeValue());
    }

    //----------------------------INTERESTED_USER_FOR_RESTAURANT----------------------------------
    @Override
    public Observable<List<User>> getAllInterestedUsersForRestaurant(String restaurantName) {
        return RxFirebaseDatabase.observeValueEvent(restaurantDatabaseReference.child(restaurantName)
                .child(DAILY_SCHEDULE).child(INTERESTED_USERS), DataSnapshotMapper.listOf(User.class))
                .toObservable();
    }

    @Override
    public Completable addCurrentUserToRestaurant(String restaurantName, User currentUser) {
        return RxFirebaseDatabase.setValue(restaurantDatabaseReference.child(restaurantName)
                .child(DAILY_SCHEDULE).child(INTERESTED_USERS).child(currentUser.getName()), currentUser);
    }

    public Completable deleteCurrentUserFromRestaurant(String originalChosenRestaurantName, User currentUser) {
        return Completable.create(emitter -> restaurantDatabaseReference.child(originalChosenRestaurantName)
                .child(DAILY_SCHEDULE).child(INTERESTED_USERS).child(currentUser.getName()).removeValue());
    }
}
