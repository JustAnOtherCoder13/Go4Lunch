package com.picone.core.data.repository;

import android.content.Context;

public abstract class FirebaseDataBase {

    private static volatile FirebaseDataBase INSTANCE;

    public abstract RestaurantDao restaurantDao();

    public abstract UserDao userDao();

    public static FirebaseDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (FirebaseDataBase.class) {
                if (INSTANCE == null) {
                    //init db
                }
            }
        }
        return INSTANCE;
    }
}
