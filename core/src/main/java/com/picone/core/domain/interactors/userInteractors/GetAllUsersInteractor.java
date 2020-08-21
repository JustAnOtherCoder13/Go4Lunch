package com.picone.core.domain.interactors.userInteractors;

import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.User;

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
