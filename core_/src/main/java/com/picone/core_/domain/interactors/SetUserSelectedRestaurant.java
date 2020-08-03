package com.picone.core_.domain.interactors;

import com.picone.core_.domain.entity.Restaurant;
import com.picone.core_.domain.entity.User;

public class SetUserSelectedRestaurant {

    private Restaurant restaurant;
    private User user;

    public SetUserSelectedRestaurant(User user, Restaurant restaurant) {
        this.user = user;
        this.restaurant = restaurant;
    }

    public void setUserSelectedRestaurant() {
        user.setSelectedRestaurant(restaurant);
    }
}
