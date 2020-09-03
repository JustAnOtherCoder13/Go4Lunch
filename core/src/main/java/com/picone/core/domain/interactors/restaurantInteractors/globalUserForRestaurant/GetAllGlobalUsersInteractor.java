package com.picone.core.domain.interactors.restaurantInteractors.globalUserForRestaurant;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetAllGlobalUsersInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public GetAllGlobalUsersInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<List<User>> getAllGlobalUsers(){
        return restaurantDataSource.getAllGlobalUsers();
    }
}
