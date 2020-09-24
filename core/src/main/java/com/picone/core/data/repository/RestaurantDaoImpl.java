package com.picone.core.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
    private DatabaseReference restaurantsDataBaseReference;

    public RestaurantDaoImpl(FirebaseDatabase database) {
        this.database = database;
        this.restaurantsDataBaseReference = database.getReference().child("restaurants");
    }

    @Override
    public Observable<Restaurant> getRestaurantForName(String restaurantName) {
        return RxFirebaseDatabase.observeSingleValueEvent(restaurantsDataBaseReference.child(restaurantName), Restaurant.class).toObservable();
    }

    @Override
    public Observable<List<Restaurant>> getRestaurantForKey(String restaurantKey) {
        Query query = restaurantsDataBaseReference.orderByChild("key").equalTo(restaurantKey);
        return RxFirebaseDatabase.observeValueEvent(query, DataSnapshotMapper.listOf(Restaurant.class)).toObservable();
    }

    @Override
    public Completable addRestaurant(Restaurant restaurant) {
        restaurant.setKey(restaurantsDataBaseReference.child(restaurant.getName()).push().getKey());
        Log.i("TAG", "addRestaurant: restaurant added");
        return RxFirebaseDatabase.setValue(restaurantsDataBaseReference
                .child(restaurant.getName()), restaurant);
    }

    @Override
    public Completable updateUserChosenRestaurant(User currentUser) {
        return RxFirebaseDatabase.setValue(database.getReference().child("users").child(currentUser.getUid()), currentUser);
    }
}
