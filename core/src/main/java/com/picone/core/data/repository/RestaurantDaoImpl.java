package com.picone.core.data.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
    DatabaseReference restaurantsDataBaseReference;

    public RestaurantDaoImpl(FirebaseDatabase database){
        this.database = database;
        this.restaurantsDataBaseReference = database.getReference().child("restaurants");

    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return null;
    }

    @Override
    public Restaurant getRestaurant(int position) {
        return null;
    }

    public Observable <List<Restaurant>> getRestaurantForName(String restaurantName){
        Query query = restaurantsDataBaseReference.orderByChild("name").equalTo(restaurantName);
        return RxFirebaseDatabase.observeValueEvent(query, DataSnapshotMapper.listOf(Restaurant.class)).toObservable();
    }

    public Completable addRestaurant (Restaurant restaurant){
        restaurant.setKey(restaurantsDataBaseReference.child(restaurant.getName()).push().getKey());
        return RxFirebaseDatabase.setValue(restaurantsDataBaseReference
                .child(restaurant.getName()),restaurant);
    }
}
