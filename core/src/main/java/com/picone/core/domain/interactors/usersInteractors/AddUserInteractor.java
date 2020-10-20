package com.picone.core.domain.interactors.usersInteractors;

import com.picone.core.data.repository.user.UserRepository;
import com.picone.core.domain.entity.user.User;

import javax.inject.Inject;

import io.reactivex.Completable;

public class AddUserInteractor {

    @Inject
    UserRepository userDataSource;

    public AddUserInteractor(UserRepository userDataSource) {
        this.userDataSource = userDataSource;
    }

    public Completable addUser(User user) {
        return userDataSource.addUser(user);
    }
}
