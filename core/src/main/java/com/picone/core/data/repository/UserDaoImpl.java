package com.picone.core.data.repository;

import androidx.annotation.NonNull;

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
import java.util.Map;

import javax.inject.Inject;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;

public class UserDaoImpl implements UserDao {

    @Inject
    protected FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDatabaseReference;

    public UserDaoImpl(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
        this.usersDatabaseReference = firebaseDatabase.getReference().child("users");
    }

    @Override
    public Observable<List<User>> getAllUsers() {
        return RxFirebaseDatabase.observeSingleValueEvent(usersDatabaseReference
                , DataSnapshotMapper.listOf(User.class)).toObservable();
    }

    @Override
    public Observable <List<User>>getCurrentUserForEmail(String currentUserEmail) {
        return RxFirebaseDatabase.observeValueEvent(usersDatabaseReference.orderByChild("email")
        .equalTo(currentUserEmail),DataSnapshotMapper.listOf(User.class)).toObservable();
    }

    @Override
    public Completable AddUser(User user) {
        return RxFirebaseDatabase.setValue(usersDatabaseReference.push(),user);
    }

    public Completable updateUserChosenRestaurant(String currentUserEmail, String restaurantKey){
        ValueEventListener userReference = usersDatabaseReference.orderByChild("email").equalTo(currentUserEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDailySchedule userDailySchedule = new UserDailySchedule("today",restaurantKey);
                RxFirebaseDatabase.setValue(snapshot.getRef(), userDailySchedule);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return Completable.create(emitter -> usersDatabaseReference.addValueEventListener(userReference));
    }
}


