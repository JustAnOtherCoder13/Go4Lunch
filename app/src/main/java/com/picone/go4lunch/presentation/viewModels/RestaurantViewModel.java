package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.entity.UserDailySchedule;
import com.picone.core.domain.entity.predictionPOJO.Prediction;
import com.picone.core.domain.interactors.restaurant.placeInteractors.FetchRestaurantDetailFromPlaceInteractor;
import com.picone.core.domain.interactors.restaurant.placeInteractors.FetchRestaurantDistanceInteractor;
import com.picone.core.domain.interactors.restaurant.placeInteractors.FetchRestaurantFromPlaceInteractor;
import com.picone.core.domain.interactors.restaurant.placeInteractors.GetPredictionInteractor;
import com.picone.core.domain.interactors.restaurant.restaurantDetailInteractors.GetFanListForRestaurantInteractor;
import com.picone.core.domain.interactors.restaurant.restaurantDetailInteractors.UpdateFanListForRestaurantInteractor;
import com.picone.core.domain.interactors.restaurant.restaurantDetailInteractors.UpdateNumberOfInterestedUsersForRestaurantInteractor;
import com.picone.core.domain.interactors.restaurant.restaurantDetailInteractors.UpdateUserChosenRestaurantInteractor;
import com.picone.core.domain.interactors.restaurant.restaurantInteractors.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurant.restaurantInteractors.GetAllPersistedRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurant.restaurantInteractors.GetRestaurantForKeyInteractor;
import com.picone.core.domain.interactors.restaurant.restaurantInteractors.GetRestaurantForNameInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetCurrentUserForEmailInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetInterestedUsersForRestaurantKeyInteractor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.picone.go4lunch.presentation.ui.fragment.MapsFragment.MAPS_KEY;

public class RestaurantViewModel extends ViewModel {

    private static String DATE;

    private MutableLiveData<User> currentUserMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Restaurant> selectedRestaurantMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> interestedUsersMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDataLoadingMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Restaurant>> allRestaurantsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Restaurant>> filteredRestaurantMutableLveData = new MutableLiveData<>();


    private GetRestaurantForNameInteractor getRestaurantForNameInteractor;
    private AddRestaurantInteractor addRestaurantInteractor;
    private UpdateUserChosenRestaurantInteractor updateUserChosenRestaurantInteractor;
    private GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor;
    private GetRestaurantForKeyInteractor getRestaurantForKeyInteractor;
    private GetInterestedUsersForRestaurantKeyInteractor getInterestedUsersForRestaurantKeyInteractor;
    private UpdateNumberOfInterestedUsersForRestaurantInteractor updateNumberOfInterestedUsersForRestaurantInteractor;
    private GetAllPersistedRestaurantsInteractor getAllPersistedRestaurantsInteractor;
    private FetchRestaurantFromPlaceInteractor fetchRestaurantFromPlaceInteractor;
    private FetchRestaurantDistanceInteractor fetchRestaurantDistanceInteractor;
    private FetchRestaurantDetailFromPlaceInteractor fetchRestaurantDetailFromPlaceInteractor;
    private UpdateFanListForRestaurantInteractor updateFanListForRestaurantInteractor;
    private GetFanListForRestaurantInteractor getFanListForRestaurantInteractor;
    private GetPredictionInteractor getPredictionInteractor;

    @ViewModelInject
    public RestaurantViewModel(GetRestaurantForNameInteractor getRestaurantForNameInteractor
            , AddRestaurantInteractor addRestaurantInteractor, UpdateUserChosenRestaurantInteractor updateUserChosenRestaurantInteractor
            , GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor, GetRestaurantForKeyInteractor getRestaurantForKeyInteractor
            , GetInterestedUsersForRestaurantKeyInteractor getInterestedUsersForRestaurantKeyInteractor
            , UpdateNumberOfInterestedUsersForRestaurantInteractor updateNumberOfInterestedUsersForRestaurantInteractor
            , FetchRestaurantFromPlaceInteractor fetchRestaurantFromPlaceInteractor
            , GetAllPersistedRestaurantsInteractor getAllPersistedRestaurantsInteractor
            , UpdateFanListForRestaurantInteractor updateFanListForRestaurantInteractor, GetFanListForRestaurantInteractor getFanListForRestaurantInteractor
            , FetchRestaurantDistanceInteractor fetchRestaurantDistanceInteractor, FetchRestaurantDetailFromPlaceInteractor fetchRestaurantDetailFromPlaceInteractor
            , GetPredictionInteractor getPredictionInteractor) {
        this.getRestaurantForNameInteractor = getRestaurantForNameInteractor;
        this.addRestaurantInteractor = addRestaurantInteractor;
        this.updateUserChosenRestaurantInteractor = updateUserChosenRestaurantInteractor;
        this.getCurrentUserForEmailInteractor = getCurrentUserForEmailInteractor;
        this.getRestaurantForKeyInteractor = getRestaurantForKeyInteractor;
        this.getInterestedUsersForRestaurantKeyInteractor = getInterestedUsersForRestaurantKeyInteractor;
        this.updateNumberOfInterestedUsersForRestaurantInteractor = updateNumberOfInterestedUsersForRestaurantInteractor;
        this.getAllPersistedRestaurantsInteractor = getAllPersistedRestaurantsInteractor;
        this.fetchRestaurantFromPlaceInteractor = fetchRestaurantFromPlaceInteractor;
        this.getFanListForRestaurantInteractor = getFanListForRestaurantInteractor;
        this.updateFanListForRestaurantInteractor = updateFanListForRestaurantInteractor;
        this.fetchRestaurantDetailFromPlaceInteractor = fetchRestaurantDetailFromPlaceInteractor;
        this.fetchRestaurantDistanceInteractor = fetchRestaurantDistanceInteractor;
        this.getPredictionInteractor = getPredictionInteractor;
        DATE = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(Calendar.getInstance().getTime());
    }

    public LiveData<Boolean> isDataLoading = isDataLoadingMutableLiveData;

    public LiveData<List<User>> getInterestedUsersForRestaurant = interestedUsersMutableLiveData;

    public LiveData<Restaurant> getSelectedRestaurant = selectedRestaurantMutableLiveData;

    public LiveData<List<Restaurant>> getAllRestaurants = allRestaurantsMutableLiveData;


    public void setLocationMutableLiveData(Location location) {
        locationMutableLiveData.setValue(location);
    }

    public void resetFilteredRestaurant() {
        filteredRestaurantMutableLveData.setValue(null);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void setCurrentUser(String authUserEmail) {
        getCurrentUserForEmailInteractor.getCurrentUserForEmail(authUserEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currentUsers -> {
                    currentUserMutableLiveData.setValue(currentUsers.get(0));
                    resetDbOnDailyScheduleDatePassed(currentUsers.get(0));
                });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void getRestaurantFromMaps() {
        Location mCurrentLocation = locationMutableLiveData.getValue();
        locationMutableLiveData.setValue(mCurrentLocation);
        fetchRestaurantFromPlaceInteractor.fetchRestaurantFromPlace(mCurrentLocation, MAPS_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(allRestaurants -> {
                    Log.i("TAG", "getRestaurantFromMaps: " + allRestaurants);
                    for (Restaurant restaurant : allRestaurants) {
                        fetchRestaurantDetailFromPlaceInteractor.getRestaurantDetail(restaurant, MAPS_KEY);
                        fetchRestaurantDistanceInteractor.getRestaurantDistance(restaurant, mCurrentLocation, MAPS_KEY);
                    }
                    return updateAllRestaurantsWithPersistedValues(allRestaurants);
                })
                .subscribe(allRestaurants -> {
                    if (filteredRestaurantMutableLveData.getValue() == null || allRestaurantsMutableLiveData.getValue() == null)
                        allRestaurantsMutableLiveData.setValue(allRestaurants);
                });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public <T> void initSelectedRestaurant(T param) {
        Restaurant selectedRestaurant = getRestaurantOnUserClick(param);
        getRestaurantForNameInteractor.getRestaurantForName(selectedRestaurant.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .switchIfEmpty(addRestaurantInteractor.addRestaurant(selectedRestaurant)
                        .andThen(getRestaurantForNameInteractor.getRestaurantForName(selectedRestaurant.getName())))
                .flatMap(restaurantForName -> {
                    selectedRestaurantMutableLiveData.setValue(restaurantForName);
                    return getInterestedUsersForRestaurantKeyInteractor.getInterestedUsersForRestaurantKey(restaurantForName.getKey());
                })
                .subscribe(usersForRestaurant -> interestedUsersMutableLiveData.setValue(usersForRestaurant));
    }

    @SuppressLint("CheckResult")
    private Observable<List<Restaurant>> updateAllRestaurantsWithPersistedValues(List<Restaurant> allRestaurants) {

        return getAllPersistedRestaurantsInteractor.getAllPersistedRestaurants()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(persistedRestaurants -> {
                    Log.i("TAG", "updateAllRestaurantsWithPersistedValues: " + allRestaurants);
                    for (Restaurant persistedRestaurant : persistedRestaurants) {
                        for (Restaurant restaurantFromMap : allRestaurants) {
                            if (persistedRestaurant.getPlaceId().equals(restaurantFromMap.getPlaceId())) {
                                restaurantFromMap.setNumberOfInterestedUsers(persistedRestaurant.getNumberOfInterestedUsers());
                                restaurantFromMap.setFanList(persistedRestaurant.getFanList());
                            }
                        }
                    }
                    return Observable.create(emitter -> {
                        emitter.onNext(allRestaurants);
                    });
                });
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
    @SuppressLint("CheckResult")
    public void getPrediction(String query) {
        Location location = locationMutableLiveData.getValue();
        List<Restaurant> filteredRestaurant = new ArrayList<>();
        getPredictionInteractor.getPredictions(query, MAPS_KEY, String.valueOf(location.getLatitude()).concat(",").concat(String.valueOf(location.getLongitude())))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(predictionResponse -> {
                    for (Prediction response : predictionResponse.getPredictions()) {
                        for (Restaurant restaurant : allRestaurantsMutableLiveData.getValue()) {
                            if (restaurant.getPlaceId().equals(response.getPlaceId()))
                                filteredRestaurant.add(restaurant);
                        }
                    }
                    filteredRestaurantMutableLveData.setValue(filteredRestaurant);
                    allRestaurantsMutableLiveData.setValue(filteredRestaurant);
                });
    }

    //TODO fan list don't update on like.
    @SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
    @SuppressLint("CheckResult")
    public void updateFanList() {
        Restaurant restaurant = selectedRestaurantMutableLiveData.getValue();
        User currentUser = currentUserMutableLiveData.getValue();
        List<String> fanList = new ArrayList<>();
        fanList.add(currentUser.getUid());
        getFanListForRestaurantInteractor.getFanListForRestaurant(restaurant.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .switchIfEmpty(updateFanListForRestaurantInteractor.updateFanListForRestaurant(restaurant.getName(), fanList)
                        .andThen(getFanListForRestaurantInteractor.getFanListForRestaurant(restaurant.getName())))
                .subscribe(persistedFanList -> {
                    if (!persistedFanList.contains(currentUser.getUid()))
                        persistedFanList.add(currentUser.getUid());
                });
    }

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    @SuppressLint("CheckResult")
    public void addUserToRestaurant() {
        isDataLoadingMutableLiveData.setValue(true);
        if (currentUserMutableLiveData.getValue().getUserDailySchedule() == null)
            updateUserDailySchedule();
        else
            getRestaurantForKeyInteractor.getRestaurantForKey(currentUserMutableLiveData.getValue().getUserDailySchedule().getRestaurantKey())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMapCompletable(restaurantsForKey -> {
                        Log.i("TAG", "addUserToRestaurant: " + restaurantsForKey.get(0).getName());
                        int interestedUsers = restaurantsForKey.get(0).getNumberOfInterestedUsers() - 1;
                        return updateNumberOfInterestedUsersForRestaurantInteractor
                                .updateNumberOfInterestedUsersForRestaurant(restaurantsForKey.get(0).getName(), interestedUsers);
                    })
                    .subscribe(this::updateUserDailySchedule);
    }

    //currentUserMutableLiveData.getValue().getEmail() can't be null cause already set in restaurant list fragment
    @SuppressLint("CheckResult")
    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    private void updateUserDailySchedule() {
        User user = currentUserMutableLiveData.getValue();
        user.setUserDailySchedule(new UserDailySchedule(DATE
                , selectedRestaurantMutableLiveData.getValue().getKey()
                , selectedRestaurantMutableLiveData.getValue().getName()));

        updateUserChosenRestaurantInteractor.updateUserChosenRestaurant(user)
                .doFinally(() -> isDataLoadingMutableLiveData.setValue(false))
                .andThen(getInterestedUsersForRestaurantKeyInteractor
                        .getInterestedUsersForRestaurantKey(selectedRestaurantMutableLiveData.getValue().getKey()))
                .flatMapCompletable(usersForRestaurant -> {
                    interestedUsersMutableLiveData.setValue(usersForRestaurant);
                    return updateNumberOfInterestedUsersForRestaurantInteractor
                            .updateNumberOfInterestedUsersForRestaurant(selectedRestaurantMutableLiveData.getValue().getName(), usersForRestaurant.size());
                })
                .subscribe(() -> {
                }, throwable -> {
                });
    }

    //TODO check all users dailySchedule
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    private void resetDbOnDailyScheduleDatePassed(User currentUser) {
        Date dailyScheduleDate = new Date();
        Date today = new Date();
        if (currentUser.getUserDailySchedule() != null) {
            try {
                dailyScheduleDate = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(currentUser.getUserDailySchedule().getDate());
                today = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(DATE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            assert dailyScheduleDate != null;
            if (dailyScheduleDate.compareTo(today) < 0) {
                getRestaurantForKeyInteractor.getRestaurantForKey(currentUser.getUserDailySchedule().getRestaurantKey())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMapCompletable(restaurantsForKey -> {
                            int interestedUsers = restaurantsForKey.get(0).getNumberOfInterestedUsers() - 1;
                            currentUser.setUserDailySchedule(new UserDailySchedule());
                            return updateNumberOfInterestedUsersForRestaurantInteractor
                                    .updateNumberOfInterestedUsersForRestaurant(restaurantsForKey.get(0).getName(), interestedUsers);
                        })
                        .andThen(updateUserChosenRestaurantInteractor.updateUserChosenRestaurant(currentUser))
                        .subscribe(() -> {
                        }, throwable -> {
                        });
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private <T> Restaurant getRestaurantOnUserClick(T param) {
        Restaurant selectedRestaurant = new Restaurant();

        if (param instanceof Integer) {
            selectedRestaurant = allRestaurantsMutableLiveData.getValue().get((Integer) param);
        }
        if (param instanceof String) {
            for (Restaurant restaurant : allRestaurantsMutableLiveData.getValue())
                if (restaurant.getName().equals(param)) selectedRestaurant = restaurant;
        }
        if (!(param instanceof String))
            if (!(param instanceof Integer))
                Log.e("WRONG_PARAMETER", "initSelectedRestaurant: Must pass a string restaurantName or an int restaurantPosition ", new Throwable());

        return selectedRestaurant;
    }
}