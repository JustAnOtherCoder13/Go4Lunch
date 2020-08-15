package com.picone.core.data.repository;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.domain.entity.User;

import java.util.List;

import javax.inject.Inject;

public class UserRepository {

    @Inject
    public UserDao userDao;
    @Inject
    public FirebaseDatabase dataBase;

    public UserRepository(FirebaseDatabase dataBase, UserDao dao) {
        this.dataBase = dataBase;
        userDao = dao;
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUser(int position) {
        return userDao.getUser(position);
    }

    public void addUser(User user) {
        userDao.AddUser(user);
    }
}
