package com.picone.core.data.repository;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class UserRepository {

    @Inject
    public UserDaoImpl userDao;
    @Inject
    public FirebaseDatabase dataBase;

    public UserRepository(FirebaseDatabase dataBase, UserDaoImpl dao) {
        this.dataBase = dataBase;
        userDao = dao;
    }

    public Observable<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }

    public Observable<List<User>> getCurrentUserForEmail(String currentUserEmail) {
        return userDao.getCurrentUserForEmail(currentUserEmail);
    }

    public Completable addUser(User user) {
        return userDao.AddUser(user);
    }

    public Completable updateUserChosenRestaurant(User currentUser, Restaurant restaurant) {
        return userDao.updateUserChosenRestaurant(currentUser, restaurant);
    }

}
