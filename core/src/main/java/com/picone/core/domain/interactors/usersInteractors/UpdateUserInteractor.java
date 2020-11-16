package com.picone.core.domain.interactors.usersInteractors;

import com.picone.core.data.repository.user.UserRepository;
import com.picone.core.domain.entity.user.User;

import javax.inject.Inject;

import io.reactivex.Completable;

public class UpdateUserInteractor extends UserBaseInteractor {

    @Inject
    UserRepository userDataSource;

    public UpdateUserInteractor(UserRepository userDataSource) {
        super(userDataSource);
    }

    public Completable updateUser(User currentUser) {
        return userDataSource.updateUser(currentUser);
    }
}
