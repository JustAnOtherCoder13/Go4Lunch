package com.picone.core.domain.interactors.userInteractors;

import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.User;

import javax.inject.Inject;

import io.reactivex.Completable;

public class AddUserInteractor {

    @Inject
    UserRepository userDataSource;

    public AddUserInteractor(UserRepository userDataSource) {
        this.userDataSource = userDataSource;
    }

    public Completable addUser(User currentUser) {
        return userDataSource.addUser(currentUser);
    }
}
