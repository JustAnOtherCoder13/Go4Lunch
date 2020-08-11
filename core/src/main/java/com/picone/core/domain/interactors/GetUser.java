package com.picone.core.domain.interactors;

import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.User;

import java.util.List;

import javax.inject.Inject;

public class GetUser {

    private UserRepository userDataSource;

    @Inject
    List<User> mUsers;


    public GetUser(List<User> users) {
        this.mUsers = users;
    }

    public User getUser(int position) {
        return mUsers.get(position);
    }
}
