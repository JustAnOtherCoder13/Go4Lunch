package com.picone.core.data.mocks;

import com.picone.core.domain.entity.Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Generator {
    public static List<Restaurant> RESTAURANTS = Arrays.asList(

            new Restaurant("chez jojo", 4, "ma photo", "french","rue A", 10, 2),
            new Restaurant("chez jaja", 5, "ma photo", "bulgar","voie B", 12, 2),
            new Restaurant("chez jiji", 7, "ma photo", "roman", "allée C", 14, 2)
    );

}
