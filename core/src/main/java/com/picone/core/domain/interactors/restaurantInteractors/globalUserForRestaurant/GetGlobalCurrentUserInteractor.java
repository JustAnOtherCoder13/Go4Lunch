package com.picone.core.domain.interactors.restaurantInteractors.globalUserForRestaurant;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.User;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetGlobalCurrentUserInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public GetGlobalCurrentUserInteractor(RestaurantRepository restaurantDataSource){
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<User> getGlobalCurrentUser(User currentUser){
        return restaurantDataSource.getGlobalCurrentUser(currentUser);
    }
}
