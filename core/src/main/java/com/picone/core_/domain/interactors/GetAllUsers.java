package com.picone.core_.domain.interactors;

import com.picone.core_.data.repository.UserRepository;
import com.picone.core_.domain.entity.User;

import java.util.List;

public class GetAllUsers {

    private UserRepository userDataSource;

    public GetAllUsers(UserRepository userDataSource) {
        this.userDataSource = userDataSource;
    }

    public List<User> getAllUsers() {
        return userDataSource.getAllUsers();
    }
}
