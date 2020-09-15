package com.picone.core.domain.interactors.usersInteractors;

import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetCurrentUserForEmailInteractor {

    @Inject
    UserRepository userDataSource;

    public GetCurrentUserForEmailInteractor(UserRepository userDataSource) {
        this.userDataSource = userDataSource;
    }

    public Observable<List<User>> getCurrentUserForEmail (String authUserEmail){
        return userDataSource.getCurrentUserForEmail(authUserEmail);
    }
}