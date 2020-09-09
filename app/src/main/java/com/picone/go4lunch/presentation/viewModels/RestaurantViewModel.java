package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.DailySchedule;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.interactors.restaurantsInteractors.restaurant.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.restaurant.DeleteRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.restaurant.GetAllGeneratedRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.restaurant.GetAllPersistedRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.restaurant.GetGeneratedRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.restaurant.GetPersistedRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.userForRestaurant.AddCurrentUserToRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.userForRestaurant.DeleteCurrentUserFromRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.userForRestaurant.GetAllInterestedUsersForRestaurantInteractor;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RestaurantViewModel extends ViewModel {

    private MutableLiveData<User> _getCurrentUser = new MutableLiveData<>();
    private MutableLiveData<Restaurant> _getSelectedRestaurant = new MutableLiveData<>();
    private MutableLiveData<List<User>> _allInterestedUsersForRestaurant = new MutableLiveData<>();

    private GetAllGeneratedRestaurantsInteractor getAllGeneratedRestaurantsInteractor;
    private GetAllPersistedRestaurantsInteractor getAllPersistedRestaurantsInteractor;
    private GetGeneratedRestaurantInteractor getGeneratedRestaurantInteractor;
    private GetPersistedRestaurantInteractor getPersistedRestaurantInteractor;
    private AddRestaurantInteractor addRestaurantInteractor;
    private DeleteRestaurantInteractor deleteRestaurantInteractor;


    private GetAllInterestedUsersForRestaurantInteractor getAllInterestedUsersForRestaurantInteractor;
    private AddCurrentUserToRestaurantInteractor addCurrentUserToRestaurantInteractor;
    private DeleteCurrentUserFromRestaurantInteractor deleteCurrentUserFromRestaurantInteractor;

    private static final String TAG = "restauViMo";

    @ViewModelInject
    public RestaurantViewModel(GetAllGeneratedRestaurantsInteractor getAllGeneratedRestaurantsInteractor, GetAllPersistedRestaurantsInteractor getAllPersistedRestaurantsInteractor
            , GetGeneratedRestaurantInteractor getGeneratedRestaurantInteractor, GetPersistedRestaurantInteractor getPersistedRestaurantInteractor
            , AddRestaurantInteractor addRestaurantInteractor, DeleteRestaurantInteractor deleteRestaurantInteractor
            , GetAllInterestedUsersForRestaurantInteractor getAllInterestedUsersForRestaurantInteractor, AddCurrentUserToRestaurantInteractor addCurrentUserToRestaurantInteractor
            ,DeleteCurrentUserFromRestaurantInteractor deleteCurrentUserFromRestaurantInteractor) {

        this.getAllGeneratedRestaurantsInteractor = getAllGeneratedRestaurantsInteractor;
        this.getAllPersistedRestaurantsInteractor = getAllPersistedRestaurantsInteractor;
        this.getGeneratedRestaurantInteractor = getGeneratedRestaurantInteractor;
        this.getPersistedRestaurantInteractor = getPersistedRestaurantInteractor;
        this.addRestaurantInteractor = addRestaurantInteractor;
        this.deleteRestaurantInteractor = deleteRestaurantInteractor;

        this.getAllInterestedUsersForRestaurantInteractor = getAllInterestedUsersForRestaurantInteractor;
        this.addCurrentUserToRestaurantInteractor = addCurrentUserToRestaurantInteractor;
        this.deleteCurrentUserFromRestaurantInteractor = deleteCurrentUserFromRestaurantInteractor;
    }


    //-----------------------------------------MANAGE RESERVATION------------------------------------------------------------
    @SuppressLint("CheckResult")
    public LiveData<List<User>> getAllInterestedUsersForRestaurant() {
        getAllInterestedUsersForRestaurantInteractor.getAllInterestedUsersForRestaurant(_getSelectedRestaurant.getValue().getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users ->
                        _allInterestedUsersForRestaurant.setValue(users));

        return _allInterestedUsersForRestaurant;
    }

    private Restaurant createNewRestaurant(Restaurant restaurant) {
        DailySchedule dailySchedule = new DailySchedule(new ArrayList<>());
        restaurant.setDailySchedule(dailySchedule);

        return restaurant;
    }

    @SuppressLint("CheckResult")
    public void addCurrentUserToRestaurant() {
        Restaurant selectedRestaurant = _getSelectedRestaurant.getValue();
        User currentUser = _getCurrentUser.getValue();

        addRestaurantInteractor.addRestaurant(createNewRestaurant(selectedRestaurant))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        addCurrentUserToRestaurantInteractor.addCurrentUserToRestaurant(selectedRestaurant.getName(), currentUser)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onComplete() {
                                        _getSelectedRestaurant.getValue().getDailySchedule().addUser(currentUser);
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });

    }

    //----------------------------GENERATED RESTAURANTS-----------------------------
    public List<Restaurant> getAllGeneratedRestaurants() {
        return getAllGeneratedRestaurantsInteractor.getAllGeneratedRestaurants();
    }

    public void setSelectedRestaurant(int position) {
        _getSelectedRestaurant.setValue(getGeneratedRestaurantInteractor.getGeneratedRestaurant(position));
    }

    public LiveData<Restaurant> getSelectedRestaurant = _getSelectedRestaurant;

    public void setCurrentUser(User currentUser) { _getCurrentUser.setValue(currentUser); }
}
