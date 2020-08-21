package com.picone.core.domain.interactors.restaurantInteractors;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.User;

import javax.inject.Inject;

import io.reactivex.Completable;

public class UpdateInterestedColleagueInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public UpdateInterestedColleagueInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Completable updateUserInterestedColleague(User user) {
        return restaurantDataSource.updateInterestedColleague(user);
    }
}
