package com.picone.go4lunch.presentation.viewModels;

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

    public RestaurantViewModel(GetAllRestaurants getAllRestaurants, GetRestaurant getRestaurant
            , SetRestaurantOpinion setRestaurantOpinion) {
        this.getAllRestaurants = getAllRestaurants;
        this.getRestaurant = getRestaurant;
        this.setRestaurantOpinion = setRestaurantOpinion;
        restaurantsMutableLiveData.setValue(getAllRestaurants.getAllRestaurants());
    }

    public LiveData<List<Restaurant>> getAllRestaurants() {
        return (LiveData<List<Restaurant>>) restaurantsMutableLiveData;
    }

    public Restaurant getRestaurant(int position) {
        return getRestaurant.getRestaurant(position);
    }

    public void setRestaurantOpinion(){
        setRestaurantOpinion.setRestaurantOpinion();
    }

}
