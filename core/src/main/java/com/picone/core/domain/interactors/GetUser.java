package com.picone.core.domain.interactors;

import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.User;

public class GetUser {

    private UserRepository userDataSource;

    public GetUser(UserRepository userDataSource) {
        this.userDataSource = userDataSource;
    }

    public User getUser(int position) {
        return userDataSource.getUser(position);
    }
}
