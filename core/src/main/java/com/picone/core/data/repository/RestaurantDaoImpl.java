package com.picone.core.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.entity.UserDailySchedule;

import java.util.List;

import javax.inject.Inject;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;

public class RestaurantDaoImpl implements RestaurantDao {

    @Inject
    FirebaseDatabase database;
    DatabaseReference restaurantsDataBaseReference;

    public RestaurantDaoImpl(FirebaseDatabase database) {
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

    @Override
    public Observable<List<Restaurant>> getRestaurantForName(String restaurantName) {
        Query query = restaurantsDataBaseReference.orderByChild("name").equalTo(restaurantName);
        return RxFirebaseDatabase.observeValueEvent(query, DataSnapshotMapper.listOf(Restaurant.class)).toObservable();
    }

    @Override
    public Observable<List<Restaurant>> getRestaurantForKey(String restaurantKey){
        Query query = restaurantsDataBaseReference.orderByChild("key").equalTo(restaurantKey);
        return RxFirebaseDatabase.observeValueEvent(query, DataSnapshotMapper.listOf(Restaurant.class)).toObservable();
    }

    @Override
    public Completable addRestaurant(Restaurant restaurant) {
        restaurant.setKey(restaurantsDataBaseReference.child(restaurant.getName()).push().getKey());
        return RxFirebaseDatabase.setValue(restaurantsDataBaseReference
                .child(restaurant.getName()), restaurant);
    }

    @Override
    public Completable updateUserChosenRestaurant(User currentUser) {
        Query query = database.getReference().child("users").orderByChild("email").equalTo(currentUser.getEmail());

        return Completable.create(emitter ->
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            dataSnapshot.getRef().child("userDailySchedule").setValue(currentUser.getUserDailySchedule())
                                    .addOnSuccessListener(
                                            aVoid -> emitter.onComplete())
                                    .addOnFailureListener(
                                            e -> {
                                                if (!emitter.isDisposed())
                                                    emitter.onError(e);
                                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }));
    }
}
