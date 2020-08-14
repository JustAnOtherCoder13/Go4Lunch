package com.picone.core.domain.interactors;

import com.picone.core.data.repository.UserDao;
import com.picone.core.data.repository.UserDaoImpl;
import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.User;

import java.util.List;

import javax.inject.Inject;

public class GetAllUsers {

   // @Inject
    //public UserRepository userDataSource;
   // UserDao userDao;

    @Inject
    List<User> mUsers;

    @Inject
    UserRepository userDataSource;

    public GetAllUsers(List<User> users, UserRepository userDataSource) {
        this.mUsers = users;
        this.userDataSource = userDataSource;
        //this.userDataSource = userDataSource;
    }

    public List<User> getAllUsers() {
        return userDataSource.getAllUsers();
    }
}
