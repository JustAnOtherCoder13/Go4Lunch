package com.picone.core.domain.interactors.userInteractors;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.User;

import javax.inject.Inject;

import io.reactivex.Completable;

public class UpdateInterestedColleagueInteractor {

    @Inject
    UserRepository userDataSource;

    public UpdateInterestedColleagueInteractor(UserRepository userDataSource) {
        this.userDataSource = userDataSource;
    }

    public Completable updateUserInterestedColleague(User user) {
        return userDataSource.updateInterestedColleague(user);
    }
}
