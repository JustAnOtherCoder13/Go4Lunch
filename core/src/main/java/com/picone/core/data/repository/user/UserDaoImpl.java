package com.picone.core.data.repository.user;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.picone.core.domain.entity.User;

import java.util.List;

import javax.inject.Inject;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
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
        return RxFirebaseDatabase.observeValueEvent(usersDatabaseReference
                , DataSnapshotMapper.listOf(User.class)).toObservable();
    }

    @Override
    public Completable AddUser(User user) {
        user.setUid(usersDatabaseReference.push().getKey());
        return RxFirebaseDatabase.setValue(usersDatabaseReference.child(user.getUid()),user);
    }

    @Override
    public Observable<List<User>> getCurrentUserForEmail (String authUserEmail){
        Query query = usersDatabaseReference.orderByChild("email").equalTo(authUserEmail);
        return RxFirebaseDatabase.observeSingleValueEvent(query,DataSnapshotMapper.listOf(User.class)).toObservable();
    }

    @Override
    public Observable<List<User>> getInterestedUsersForRestaurantPlaceId(String restaurantPlaceId){
        Query query = usersDatabaseReference.orderByChild("userDailySchedule/restaurantKey").equalTo(restaurantPlaceId);
        return RxFirebaseDatabase.observeValueEvent(query,DataSnapshotMapper.listOf(User.class)).toObservable();
    }
}


