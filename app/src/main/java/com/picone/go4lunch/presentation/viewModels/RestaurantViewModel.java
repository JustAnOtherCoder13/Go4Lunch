package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
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
    public void initSelectedRestaurant(int position) {
        Restaurant selectedRestaurant = getRestaurant.getRestaurant(position);
        selectedRestaurantMutableLiveData.setValue(selectedRestaurant);
        getRestaurantForNameInteractor.getRestaurantForName(selectedRestaurant.getName())
                .flatMap((Function<List<Restaurant>, ObservableSource<List<User>>>)
                        restaurants -> {
                            String restaurantKey;
                            if (!restaurants.isEmpty()) {
                                restaurantKey = restaurants.get(0).getKey();
                                selectedRestaurantKeyMutableLiveData.setValue(restaurantKey);
                            } else {
                                restaurantKey = "unknown";
                                addRestaurantInteractor.addRestaurant(selectedRestaurant)
                                        .doOnError(throwable -> { })
                                        .subscribe();
                            }
                            return getInterestedUsersForRestaurantKeyInteractor.getInterestedUsersForRestaurantKey(restaurantKey);
                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(interestedUsers -> interestedUsersMutableLiveData.setValue(interestedUsers));
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
    public void addRestaurant() {
        if (currentUserMutableLiveData.getValue().getUserDailySchedule() == null)
            updateUserChosenRestaurant();
        else {
            getRestaurantForKeyInteractor.getRestaurantForKey(currentUserMutableLiveData.getValue().getUserDailySchedule().getRestaurantKey())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(restaurantsForKey -> {
                        Restaurant originalChosenRestaurant = restaurantsForKey.get(0);
                        if (!originalChosenRestaurant.getName().equals(selectedRestaurantMutableLiveData.getValue().getName()))
                            updateUserChosenRestaurant();

                    });
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

    //currentUserMutableLiveData.getValue().getEmail() can't be null cause already set in restaurant list fragment
    @SuppressWarnings("ConstantConditions")
    private void updateUserChosenRestaurant() {
        User user = currentUserMutableLiveData.getValue();
        user.setUserDailySchedule(new UserDailySchedule(DATE, selectedRestaurantKeyMutableLiveData.getValue()));
        updateUserChosenRestaurantInteractor.updateUserChosenRestaurant(user)
                .doOnError(throwable -> { })
                .subscribe();
    }

    private void setDate() {
        Date today = Calendar.getInstance().getTime();
        DATE = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(today);
    }
}
