package com.picone.core.data.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

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


    public RestaurantDaoImpl(FirebaseDatabase database) {
        this.database = database;
        restaurantDatabaseReference = database.getReference().child("restaurants");
    }

    @Override
    public Observable<List<Restaurant>> getAllRestaurants() {
        return RxFirebaseDatabase.observeSingleValueEvent(restaurantDatabaseReference
                , DataSnapshotMapper.listOf(Restaurant.class)).toObservable();
    }

    @Override
    public Restaurant getRestaurant(int position) {
        return null;
    }





}
