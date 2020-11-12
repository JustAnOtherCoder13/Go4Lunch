package com.picone.go4lunch.presentation.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.interactors.SendNotificationInteractor;
import com.picone.core.domain.interactors.chatInteractors.GetAllMessagesInteractor;
import com.picone.core.domain.interactors.chatInteractors.PostMessageInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.FetchRestaurantDetailFromPlaceInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.FetchRestaurantDistanceInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.FetchRestaurantFromPlaceInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.GetPredictionInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors.GetAllPersistedRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors.UpdateUserChosenRestaurantInteractor;
import com.picone.core.domain.interactors.usersInteractors.AddUserInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetAllUsersInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetCurrentUserForEmailInteractor;
import com.picone.core.domain.interactors.usersInteractors.UpdateUserInteractor;
import com.picone.core.utils.SchedulerProvider;
import com.picone.go4lunch.presentation.helpers.ErrorHandler;

public abstract class BaseViewModel extends ViewModel {

    protected MutableLiveData<ErrorHandler> errorState = new MutableLiveData<>(ErrorHandler.NO_ERROR);
    public LiveData<ErrorHandler> getErrorState = errorState;

    //------------------------RESTAURANT_INTERACTORS----------------------------
    protected AddRestaurantInteractor addRestaurantInteractor;
    protected UpdateUserChosenRestaurantInteractor updateUserChosenRestaurantInteractor;
    protected GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor;
    protected GetAllPersistedRestaurantsInteractor getAllPersistedRestaurantsInteractor;
    protected FetchRestaurantFromPlaceInteractor fetchRestaurantFromPlaceInteractor;
    protected FetchRestaurantDistanceInteractor fetchRestaurantDistanceInteractor;
    protected FetchRestaurantDetailFromPlaceInteractor fetchRestaurantDetailFromPlaceInteractor;
    protected GetPredictionInteractor getPredictionInteractor;
    protected SendNotificationInteractor sendNotificationInteractor;

    //------------------------USER_INTERACTORS----------------------------
    protected AddUserInteractor addUserInteractor;
    protected GetAllUsersInteractor getAllUsersInteractor;
    protected UpdateUserInteractor updateUserInteractor;

    //------------------------CHAT_INTERACTORS----------------------------
    protected GetAllMessagesInteractor getAllMessagesInteractor;
    protected PostMessageInteractor postMessageInteractor;

    protected SchedulerProvider schedulerProvider;

    protected void checkException() {
        errorState.setValue(ErrorHandler.ON_ERROR);
    }
}
