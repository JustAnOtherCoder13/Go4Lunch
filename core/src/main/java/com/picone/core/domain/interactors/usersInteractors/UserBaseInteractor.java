package com.picone.core.domain.interactors.usersInteractors;

import com.picone.core.data.repository.user.UserRepository;

import javax.inject.Inject;

public class UserBaseInteractor {

    @Inject
    protected UserRepository userDataSource;

    public UserBaseInteractor(UserRepository userDataSource) {
        this.userDataSource = userDataSource;
    }
}
