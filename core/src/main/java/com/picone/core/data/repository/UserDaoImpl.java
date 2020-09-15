package com.picone.core.data.repository;

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

        return RxFirebaseDatabase.observeSingleValueEvent(usersDatabaseReference
                , DataSnapshotMapper.listOf(User.class)).toObservable();
    }

    @Override
    public User getUser(int position) {
        return null;
    }

    @Override
    public Completable AddUser(User user) {
        return RxFirebaseDatabase.setValue(usersDatabaseReference.push(),user);
    }

    public Observable<List<User>> getCurrentUserForEmail (String authUserEmail){
        Query query = usersDatabaseReference.orderByChild("email").equalTo(authUserEmail);
        return RxFirebaseDatabase.observeSingleValueEvent(query,DataSnapshotMapper.listOf(User.class)).toObservable();
    }
}


