package com.picone.core.data.repository;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class FirebaseDataBase {

    private static volatile FirebaseDataBase INSTANCE;

    private static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public abstract RestaurantDao restaurantDao();
    public abstract UserDao userDao();

    public static FirebaseDataBase getInstance() {
        if (INSTANCE == null) {
            synchronized (FirebaseDataBase.class) {
                if (INSTANCE == null) {
                    //init db
                    INSTANCE = FirebaseDataBase.getInstance();
                }
            }
        }
        return INSTANCE;
    }


}
