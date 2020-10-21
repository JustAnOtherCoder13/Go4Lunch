package com.picone.core.domain.interactors.usersInteractors;

import com.picone.core.data.repository.user.UserRepository;
import com.picone.core.domain.entity.user.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetAllUsersInteractor {

    @Inject
    UserRepository userDataSource;

    public GetAllUsersInteractor(UserRepository userDataSource) {
        this.userDataSource = userDataSource;
    }

    public Observable<List<User>> getAllUsers() {
        return userDataSource.getAllUsers();
    }
}
