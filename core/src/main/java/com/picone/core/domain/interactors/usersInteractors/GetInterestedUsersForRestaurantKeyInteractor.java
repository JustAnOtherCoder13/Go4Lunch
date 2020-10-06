package com.picone.core.domain.interactors.usersInteractors;

import com.picone.core.data.repository.user.UserRepository;
import com.picone.core.domain.entity.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetInterestedUsersForRestaurantKeyInteractor {

    @Inject
    UserRepository userDataSource;

    public GetInterestedUsersForRestaurantKeyInteractor(UserRepository userDataSource) {
        this.userDataSource = userDataSource;
    }

    public Observable<List<User>> getInterestedUsersForRestaurantKey(String restaurantKey){
        return userDataSource.getInterestedUsersForRestaurantKey(restaurantKey);
    }
}
