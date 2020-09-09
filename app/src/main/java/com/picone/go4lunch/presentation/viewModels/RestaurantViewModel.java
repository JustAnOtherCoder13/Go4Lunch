package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.Toast;

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

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RestaurantViewModel extends ViewModel {

    private MutableLiveData<User> _getCurrentUser = new MutableLiveData<>();
    private MutableLiveData<Restaurant> _getSelectedRestaurant = new MutableLiveData<>();
    private MutableLiveData<List<User>> _allInterestedUsersForRestaurant = new MutableLiveData<>();
    private MutableLiveData<Boolean> _isThisRestaurantIsPersisted = new MutableLiveData<>();
    private MutableLiveData<Boolean> _isUserHasAlreadyChooseRestaurant = new MutableLiveData<>();
    private MutableLiveData<Restaurant> _userSelectedRestaurant = new MutableLiveData<>();

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
            , DeleteCurrentUserFromRestaurantInteractor deleteCurrentUserFromRestaurantInteractor) {

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
        //check if restaurant exist and if user has already chose a restaurant, assign values.
        getAllPersistedRestaurantsInteractor.getAllPersistedRestaurants()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restaurants -> {
                    for (Restaurant restaurant : restaurants) {

                        if (_getSelectedRestaurant.getValue()!=null && restaurant.getName().equals(_getSelectedRestaurant.getValue().getName()))
                            _isThisRestaurantIsPersisted.setValue(true);

                        getAllInterestedUsersForRestaurantInteractor.getAllInterestedUsersForRestaurant(restaurant.getName())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(users -> {
                                        for (User user : users) {
                                            if (user.getEmail().equals(_getCurrentUser.getValue().getEmail())) {
                                                _isUserHasAlreadyChooseRestaurant.setValue(true);
                                                _userSelectedRestaurant.setValue(restaurant);
                                            }
                                        }
                                        //user come from your lunch
                                        if (_getSelectedRestaurant.getValue()==null && !_isUserHasAlreadyChooseRestaurant.getValue()){
                                            Log.i(TAG, "getAllInterestedUsersForRestaurant: you hasn't chose a restaurant yet");

                                        }
                                        else  if (_getSelectedRestaurant.getValue()==null && _isUserHasAlreadyChooseRestaurant.getValue()){
                                            _getSelectedRestaurant.setValue(restaurant);
                                            _allInterestedUsersForRestaurant.setValue(users);
                                        }
                                    });

                        }
                });
        //if come from restaurant list
        if (_getSelectedRestaurant.getValue()!=null)
        getAllInterestedUsersForRestaurantInteractor.getAllInterestedUsersForRestaurant(_getSelectedRestaurant.getValue().getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users ->
                        _allInterestedUsersForRestaurant.setValue(users));

        return _allInterestedUsersForRestaurant;
    }

    @SuppressLint("CheckResult")
    public void addCurrentUserToRestaurant() {
        Restaurant selectedRestaurant = _getSelectedRestaurant.getValue();
        User currentUser = _getCurrentUser.getValue();

            if (_isUserHasAlreadyChooseRestaurant.getValue()){
                //If user has chose this restaurant
                if(_userSelectedRestaurant.getValue().getName().equals(selectedRestaurant.getName())){
                    Log.i(TAG, "addCurrentUserToRestaurant: this restaurant");
                }
                //If user has chose an other restaurant
                else {
                    Log.i(TAG, "addCurrentUserToRestaurant: other restaurant");
                }
            }
            //If restaurant exist in Db and user hasn't chose a restaurant yet
            else if(_isThisRestaurantIsPersisted.getValue() && !_isUserHasAlreadyChooseRestaurant.getValue()){
                Log.i(TAG, "addCurrentUserToRestaurant: add user");
                addCurrentUserToRestaurantInteractor.addCurrentUserToRestaurant(selectedRestaurant.getName(), currentUser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onComplete() {
                                Log.i(TAG, "onComplete: you've join this restaurant");
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
            }

            //if restaurant doesn't exist in Db
        else{
                DailySchedule dailySchedule = new DailySchedule("today", new ArrayList<>());
                selectedRestaurant.setDailySchedule(dailySchedule);
                Log.i(TAG, "addCurrentUserToRestaurant: add restaurant");
            addRestaurantInteractor.addRestaurant(selectedRestaurant)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onComplete() {
                            _isThisRestaurantIsPersisted.setValue(true);
                            addCurrentUserToRestaurantInteractor.addCurrentUserToRestaurant(selectedRestaurant.getName(), currentUser)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onComplete() {
                                _isUserHasAlreadyChooseRestaurant.setValue(true);
                                _userSelectedRestaurant.setValue(selectedRestaurant);
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


    }

    //----------------------------GENERATED RESTAURANTS-----------------------------
    public List<Restaurant> getAllGeneratedRestaurants() {
        return getAllGeneratedRestaurantsInteractor.getAllGeneratedRestaurants();
    }

    public void setSelectedRestaurant(int position) {
        _getSelectedRestaurant.setValue(getGeneratedRestaurantInteractor.getGeneratedRestaurant(position));
    }

    public LiveData<Restaurant> getSelectedRestaurant(){
        _userSelectedRestaurant.setValue(null);
        _isThisRestaurantIsPersisted.setValue(false);
        _isUserHasAlreadyChooseRestaurant.setValue(false);

        return _getSelectedRestaurant;
    }

    public void setCurrentUser(User currentUser) {
        _getCurrentUser.setValue(currentUser);
    }
}
