package com.picone.core.domain.interactors.usersInteractors;

import com.picone.core.data.repository.UserRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import javax.inject.Inject;

import io.reactivex.Completable;

public class UpdateUserChosenRestaurantInteractor {

    @Inject
    UserRepository userDataSource;

    public UpdateUserChosenRestaurantInteractor(UserRepository userDataSource) {
        this.userDataSource = userDataSource;
    }

    public Completable updateUserChosenRestaurant(User currentUser, Restaurant restaurant){
        return userDataSource.updateUserChosenRestaurant(currentUser, restaurant);
    }
}