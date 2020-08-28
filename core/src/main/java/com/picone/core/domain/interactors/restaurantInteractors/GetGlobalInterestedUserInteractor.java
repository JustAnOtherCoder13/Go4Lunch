package com.picone.core.domain.interactors.restaurantInteractors;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetGlobalInterestedUserInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public GetGlobalInterestedUserInteractor(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<User> getGlobalInterestedUser(User user){
        return restaurantDataSource.getGlobalInterestedUser(user);
    }
}
