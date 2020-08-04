package com.picone.core.data.repository;


public abstract class FirebaseDataBase {

    private static volatile FirebaseDataBase INSTANCE;

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
