package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.RestaurantDailySchedule;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.entity.UserDailySchedule;
import com.picone.core.domain.interactors.restaurantsInteractors.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.AddUserToRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.DeleteUserFromRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetAllRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantForKeyInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantForNameInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.UpdateUserChosenRestaurantInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetCurrentUserForEmailInteractor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.CompletableObserver;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RestaurantViewModel extends ViewModel {

    private final static Calendar CALENDAR = Calendar.getInstance();
    private final static int MY_DAY_OF_MONTH = CALENDAR.get(Calendar.DAY_OF_MONTH);
    private final static int MY_MONTH = CALENDAR.get(Calendar.MONTH);
    private final static int MY_YEAR = CALENDAR.get(Calendar.YEAR);

    private static String DATE;

    private MutableLiveData<User> currentUserMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Restaurant> selectedRestaurantMutableLiveData = new MutableLiveData<>();

    private GetAllRestaurantsInteractor getAllRestaurantsInteractor;
    private GetRestaurantInteractor getRestaurant;
    private GetRestaurantForNameInteractor getRestaurantForNameInteractor;
    private AddRestaurantInteractor addRestaurantInteractor;
    private UpdateUserChosenRestaurantInteractor updateUserChosenRestaurantInteractor;
    private GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor;
    private GetRestaurantForKeyInteractor getRestaurantForKeyInteractor;
    private AddUserToRestaurantInteractor addUserToRestaurantInteractor;
    private DeleteUserFromRestaurantInteractor deleteUserFromRestaurantInteractor;


    @ViewModelInject
    public RestaurantViewModel(GetAllRestaurantsInteractor getAllRestaurantsInteractor
            , GetRestaurantInteractor getRestaurant, GetRestaurantForNameInteractor getRestaurantForNameInteractor
            , AddRestaurantInteractor addRestaurantInteractor, UpdateUserChosenRestaurantInteractor updateUserChosenRestaurantInteractor
            , GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor, GetRestaurantForKeyInteractor getRestaurantForKeyInteractor
            , AddUserToRestaurantInteractor addUserToRestaurantInteractor, DeleteUserFromRestaurantInteractor deleteUserFromRestaurantInteractor) {
        this.getAllRestaurantsInteractor = getAllRestaurantsInteractor;
        this.getRestaurant = getRestaurant;
        this.getRestaurantForNameInteractor = getRestaurantForNameInteractor;
        this.addRestaurantInteractor = addRestaurantInteractor;
        this.updateUserChosenRestaurantInteractor = updateUserChosenRestaurantInteractor;
        this.getCurrentUserForEmailInteractor = getCurrentUserForEmailInteractor;
        this.getRestaurantForKeyInteractor = getRestaurantForKeyInteractor;
        this.addUserToRestaurantInteractor = addUserToRestaurantInteractor;
        this.deleteUserFromRestaurantInteractor = deleteUserFromRestaurantInteractor;
        setDate();
    }

    public void setSelectedRestaurant(int position) {
        selectedRestaurantMutableLiveData.setValue(getRestaurant.getRestaurant(position));
    }

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
                            checkIfRestaurantExistAndUpdateReservation(restaurant);
                        }
                    });
        } else {
            checkIfRestaurantExistAndUpdateReservation(restaurant);
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

    //Suppress warning is safe cause subscribe is used to check if restaurant exist in db
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    private void checkIfRestaurantExistAndUpdateReservation(Restaurant restaurant) {
        getRestaurantForNameInteractor.getRestaurantForName(restaurant.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restaurantsForName -> {
                    if (restaurantsForName.isEmpty()) {
                        addRestaurantWhenNotPersisted(restaurant);
                    } else {
                        Log.i("TAG", "onNext: restaurant exist" + restaurantsForName.get(0));
                        updateUserForRestaurant(restaurant, restaurantsForName.get(0).getKey());
                    }
                });
    }

    private void addRestaurantWhenNotPersisted(Restaurant restaurant) {
        restaurant.setRestaurantDailySchedule(new RestaurantDailySchedule(DATE, new ArrayList<>()));
        addRestaurantInteractor.addRestaurant(restaurant)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("TAG", "onComplete: restaurant added");
                        updateUserForRestaurant(restaurant, restaurant.getKey());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void updateUserForRestaurant(Restaurant restaurant, String restaurantKey) {
        addUserToRestaurantInteractor.addCurrentUserToRestaurant(currentUserMutableLiveData.getValue(), restaurant.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) { }
                    //Suppress Warning is safe cause current user can't be null.
                    @SuppressWarnings("ConstantConditions")
                    @Override
                    public void onComplete() {
                        updateUserChosenRestaurantInteractor.updateUserChosenRestaurant
                                (currentUserMutableLiveData.getValue().getEmail(), new UserDailySchedule(DATE, restaurantKey))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(Disposable d) { }

                                    @Override
                                    public void onComplete() {
                                        Log.i("TAG", "onComplete: updateUserComplete");
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

    private void setDate() {
        Date today = new Date();
        try {
            today = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(MY_DAY_OF_MONTH + "/" + MY_MONTH + "/" + MY_YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert today != null;
        DATE = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(today);
    }
}
