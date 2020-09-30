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
import com.picone.core.domain.interactors.restaurantsInteractors.GetAllPersistedRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetAllRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantForKeyInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantForNameInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.UpdateNumberOfInterestedUsersForRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.UpdateUserChosenRestaurantInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetCurrentUserForEmailInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetInterestedUsersForRestaurantKeyInteractor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RestaurantViewModel extends ViewModel {

    private static String DATE;

    private MutableLiveData<User> currentUserMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Restaurant> selectedRestaurantMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> interestedUsersMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> selectedRestaurantKeyMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDataLoadingMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Restaurant>> allRestaurantsMutableLiveData = new MutableLiveData<>();

    private GetAllRestaurantsInteractor getAllRestaurantsInteractor;
    private GetRestaurantInteractor getRestaurant;
    private GetRestaurantForNameInteractor getRestaurantForNameInteractor;
    private AddRestaurantInteractor addRestaurantInteractor;
    private UpdateUserChosenRestaurantInteractor updateUserChosenRestaurantInteractor;
    private GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor;
    private GetRestaurantForKeyInteractor getRestaurantForKeyInteractor;
    private GetInterestedUsersForRestaurantKeyInteractor getInterestedUsersForRestaurantKeyInteractor;
    private UpdateNumberOfInterestedUsersForRestaurantInteractor updateNumberOfInterestedUsersForRestaurantInteractor;
    private GetAllPersistedRestaurantsInteractor getAllPersistedRestaurantsInteractor;

    @ViewModelInject
    public RestaurantViewModel(GetAllRestaurantsInteractor getAllRestaurantsInteractor
            , GetRestaurantInteractor getRestaurant, GetRestaurantForNameInteractor getRestaurantForNameInteractor
            , AddRestaurantInteractor addRestaurantInteractor, UpdateUserChosenRestaurantInteractor updateUserChosenRestaurantInteractor
            , GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor, GetRestaurantForKeyInteractor getRestaurantForKeyInteractor
            , GetInterestedUsersForRestaurantKeyInteractor getInterestedUsersForRestaurantKeyInteractor
            , UpdateNumberOfInterestedUsersForRestaurantInteractor updateNumberOfInterestedUsersForRestaurantInteractor
            , GetAllPersistedRestaurantsInteractor getAllPersistedRestaurantsInteractor) {
        this.getAllRestaurantsInteractor = getAllRestaurantsInteractor;
        this.getRestaurant = getRestaurant;
        this.getRestaurantForNameInteractor = getRestaurantForNameInteractor;
        this.addRestaurantInteractor = addRestaurantInteractor;
        this.updateUserChosenRestaurantInteractor = updateUserChosenRestaurantInteractor;
        this.getCurrentUserForEmailInteractor = getCurrentUserForEmailInteractor;
        this.getRestaurantForKeyInteractor = getRestaurantForKeyInteractor;
        this.getInterestedUsersForRestaurantKeyInteractor = getInterestedUsersForRestaurantKeyInteractor;
        this.updateNumberOfInterestedUsersForRestaurantInteractor = updateNumberOfInterestedUsersForRestaurantInteractor;
        this.getAllPersistedRestaurantsInteractor = getAllPersistedRestaurantsInteractor;
        setDate();
    }

    public LiveData<Boolean> isDataLoading = isDataLoadingMutableLiveData;

    public LiveData<List<User>> getInterestedUsersForRestaurant = interestedUsersMutableLiveData;

    public LiveData<Restaurant> getSelectedRestaurant = selectedRestaurantMutableLiveData;

    public LiveData<List<Restaurant>> getAllRestaurants = allRestaurantsMutableLiveData;


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void setUserChosenRestaurant(String restaurantKey) {
        getRestaurantForKeyInteractor.getRestaurantForKey(restaurantKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(restaurants -> {
                    selectedRestaurantMutableLiveData.setValue(restaurants.get(0));
                    return getInterestedUsersForRestaurantKeyInteractor
                            .getInterestedUsersForRestaurantKey(restaurantKey);
                })
                .subscribe(users -> interestedUsersMutableLiveData.setValue(users));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void initRestaurants(String authUserEmail) {
        allRestaurantsMutableLiveData.setValue(getAllRestaurantsInteractor.getGeneratedRestaurants());
        getCurrentUserForEmailInteractor.getCurrentUserForEmail(authUserEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(currentUsers -> {
                    String restaurantKey;
                    if (!currentUsers.isEmpty() && currentUsers.get(0).getUserDailySchedule() != null)
                        restaurantKey = currentUsers.get(0).getUserDailySchedule().getRestaurantKey();
                    else restaurantKey = "unknown";
                    return getRestaurantForKeyInteractor.getRestaurantForKey(restaurantKey);
                })
                .subscribe(restaurantsForKey -> {
                    if (!restaurantsForKey.isEmpty()) {
                        Restaurant chosenRestaurant = restaurantsForKey.get(0);
                        selectedRestaurantMutableLiveData.setValue(chosenRestaurant);
                    }
                    updateRestaurant();
                });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void initUsers(String authUserEmail) {
        getCurrentUserForEmailInteractor.getCurrentUserForEmail(authUserEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(currentUsers -> {
                    currentUserMutableLiveData.setValue(currentUsers.get(0));
                    if (!currentUsers.isEmpty() && currentUsers.get(0).getUserDailySchedule() != null)
                        return getInterestedUsersForRestaurantKeyInteractor
                                .getInterestedUsersForRestaurantKey
                                        (currentUsers.get(0).getUserDailySchedule().getRestaurantKey());
                    else
                        return getInterestedUsersForRestaurantKeyInteractor
                                .getInterestedUsersForRestaurantKey("unknown");
                })
                .subscribe(usersForRestaurant -> interestedUsersMutableLiveData.setValue(usersForRestaurant));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public <T> void initSelectedRestaurant(T param) {
        Restaurant selectedRestaurant = new Restaurant();
        String selectedRestaurantName = "";
        if (param instanceof Integer) {
            selectedRestaurant = getRestaurant.getRestaurant((Integer) param);
            selectedRestaurantName = selectedRestaurant.getName();
            selectedRestaurantMutableLiveData.setValue(selectedRestaurant);
        }
        if (param instanceof String) {
            selectedRestaurantName = (String) param;
            for (Restaurant restaurant : Objects.requireNonNull(allRestaurantsMutableLiveData.getValue()))
                if (restaurant.getName().equals(param)) selectedRestaurant = restaurant;
            selectedRestaurantMutableLiveData.setValue(selectedRestaurant);
        }

        getRestaurantForNameInteractor.getRestaurantForName(selectedRestaurantName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .switchIfEmpty(addRestaurantInteractor.addRestaurant(selectedRestaurant)
                        .andThen(getRestaurantForNameInteractor.getRestaurantForName(selectedRestaurantName)))
                .flatMap(restaurantForName -> {
                    selectedRestaurantKeyMutableLiveData.setValue(restaurantForName.getKey());
                    return getInterestedUsersForRestaurantKeyInteractor.getInterestedUsersForRestaurantKey(restaurantForName.getKey());
                })
                .subscribe(usersForRestaurant -> interestedUsersMutableLiveData.setValue(usersForRestaurant));
    }


    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    @SuppressLint("CheckResult")
    public void setUserToRestaurant() {
        isDataLoadingMutableLiveData.setValue(true);
        if (currentUserMutableLiveData.getValue().getUserDailySchedule() == null)
            updateUserChosenRestaurant();
        else
            getRestaurantForKeyInteractor.getRestaurantForKey(currentUserMutableLiveData.getValue().getUserDailySchedule().getRestaurantKey())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMapCompletable(restaurantsForKey -> {
                        int interestedUsers = restaurantsForKey.get(0).getNumberOfInterestedUsers() - 1;
                        return updateNumberOfInterestedUsersForRestaurantInteractor
                                .updateNumberOfInterestedUsersForRestaurant(restaurantsForKey.get(0).getName(), interestedUsers);
                    })
                    .subscribe(this::updateUserChosenRestaurant);
    }

    //currentUserMutableLiveData.getValue().getEmail() can't be null cause already set in restaurant list fragment
    @SuppressLint("CheckResult")
    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    public void updateUserChosenRestaurant() {
        User user = currentUserMutableLiveData.getValue();
        user.setUserDailySchedule(new UserDailySchedule(DATE
                , selectedRestaurantKeyMutableLiveData.getValue()
                , selectedRestaurantMutableLiveData.getValue().getName()));
        updateUserChosenRestaurantInteractor.updateUserChosenRestaurant(user)
                .doFinally(() -> isDataLoadingMutableLiveData.setValue(false))
                .andThen(getInterestedUsersForRestaurantKeyInteractor
                        .getInterestedUsersForRestaurantKey(selectedRestaurantKeyMutableLiveData.getValue()))
                .flatMapCompletable(usersForRestaurant -> {
                    interestedUsersMutableLiveData.setValue(usersForRestaurant);
                    return updateNumberOfInterestedUsersForRestaurantInteractor
                            .updateNumberOfInterestedUsersForRestaurant(selectedRestaurantMutableLiveData.getValue().getName(), usersForRestaurant.size());
                })
                .subscribe(() -> {
                }, throwable -> {
                });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    private void updateRestaurant() {
        List<Restaurant> updatedRestaurants = new ArrayList<>();
        getAllPersistedRestaurantsInteractor.getAllPersistedRestaurants()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(persistedRestaurants -> {
                    for (Restaurant persistedRestaurant : persistedRestaurants) {
                        for (Restaurant generatedRestaurant : getAllRestaurantsInteractor.getGeneratedRestaurants()) {
                            if (persistedRestaurant.getName().equals(generatedRestaurant.getName())) {
                                generatedRestaurant.setNumberOfInterestedUsers(persistedRestaurant.getNumberOfInterestedUsers());
                            }
                            if (!updatedRestaurants.contains(generatedRestaurant))
                                updatedRestaurants.add(generatedRestaurant);
                        }
                    }
                    allRestaurantsMutableLiveData.setValue(updatedRestaurants);
                });
    }

    private void setDate() {
        Date today = Calendar.getInstance().getTime();
        DATE = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(today);
    }
}