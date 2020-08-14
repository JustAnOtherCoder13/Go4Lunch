package com.picone.core.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.picone.core.domain.entity.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class UserDaoImpl implements UserDao {

    @Inject
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<User> users = new ArrayList<>();


    public UserDaoImpl(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
        this.databaseReference = firebaseDatabase.getReference().child("users");
    }

    @Override
    public List<User> getAllUsers() {
        readData(user -> {
            users.add(user);
            Log.i("test", "onCallBack: " + users);
        });

        return users;
    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public void AddUser(User user) {

        databaseReference.push().setValue(user);

    }

    public void readData(MyCallback myCallback) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DataSnapshot snapshotRef = snapshot.child("users");
                User user = snapshotRef.getValue(User.class);
                myCallback.onCallBack(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

interface MyCallback {

    void onCallBack(User user);

}
