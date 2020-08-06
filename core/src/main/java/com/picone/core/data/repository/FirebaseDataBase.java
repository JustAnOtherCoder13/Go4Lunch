package com.picone.core.data.repository;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.data.mocks.Generator;
import com.picone.core.domain.entity.User;

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
                    prePopulateDb();
                }
            }
        }
        return INSTANCE;
    }

    private static void prePopulateDb(){
        User user = Generator.generateUsers().get(0);
        user.setSelectedRestaurant(Generator.generateRestaurant().get(0));
        databaseReference.child("user").setValue(user);
    }


}
