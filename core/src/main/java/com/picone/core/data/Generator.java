package com.picone.core.data;

import com.picone.core.domain.entity.ChatMessage;
import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.entity.restaurant.RestaurantPosition;
import com.picone.core.domain.entity.user.SettingValues;
import com.picone.core.domain.entity.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Generator {

    private static List<User> FAKE_USERS = Arrays.asList(
            new User("1", "Jiji", "jiji@gmail.com", "", new ArrayList<>(), new SettingValues()),
            new User("2", "Jaja", "jaja@gmail.com", "", new ArrayList<>(), new SettingValues()),
            new User("3", "Jojo", "jojo@gmail.com", "", new ArrayList<>(), new SettingValues())
    );
    public static List<User> generateUsers() {
        return new ArrayList<>(FAKE_USERS);
    }

    private static List<Restaurant> FAKE_RESTAURANTS = Arrays.asList(
            new Restaurant("Chez Jiji", "10m", "", new RestaurantPosition(), "avenue jiji", "13127", "10", "", "", new ArrayList<>(), new ArrayList<>()),
            new Restaurant("Chez Jaja", "20m", "", new RestaurantPosition(), "avenue jaja", "13700", "11", "", "", new ArrayList<>(), new ArrayList<>()),
            new Restaurant("Chez Jojo", "30m", "", new RestaurantPosition(), "avenue jojo", "13013", "12", "", "", new ArrayList<>(), new ArrayList<>())
    );
    public static List<Restaurant> generateRestaurants() { return new ArrayList<>(FAKE_RESTAURANTS); }

    private static List<ChatMessage> FAKE_MESSAGES = Arrays.asList(
            new ChatMessage("now","","Jiji","hello","1"),
            new ChatMessage("now","","Jaja","hi, how are you","2"),
            new ChatMessage("now","","Jojo","hey, where we gonna eat on twelve?","3")
    );
    public static List<ChatMessage> generateChatMessages(){ return new ArrayList<>(FAKE_MESSAGES); }
}
