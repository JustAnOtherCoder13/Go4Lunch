package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.entity.UserDailySchedule;
import com.picone.core.domain.interactors.restaurantsInteractors.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetAllRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantForKeyInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantForNameInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.UpdateUserChosenRestaurantInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetCurrentUserForEmailInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetInterestedUsersForRestaurantKeyInteractor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RestaurantViewModel extends ViewModel {

    private static String DATE;

    private MutableLiveData<User> currentUserMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Restaurant> selectedRestaurantMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> interestedUsersMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> selectedRestaurantKeyMutableLiveData = new MutableLiveData<>();

    private GetAllRestaurantsInteractor getAllRestaurantsInteractor;
    private GetRestaurantInteractor getRestaurant;
    private GetRestaurantForNameInteractor getRestaurantForNameInteractor;
    private AddRestaurantInteractor addRestaurantInteractor;
    private UpdateUserChosenRestaurantInteractor updateUserChosenRestaurantInteractor;
    private GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor;
    private GetRestaurantForKeyInteractor getRestaurantForKeyInteractor;
    private GetInterestedUsersForRestaurantKeyInteractor getInterestedUsersForRestaurantKeyInteractor;


    @ViewModelInject
    public RestaurantViewModel(GetAllRestaurantsInteractor getAllRestaurantsInteractor
            , GetRestaurantInteractor getRestaurant, GetRestaurantForNameInteractor getRestaurantForNameInteractor
            , AddRestaurantInteractor addRestaurantInteractor, UpdateUserChosenRestaurantInteractor updateUserChosenRestaurantInteractor
            , GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor, GetRestaurantForKeyInteractor getRestaurantForKeyInteractor
            , GetInterestedUsersForRestaurantKeyInteractor getInterestedUsersForRestaurantKeyInteractor) {
        this.getAllRestaurantsInteractor = getAllRestaurantsInteractor;
        this.getRestaurant = getRestaurant;
        this.getRestaurantForNameInteractor = getRestaurantForNameInteractor;
        this.addRestaurantInteractor = addRestaurantInteractor;
        this.updateUserChosenRestaurantInteractor = updateUserChosenRestaurantInteractor;
        this.getCurrentUserForEmailInteractor = getCurrentUserForEmailInteractor;
        this.getRestaurantForKeyInteractor = getRestaurantForKeyInteractor;
        this.getInterestedUsersForRestaurantKeyInteractor = getInterestedUsersForRestaurantKeyInteractor;
        setDate();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void setSelectedRestaurant(int position) {
        selectedRestaurantMutableLiveData.setValue(getRestaurant.getRestaurant(position));
        getRestaurantForNameInteractor.getRestaurantForName(getRestaurant.getRestaurant(position).getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restaurants -> {
                    if (!restaurants.isEmpty()) {
                        selectedRestaurantKeyMutableLiveData.setValue(restaurants.get(0).getKey());
                        getInterestedUsersForRestaurantKeyInteractor.getInterestedUsersForRestaurantKey(restaurants.get(0).getKey())
                                .subscribe(users -> {
                                    interestedUsersMutableLiveData.setValue(users);});
                    } else {
                        addRestaurantWhenNotPersisted(getRestaurant.getRestaurant(position));
                        interestedUsersMutableLiveData.setValue(new ArrayList<>());}
                });
    }

    public LiveData<List<User>> getInterestedUsersForRestaurant = interestedUsersMutableLiveData;

    public LiveData<Restaurant> getSelectedRestaurant = selectedRestaurantMutableLiveData;

    public List<Restaurant> getAllRestaurants() {
        return getAllRestaurantsInteractor.getGeneratedRestaurants();
    }

    //Suppress warning is safe cause current user can't be nul, already set in restaurantListFragment
    //And subscribe is used to check if User has already set a restaurant.
    @SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
    @SuppressLint("CheckResult")
    public void addRestaurant(Restaurant restaurant) {
        if (currentUserMutableLiveData.getValue().getUserDailySchedule() != null) {
            getRestaurantForKeyInteractor.getRestaurantForKey(currentUserMutableLiveData.getValue().getUserDailySchedule().getRestaurantKey())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(restaurantsForKey -> {
                        Restaurant originalChosenRestaurant = restaurantsForKey.get(0);
                        if (originalChosenRestaurant.getName().equals(selectedRestaurantMutableLiveData.getValue().getName())) {
                            Log.i("TAG", "addRestaurant: same restaurant");
                        } else {
                            updateUserChosenRestaurant();
                        }
                    });
        } else {
            updateUserChosenRestaurant();
        }
    }

    //Suppress warning is safe cause subscribe is only used to set _currentUser
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void getCurrentUserForEmail(String authUserEmail) {
        getCurrentUserForEmailInteractor.getCurrentUserForEmail(authUserEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> currentUserMutableLiveData.setValue(users.get(0)));
    }


    private void addRestaurantWhenNotPersisted(Restaurant restaurant) {
        addRestaurantInteractor.addRestaurant(restaurant)
                .doOnError(throwable -> {})
                .subscribe();
    }

    //currentUserMutableLiveData.getValue().getEmail() can't be null cause already set in restaurant list fragment
    @SuppressWarnings("ConstantConditions")
    private void updateUserChosenRestaurant() {

        updateUserChosenRestaurantInteractor.updateUserChosenRestaurant
                (currentUserMutableLiveData.getValue().getEmail(), new UserDailySchedule(DATE, selectedRestaurantKeyMutableLiveData.getValue()))
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @SuppressWarnings("ResultOfMethodCallIgnored")
                    @SuppressLint("CheckResult")
                    @Override
                    public void onComplete() {
                        getInterestedUsersForRestaurantKeyInteractor.getInterestedUsersForRestaurantKey(selectedRestaurantKeyMutableLiveData.getValue())
                                .subscribe(users -> interestedUsersMutableLiveData.setValue(users));
                        Log.i("TAG", "onComplete: updateUserComplete");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void setDate() {
        Date today = Calendar.getInstance().getTime();
        DATE = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(today);
    }
}
