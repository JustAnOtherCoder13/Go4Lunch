package com.picone.core.data.repository;

import com.google.firebase.database.FirebaseDatabase;
import com.picone.core.domain.entity.User;

import java.util.List;

import javax.inject.Inject;

public class UserRepository {

    private final UserDao userDao;
    @Inject
    public FirebaseDatabase dataBase;

    public UserRepository(FirebaseDatabase dataBase) {
       this.dataBase = dataBase;
        userDao = new UserDaoImpl(dataBase);
    }

    public List<User> getAllUsers(){return userDao.getAllUsers();}

    public User getUser(int position){return userDao.getUser();}

    public void addUser (User user) { userDao.AddUser(user); }
}
