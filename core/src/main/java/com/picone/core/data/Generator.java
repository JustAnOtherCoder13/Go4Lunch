package com.picone.core.data;

import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.entity.restaurant.RestaurantPosition;
import com.picone.core.domain.entity.user.SettingValues;
import com.picone.core.domain.entity.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Generator {

    static List<User> FAKE_USERS = Arrays.asList(
            new User("1", "Jiji", "jiji@gmail.com", "", new ArrayList<>(), new SettingValues()),
            new User("2", "Jaja", "jaja@gmail.com", "", new ArrayList<>(), new SettingValues()),
            new User("3", "Jojo", "jojo@gmail.com", "", new ArrayList<>(), new SettingValues())
    );

    public static List<User> generateUsers() {
        return new ArrayList<>(FAKE_USERS);
    }


    static List<Restaurant> FAKE_RESTAURANTS = Arrays.asList(
            new Restaurant("Chez Jiji", "10m", "", new RestaurantPosition(), "avenue jiji", "13127", "10", "", "", new ArrayList<>(), new ArrayList<>()),
            new Restaurant("Chez Jaja", "20m", "", new RestaurantPosition(), "avenue jaja", "13700", "11", "", "", new ArrayList<>(), new ArrayList<>()),
            new Restaurant("Chez Jojo", "30m", "", new RestaurantPosition(), "avenue jojo", "13013", "12", "", "", new ArrayList<>(), new ArrayList<>())
    );

    public static List<Restaurant> generateRestaurants() {
        return new ArrayList<>(FAKE_RESTAURANTS);
    }
}
