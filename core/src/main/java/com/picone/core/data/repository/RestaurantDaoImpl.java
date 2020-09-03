package com.picone.core.data.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.picone.core.domain.entity.DailySchedule;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;

public class RestaurantDaoImpl implements RestaurantDao {

    @Inject
    protected FirebaseDatabase database;

    private DatabaseReference restaurantDatabaseReference;
    private DatabaseReference globalInterestedUsersDatabaseReference;
    private final String DAILY_SCHEDULE = "dailySchedule";
    private final String INTERESTED_USERS = "interestedUsers";


    public RestaurantDaoImpl(FirebaseDatabase database) {
        this.database = database;
        restaurantDatabaseReference = database.getReference().child("restaurants");
        globalInterestedUsersDatabaseReference = database.getReference().child("globalInterestedUsers");
    }

    //-----------------------RESTAURANT-------------------------------
    @Override
    public Observable<Restaurant> getPersistedRestaurant(String restaurantName) {
        return RxFirebaseDatabase.observeSingleValueEvent(restaurantDatabaseReference.child(restaurantName), Restaurant.class).toObservable();
    }

    @Override
    public Completable addRestaurant(Restaurant restaurant) {
        return RxFirebaseDatabase.setValue(restaurantDatabaseReference.child(restaurant.getName()), restaurant);
    }

    //--------------------------------DAILY_SCHEDULE-----------------------------------
    @Override
    public Observable<DailySchedule> getDailyScheduleForRestaurant(String selectedRestaurantName) {
        return RxFirebaseDatabase.observeSingleValueEvent(restaurantDatabaseReference.child(selectedRestaurantName)
                .child(DAILY_SCHEDULE), DailySchedule.class).toObservable();
    }

    @Override
    public Completable addDailyScheduleToRestaurant(DailySchedule dailySchedule, String selectedRestaurantName) {
        return RxFirebaseDatabase.setValue(restaurantDatabaseReference.child(selectedRestaurantName)
                .child(DAILY_SCHEDULE), dailySchedule);
    }

    @Override
    public Completable deleteDailyScheduleFromRestaurant(String selectedRestaurantName) {
        return Completable.create(emitter ->
                restaurantDatabaseReference.child(selectedRestaurantName)
                .child(DAILY_SCHEDULE).removeValue());
    }

    //----------------------------INTERESTED_USER_FOR_RESTAURANT----------------------------------
    @Override
    public Observable<List<User>> getAllInterestedUsersForRestaurant(String restaurantName) {
        return RxFirebaseDatabase.observeSingleValueEvent(restaurantDatabaseReference.child(restaurantName)
                .child(DAILY_SCHEDULE).child(INTERESTED_USERS), DataSnapshotMapper.listOf(User.class))
                .toObservable();
    }

    @Override
    public Completable addCurrentUserToRestaurant(String restaurantName, User currentUser) {
        return RxFirebaseDatabase.setValue(restaurantDatabaseReference.child(restaurantName)
                .child(DAILY_SCHEDULE).child(INTERESTED_USERS).child(currentUser.getName()), currentUser);
    }

    public Completable deleteCurrentUserFromRestaurant(String originalChosenRestaurantName, User currentUser) {
        return Completable.create(emitter -> restaurantDatabaseReference.child(originalChosenRestaurantName)
                .child(DAILY_SCHEDULE).child(INTERESTED_USERS).child(currentUser.getName()).removeValue());
    }

    //-----------------------------GLOBAL_INTERESTED_USER----------------------------
    @Override
    public Observable<User> getGlobalCurrentUser(User currentUser) {
        return RxFirebaseDatabase.observeSingleValueEvent(globalInterestedUsersDatabaseReference.child(currentUser.getName()), User.class).toObservable();
    }

    @Override
    public Observable<List<User>> getAllGlobalInterestedUsers() {
        return RxFirebaseDatabase.observeSingleValueEvent(globalInterestedUsersDatabaseReference,DataSnapshotMapper.listOf(User.class)).toObservable();
    }

    @Override
    public Completable addCurrentUserToGlobalList(User persistedCurrentUserWithRestaurantSet) {
        return RxFirebaseDatabase.setValue(globalInterestedUsersDatabaseReference.child(persistedCurrentUserWithRestaurantSet.getName()), persistedCurrentUserWithRestaurantSet);
    }

    @Override
    public Completable deleteUserFromGlobalList(User globalPersistedUser) {
        return Completable.create(emitter -> database.getReference().child("global_interested_users").child(globalPersistedUser.getName()).removeValue());
    }
}
