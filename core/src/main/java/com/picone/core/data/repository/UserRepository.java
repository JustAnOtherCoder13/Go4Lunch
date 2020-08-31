package com.picone.core.data.repository;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.domain.entity.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class UserRepository {

    @Inject
    public UserDao userDao;
    @Inject
    public FirebaseDatabase dataBase;

    public UserRepository(FirebaseDatabase dataBase, UserDao dao) {
        this.dataBase = dataBase;
        userDao = dao;
    }

    public Observable<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }


    public Completable addUser(User currentUser) {
        return userDao.AddUser(currentUser);
    }

}
