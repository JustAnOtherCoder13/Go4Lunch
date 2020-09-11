package com.picone.core.data.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.domain.entity.Restaurant;

import java.util.List;

import javax.inject.Inject;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class RestaurantDaoImpl implements RestaurantDao{

    @Inject
    FirebaseDatabase database;
    private DatabaseReference restaurantsDatabaseReference;

    public RestaurantDaoImpl(FirebaseDatabase database){
        this.database = database;
        restaurantsDatabaseReference = database.getReference().child("restaurants");
    }

    @Override
    public Observable<List<Restaurant>> getAllRestaurants() {
        return RxFirebaseDatabase.observeValueEvent(restaurantsDatabaseReference, DataSnapshotMapper.listOf(Restaurant.class)).toObservable();
    }

    @Override
    public Observable<List<Restaurant >>getRestaurant(String restaurantKey) {
        return RxFirebaseDatabase.observeValueEvent(restaurantsDatabaseReference
                .orderByChild("key").equalTo(restaurantKey),
                DataSnapshotMapper.listOf(Restaurant.class)).toObservable();
    }

    public Completable addRestaurant (Restaurant restaurant){
        return RxFirebaseDatabase.setValue(restaurantsDatabaseReference.push(),restaurant);
    }
}
