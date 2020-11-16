package com.picone.core.domain.interactors.usersInteractors;

import com.picone.core.data.repository.user.UserRepository;
import com.picone.core.domain.entity.user.User;

import java.util.List;

import io.reactivex.Observable;

public class GetCurrentUserForEmailInteractor extends UserBaseInteractor {

    public GetCurrentUserForEmailInteractor(UserRepository userDataSource) {
        super(userDataSource);
    }

    public Observable<List<User>> getCurrentUserForEmail(String authUserEmail){
        return userDataSource.getCurrentUserForEmail(authUserEmail);
    }
}
