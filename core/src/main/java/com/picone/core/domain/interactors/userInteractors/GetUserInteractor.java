package com.picone.core.domain.interactors.userInteractors;

import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.User;

import javax.inject.Inject;

public class GetUserInteractor {

    @Inject
    UserRepository userDataSource;

    public GetUserInteractor(UserRepository userDataSource) {
        this.userDataSource = userDataSource;
    }

    public User getUser(int position) {
        return userDataSource.getUser(position);
    }
}
