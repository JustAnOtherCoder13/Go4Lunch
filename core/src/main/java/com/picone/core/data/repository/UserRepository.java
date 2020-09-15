package com.picone.core.data.repository;

import com.google.firebase.database.FirebaseDatabase;
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

    public User getUser(int position) {
        return userDao.getUser(position);
    }

    public Completable addUser(User user) {
        return userDao.AddUser(user);
    }

    public Observable<List<User>> getCurrentUserForEmail (String authUserEmail){
        return userDao.getCurrentUserForEmail(authUserEmail);
    }


}
