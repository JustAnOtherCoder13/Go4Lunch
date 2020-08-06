package com.picone.core.data.repository;

import com.picone.core.domain.entity.User;

import java.util.List;

import javax.inject.Inject;

public class UserRepository {

    private final UserDao userDao;
    @Inject
    public FirebaseDataBase firebaseDataBase;

    public UserRepository() {
        userDao = firebaseDataBase.userDao();
    }

    public List<User> getAllUsers(){return userDao.getAllUsers();}

    public User getUser(int position){return userDao.getUser();}
}
