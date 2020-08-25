package com.picone.core.domain.interactors.userInteractors;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetInterestedColleagueInteractor {

    @Inject
    UserRepository userDataSource;

    public GetInterestedColleagueInteractor(UserRepository userDataSource) {
        this.userDataSource = userDataSource;
    }

    public Observable<List<User>> getInterestedColleague() {
        return userDataSource.getInterestedColleague();
    }
}
