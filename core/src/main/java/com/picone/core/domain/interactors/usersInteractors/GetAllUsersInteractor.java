package com.picone.core.domain.interactors.usersInteractors;

import com.picone.core.data.repository.user.UserRepository;
import com.picone.core.domain.entity.user.User;

import java.util.List;

import io.reactivex.Observable;

public class GetAllUsersInteractor extends UserBaseInteractor {

    public GetAllUsersInteractor(UserRepository userDataSource) {
        super(userDataSource);
    }

    public Observable<List<User>> getAllUsers() {
        return userDataSource.getAllUsers();
    }
}
