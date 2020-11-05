package com.picone.core.utils;

import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.entity.restaurant.RestaurantDailySchedule;
import com.picone.core.domain.entity.user.User;
import com.picone.core.domain.entity.user.UserDailySchedule;

import java.util.List;
import java.util.Objects;

import static com.picone.core.utils.ConstantParameter.TODAY;

public class FindInListHelper {

    public static RestaurantDailySchedule getRestaurantDailyScheduleOnToday(List<RestaurantDailySchedule> dailySchedules) {
        RestaurantDailySchedule restaurantDailyScheduleToReturn = null;
        if (dailySchedules != null)
            for (RestaurantDailySchedule restaurantDailySchedule : dailySchedules)
                if (restaurantDailySchedule.getDate().equals(TODAY)) {
                    restaurantDailyScheduleToReturn = restaurantDailySchedule;
                }
        return restaurantDailyScheduleToReturn;
    }

    public static UserDailySchedule getUserDailyScheduleOnToday(List<UserDailySchedule> dailySchedules) {
        UserDailySchedule userDailyScheduleToReturn = null;
        if (dailySchedules != null)
            for (UserDailySchedule userDailySchedule : dailySchedules)
                if (userDailySchedule.getDate().equals(TODAY)) {
                    userDailyScheduleToReturn = userDailySchedule;
                }
        return userDailyScheduleToReturn;
    }

    public static Restaurant getRestaurantForPlaceId(String placeId, List<Restaurant> allRestaurants) {
        Restaurant restaurantToReturn = null;
        for (Restaurant restaurant : Objects.requireNonNull(allRestaurants)) {
            if (restaurant.getPlaceId().equals(placeId))
                restaurantToReturn = restaurant;
        }
        return restaurantToReturn;
    }

    public static User getCurrentUserInfoForUid(List<User> allUsers, User currentUser) {
        User userToReturn = null;
        if (allUsers != null)
            for (User user : allUsers)
                if (user.getUid().equals(Objects.requireNonNull(currentUser.getUid())))
                    userToReturn = user;
        return userToReturn;
    }
}
