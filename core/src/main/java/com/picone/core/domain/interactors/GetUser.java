package com.picone.core.domain.interactors;

import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.User;

import javax.inject.Inject;

public class GetUser {

    @Inject
    UserRepository userDataSource;

    public GetUser(UserRepository userDataSource) {
        this.userDataSource = userDataSource;
    }

    public User getUser(int position) {
        return userDataSource.getUser(position);
    }
}
