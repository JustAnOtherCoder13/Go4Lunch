package com.picone.core.data.mocks;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.RestaurantPosition;

import java.util.Arrays;
import java.util.List;

public abstract class Generator {

    public static List<Restaurant> RESTAURANTS = Arrays.asList(

            new Restaurant(null,"chez jojo", 4, "ma photo", "french", "1", 10, 2,new RestaurantPosition(43.542291,5.040949),0),
            new Restaurant(null,"chez jaja", 5, "ma photo", "bulgar", "2", 12, 2,new RestaurantPosition(43.54884,5.036508),0),
            new Restaurant(null,"chez jiji", 7, "ma photo", "roman", "3", 14, 2,new RestaurantPosition(43.547546,5.032522),0)
    );

}
