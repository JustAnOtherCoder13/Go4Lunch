package com.picone.core.data.mocks;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Generator {
    public static List<Restaurant> RESTAURANTS = Arrays.asList(

            new Restaurant("chez jojo", 4, "ma photo", "french", 1, 10, 2),
            new Restaurant("chez jaja", 5, "ma photo", "bulgar", 2, 12, 2),
            new Restaurant("chez jiji", 7, "ma photo", "roman", 3, 14, 2)
    );

    public static List<Restaurant> generateRestaurant() {
        return new ArrayList<>(RESTAURANTS);
    }

    public static List<User> USERS = Arrays.asList(

            new User("Frank", new Restaurant("", 0, "", "", 0, 0, 0)),
            new User("Jo", new Restaurant("", 0, "", "", 0, 0, 0)),
            new User("Edouard", new Restaurant("", 0, "", "", 0, 0, 0))
    );

    public static List<User> generateUsers() {
        return new ArrayList<>(USERS);
    }
}
