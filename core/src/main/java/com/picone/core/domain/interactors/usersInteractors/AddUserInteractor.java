package com.picone.core.domain.interactors.usersInteractors;

import com.picone.core.data.repository.user.UserRepository;
import com.picone.core.domain.entity.user.User;

import io.reactivex.Completable;

public class AddUserInteractor extends UserBaseInteractor {

    public AddUserInteractor(UserRepository userDataSource) {
        super(userDataSource);
    }

    public Completable addUser(User user) {
        return userDataSource.addUser(user);
    }
}
