package com.picone.core.domain.interactors;

import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.User;

import javax.inject.Inject;

public class AddUser {

    @Inject
    UserRepository userDataSource;

    public AddUser(UserRepository userDataSource){
        this.userDataSource = userDataSource;

    }

    public void addUser(User user){
        userDataSource.addUser(user);
    }
}
