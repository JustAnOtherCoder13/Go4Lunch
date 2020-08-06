package com.picone.core.domain.interactors;

import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.User;

import java.util.List;

import javax.inject.Inject;

public class GetAllUsers {

    @Inject
    public UserRepository userDataSource;

    @Inject
    List<User> mUsers;

    public GetAllUsers(UserRepository userDataSource) {
        this.userDataSource = userDataSource;
    }

    public List<User> getAllUsers() {
        return mUsers;
    }
}
