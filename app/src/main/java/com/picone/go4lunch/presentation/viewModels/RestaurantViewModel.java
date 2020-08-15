package com.picone.go4lunch.presentation.viewModels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.interactors.GetAllRestaurants;
import com.picone.core.domain.interactors.GetRestaurant;
import com.picone.core.domain.interactors.SetRestaurantOpinion;

import java.util.List;

public class RestaurantViewModel extends ViewModel {

    private MutableLiveData<List<Restaurant>> restaurantsMutableLiveData = new MutableLiveData<>();
    private GetAllRestaurants getAllRestaurants;
    private GetRestaurant getRestaurant;
    private SetRestaurantOpinion setRestaurantOpinion;

    @ViewModelInject
    public RestaurantViewModel(GetAllRestaurants getAllRestaurants, GetRestaurant getRestaurant) {
        this.getAllRestaurants = getAllRestaurants;
        this.getRestaurant = getRestaurant;
        restaurantsMutableLiveData.setValue(getAllRestaurants.getAllRestaurants());
    }

    public LiveData<List<Restaurant>> getAllRestaurants() {
        return restaurantsMutableLiveData;
    }

    public Restaurant getRestaurant(int position) {
        return getRestaurant.getRestaurant(position);
    }

    public void setRestaurantOpinion(double note, long numberOfVote, Restaurant restaurant) {
        setRestaurantOpinion.setRestaurantOpinion(note, numberOfVote, restaurant);
    }

}
