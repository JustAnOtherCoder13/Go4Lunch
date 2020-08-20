package com.picone.core.domain.interactors;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.List;

public class SetUserSelectedRestaurant {

    private Restaurant restaurant;
    private List<User> interestedUsers;

    public SetUserSelectedRestaurant(List<User> interestedUsers, Restaurant restaurant) {
        this.interestedUsers = interestedUsers;
        this.restaurant = restaurant;
    }

    public void setUserSelectedRestaurant(User currentUser) {
        interestedUsers.add(currentUser);
        restaurant.setInterestedColleague(interestedUsers);
    }
}
