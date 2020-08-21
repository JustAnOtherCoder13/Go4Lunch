package com.picone.core.domain.interactors.restaurantInteractors;

import com.picone.core.data.repository.RestaurantRepository;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetInterestedColleagueInteractor {

    @Inject
    RestaurantRepository restaurantDataSource;

    public GetInterestedColleagueInteractor(RestaurantRepository restaurantDataSource) {
        this.restaurantDataSource = restaurantDataSource;
    }

    public Observable<List<User>> getInterestedColleague(Restaurant restaurant) {
        return restaurantDataSource.getInterestedColleague(restaurant);
    }
}
