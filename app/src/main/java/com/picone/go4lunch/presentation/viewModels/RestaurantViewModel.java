package com.picone.go4lunch.presentation.viewModels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.data.mocks.Generator;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.interactors.restaurantsInteractors.GetAllRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantInteractor;

import java.util.List;

import io.reactivex.Observable;

public class RestaurantViewModel extends ViewModel {

    private MutableLiveData<User> _currentUser = new MutableLiveData<>();
    private MutableLiveData<Restaurant> selectedRestaurantMutableLiveData = new MutableLiveData<>();

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

    public Restaurant getGeneratedRestaurant(int position){
        return getRestaurant.getRestaurant(position);
    }

    public LiveData<Restaurant> selectedRestaurant = selectedRestaurantMutableLiveData;

    public void setSelectedRestaurant (int position){
        selectedRestaurantMutableLiveData.setValue(getRestaurant.getRestaurant(position));
    }
    public void setCurrentUser(User currentUser){
        _currentUser.setValue(currentUser);
    }

}
