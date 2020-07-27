package com.picone.go4lunch.presentation.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.data.mocks.Generator;
import com.picone.core.domain.entity.Restaurant;

import java.util.List;

public class RestaurantViewModel extends ViewModel {

    public MutableLiveData<List<Restaurant>> restaurantsMutableLiveData = new MutableLiveData<>();

    public RestaurantViewModel() {
        restaurantsMutableLiveData.setValue(Generator.generateRestaurant());
    }
}
