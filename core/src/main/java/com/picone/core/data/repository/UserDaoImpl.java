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
    protected FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<User> users = new ArrayList<>();


    public UserDaoImpl(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
        this.databaseReference = firebaseDatabase.getReference().child("users");
    }

    @Override
    public List<User> getAllUsers() {

        this.databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    users.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("error", "onCancelled: " + error);
            }
        });
        return users;
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


