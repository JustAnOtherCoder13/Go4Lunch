package com.picone.core.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.RestaurantDailySchedule;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.entity.UserDailySchedule;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class UserDaoImpl implements UserDao {

    @Inject
    protected FirebaseDatabase database;
    private DatabaseReference usersDatabaseReference;

    public UserDaoImpl(FirebaseDatabase database) {
        this.database = database;
        this.usersDatabaseReference = database.getReference().child("users");
    }

    @Override
    public Observable<List<User>> getAllUsers() {
        return RxFirebaseDatabase.observeSingleValueEvent(usersDatabaseReference
                , DataSnapshotMapper.listOf(User.class)).toObservable();
    }

    @Override
    public Observable<List<User>> getCurrentUserForEmail(String currentUserEmail) {
        return RxFirebaseDatabase.observeValueEvent(usersDatabaseReference.orderByChild("email")
                .equalTo(currentUserEmail), DataSnapshotMapper.listOf(User.class)).toObservable();
    }

    @Override
    public Completable AddUser(User user) {
        return RxFirebaseDatabase.setValue(usersDatabaseReference.push(), user);
    }


    public Completable updateUserChosenRestaurant(User currentUser, Restaurant restaurant) {
        DatabaseReference chosenRestaurantRef = database.getReference().child("restaurants").child(restaurant.getName());

        return Completable.create(emitter -> chosenRestaurantRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot persistedRestaurant) {
                if (persistedRestaurant.getValue() == null) {
                    RestaurantDailySchedule restaurantDailySchedule = new RestaurantDailySchedule("date", new ArrayList<>());
                    String restaurantKey = persistedRestaurant.getRef().push().getKey();
                    restaurant.setRestaurantDailySchedule(restaurantDailySchedule);
                    restaurant.setKey(restaurantKey);
                    chosenRestaurantRef.setValue(restaurant);
                }
                chosenRestaurantRef.child("restaurantDailySchedule").child("interestedUsers").push().setValue(currentUser)
                        .addOnSuccessListener(aVoid ->
                                usersDatabaseReference.orderByChild("email").equalTo(currentUser.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot users) {
                                        for (DataSnapshot user : users.getChildren()) {
                                            UserDailySchedule userDailySchedule = new UserDailySchedule("today", restaurant.getKey());
                                            user.getRef().child("dailySchedule").setValue(userDailySchedule)
                                                    .addOnSuccessListener(aVoid -> {
                                                        emitter.onComplete();
                                                    })

                                                    .addOnFailureListener(exception -> {
                                                        if (!emitter.isDisposed())
                                                            emitter.onError(exception);
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                }));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));
    }
}


