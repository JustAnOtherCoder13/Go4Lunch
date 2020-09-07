package com.picone.go4lunch.presentation.viewModels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.interactors.restaurantsInteractors.GetAllRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantInteractor;

import java.util.List;

public class RestaurantViewModel extends ViewModel {

    private GetAllRestaurantsInteractor getAllRestaurantsInteractor;
    private GetRestaurantInteractor getRestaurant;

    @ViewModelInject
    public RestaurantViewModel(GetAllRestaurantsInteractor getAllRestaurantsInteractor, GetRestaurantInteractor getRestaurant) {
        this.getAllRestaurantsInteractor = getAllRestaurantsInteractor;
        this.getRestaurant = getRestaurant;
    }

    public List<Restaurant> getAllRestaurants() {
        return getAllRestaurantsInteractor.getGeneratedRestaurants();
    }

    public Restaurant getRestaurant(int position) {
        return getRestaurant.getRestaurant(position);
    }
}
