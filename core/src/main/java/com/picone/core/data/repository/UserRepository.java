package com.picone.core.data.repository;

import android.content.Context;

import com.picone.core.domain.entity.User;

import java.util.List;

public class UserRepository {

    private final UserDao userDao;

    public UserRepository(Context context) {
        FirebaseDataBase firebaseDataBase = FirebaseDataBase.getInstance(context);
        userDao = firebaseDataBase.userDao();
    }

    public List<User> getAllUsers(){return userDao.getAllUsers();}

    public User getUser(int position){return userDao.getUser();}
}
