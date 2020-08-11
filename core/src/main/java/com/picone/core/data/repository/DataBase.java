package com.picone.core.data.repository;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class DataBase {

    private static volatile DataBase INSTANCE;

    private static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public abstract RestaurantDao restaurantDao();

    public abstract UserDao userDao();

    public static DataBase getInstance() {

        INSTANCE = DataBase.getInstance();
        return INSTANCE;
    }


}
