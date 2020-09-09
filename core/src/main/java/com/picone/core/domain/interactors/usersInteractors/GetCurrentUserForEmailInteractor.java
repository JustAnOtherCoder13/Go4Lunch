package com.picone.core.domain.interactors.usersInteractors;

import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.User;

import javax.inject.Inject;

public class GetCurrentUserForEmailInteractor {

    @Inject
    UserRepository userDataSource;

    public GetCurrentUserForEmailInteractor(UserRepository userDataSource) {
        this.userDataSource = userDataSource;
    }

    public User getUser(int position) {
        return userDataSource.getUser(position);
    }
}
