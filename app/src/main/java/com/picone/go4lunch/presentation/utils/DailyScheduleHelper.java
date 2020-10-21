package com.picone.go4lunch.presentation.utils;

import com.picone.core.domain.entity.user.UserDailySchedule;
import com.picone.core.domain.entity.restaurant.RestaurantDailySchedule;

import java.util.List;

import static com.picone.go4lunch.presentation.utils.ConstantParameter.TODAY;

public class DailyScheduleHelper {

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
}
