package com.picone.core.data.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.domain.entity.User;

import java.util.List;

import javax.inject.Inject;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class UserDaoImpl implements UserDao {

    @Inject
    protected FirebaseDatabase database;
    private DatabaseReference userDatabaseReference;

    public UserDaoImpl(FirebaseDatabase database) {
        this.database = database;
        this.userDatabaseReference = database.getReference().child("users");
    }

    @Override
    public Observable<List<User>> getAllUsers() {
        return RxFirebaseDatabase.observeSingleValueEvent(userDatabaseReference
                , DataSnapshotMapper.listOf(User.class)).toObservable();
    }

    @Override
    public User getUser(int position) {
        return null;
    }

    @Override
    public Completable AddUser(User user) {
        return RxFirebaseDatabase.setValue(userDatabaseReference.push(), user);
    }
}


