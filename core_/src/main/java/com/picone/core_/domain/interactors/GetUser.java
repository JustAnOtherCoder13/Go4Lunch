package com.picone.core_.domain.interactors;

import com.picone.core_.data.repository.UserRepository;
import com.picone.core_.domain.entity.User;

public class GetUser {

    private UserRepository userDataSource;

    public GetUser(UserRepository userDataSource) {
        this.userDataSource = userDataSource;
    }

    public User getUser(int position) {
        return userDataSource.getUser(position);
    }
}
