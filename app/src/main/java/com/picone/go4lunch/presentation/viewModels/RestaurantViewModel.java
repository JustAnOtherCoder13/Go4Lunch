package com.picone.go4lunch.presentation.viewModels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.picone.core_.domain.entity.Restaurant;
import com.picone.core_.domain.interactors.GetAllRestaurants;
import com.picone.core_.domain.interactors.GetRestaurant;
import com.picone.core_.domain.interactors.SetRestaurantOpinion;

import java.util.List;


public class RestaurantViewModel extends ViewModel {

    private MutableLiveData<List<Restaurant>> restaurantsMutableLiveData = new MutableLiveData<>();
    public GetAllRestaurants getAllRestaurants;
    private GetRestaurant getRestaurant;
    private SetRestaurantOpinion setRestaurantOpinion;
    private final SavedStateHandle savedStateHandle;


    @ViewModelInject
    public RestaurantViewModel(GetAllRestaurants getAllRestaurants, GetRestaurant getRestaurant
            , SetRestaurantOpinion setRestaurantOpinion,@Assisted SavedStateHandle savedStateHandle) {
        this.getAllRestaurants = getAllRestaurants;
        this.getRestaurant = getRestaurant;
        this.setRestaurantOpinion = setRestaurantOpinion;
        this.savedStateHandle = savedStateHandle;
        restaurantsMutableLiveData.setValue(getAllRestaurants.getAllRestaurants());
    }

    public LiveData<List<Restaurant>> getAllRestaurants() {
        return restaurantsMutableLiveData;
    }

    public Restaurant getRestaurant(int position) {
        return getRestaurant.getRestaurant(position);
    }

    public void setRestaurantOpinion(double note, long numberOfVote, Restaurant restaurant){
        setRestaurantOpinion.setRestaurantOpinion(note,numberOfVote,restaurant);
    }

}
