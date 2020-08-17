package com.picone.core.data.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.domain.entity.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Maybe;
import io.reactivex.Observable;

public class UserDaoImpl implements UserDao {

    @Inject
    protected FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<User> users = new ArrayList<>();


    public UserDaoImpl(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
        this.databaseReference = firebaseDatabase.getReference().child("users");
    }

    @Override
    public Observable<List<User>> getAllUsers() {

        Maybe<List<User>> usersObservable = RxFirebaseDatabase.observeSingleValueEvent(databaseReference
        , DataSnapshotMapper.listOf(User.class));

        return usersObservable.toObservable();
    }

    @Override
    public User getUser(int position) {
        return null;
    }

    @Override
    public void AddUser(User user) {
        databaseReference.push().setValue(user);
    }
}


