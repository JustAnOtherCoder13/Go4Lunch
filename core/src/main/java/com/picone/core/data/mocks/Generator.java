package com.picone.core.data.mocks;

import com.picone.core.domain.entity.Restaurant;

import java.util.Arrays;
import java.util.List;

public abstract class Generator {
    public static List<Restaurant> RESTAURANTS = Arrays.asList(

            new Restaurant("chez jojo", 4, "ma photo", "french", 1, 10, 2),
            new Restaurant("chez jaja", 5, "ma photo", "bulgar", 2, 12, 2),
            new Restaurant("chez jiji", 7, "ma photo", "roman", 3, 14, 2)
    );

}
