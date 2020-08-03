package com.picone.core.domain.interactors;

import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.User;

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
