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
import com.picone.core.domain.interactors.restaurantsInteractors.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetAllPersistedRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetFanListForRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantForKeyInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantForNameInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.placeInteractors.FetchRestaurantDetailFromPlaceInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.placeInteractors.FetchRestaurantDistanceInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.placeInteractors.FetchRestaurantFromPlaceInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.UpdateFanListForRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.UpdateNumberOfInterestedUsersForRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.UpdateUserChosenRestaurantInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetCurrentUserForEmailInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetInterestedUsersForRestaurantKeyInteractor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.picone.go4lunch.presentation.ui.fragment.MapsFragment.MAPS_KEY;

public class RestaurantViewModel extends ViewModel {

    private static String DATE;

    private MutableLiveData<User> currentUserMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Restaurant> selectedRestaurantMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> interestedUsersMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> selectedRestaurantKeyMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDataLoadingMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Restaurant>> allRestaurantsMutableLiveData = new MutableLiveData<>();

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

    @ViewModelInject
    public RestaurantViewModel(GetRestaurantForNameInteractor getRestaurantForNameInteractor
            , AddRestaurantInteractor addRestaurantInteractor, UpdateUserChosenRestaurantInteractor updateUserChosenRestaurantInteractor
            , GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor, GetRestaurantForKeyInteractor getRestaurantForKeyInteractor
            , GetInterestedUsersForRestaurantKeyInteractor getInterestedUsersForRestaurantKeyInteractor
            , UpdateNumberOfInterestedUsersForRestaurantInteractor updateNumberOfInterestedUsersForRestaurantInteractor
            , FetchRestaurantFromPlaceInteractor fetchRestaurantFromPlaceInteractor
            , GetAllPersistedRestaurantsInteractor getAllPersistedRestaurantsInteractor
            , UpdateFanListForRestaurantInteractor updateFanListForRestaurantInteractor, GetFanListForRestaurantInteractor getFanListForRestaurantInteractor
    ,FetchRestaurantDistanceInteractor fetchRestaurantDistanceInteractor, FetchRestaurantDetailFromPlaceInteractor fetchRestaurantDetailFromPlaceInteractor) {
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
        this.fetchRestaurantDetailFromPlaceInteractor=fetchRestaurantDetailFromPlaceInteractor;
        this.fetchRestaurantDistanceInteractor = fetchRestaurantDistanceInteractor;
        DATE = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(Calendar.getInstance().getTime());
    }

    public LiveData<Boolean> isDataLoading = isDataLoadingMutableLiveData;

    public LiveData<List<User>> getInterestedUsersForRestaurant = interestedUsersMutableLiveData;

    public LiveData<Restaurant> getSelectedRestaurant = selectedRestaurantMutableLiveData;

    public LiveData<List<Restaurant>> getAllRestaurants = allRestaurantsMutableLiveData;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void getRestaurantFromMaps(Location mCurrentLocation) {
        Log.i("TAG", "getRestaurantFromMaps: "+MAPS_KEY);
        fetchRestaurantFromPlaceInteractor.fetchRestaurantFromPlace(mCurrentLocation, MAPS_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(restaurantPOJOS ->
                        fetchRestaurantDetailFromPlaceInteractor.getRestaurantDetail(restaurantPOJOS,MAPS_KEY))
                .subscribe(restaurants -> {
                    if (allRestaurantsMutableLiveData.getValue() == null)
                        allRestaurantsMutableLiveData.setValue(restaurants);
                });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void setRestaurantDistance(Location mCurrentLocation){
        fetchRestaurantDistanceInteractor.getRestaurantDistance(allRestaurantsMutableLiveData.getValue(),mCurrentLocation,MAPS_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restaurants -> {
                    Log.i("restaurantDistance", "setRestaurantDistance: "+restaurants);
                });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void updateRestaurantForKey(String restaurantKey) {
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
                .flatMapCompletable(strings -> {
                    if (!strings.contains(currentUser.getUid()))
                        strings.add(currentUser.getUid());
                    return updateFanListForRestaurantInteractor.updateFanListForRestaurant(restaurant.getName(), strings);
                })
                .andThen(getRestaurantForNameInteractor.getRestaurantForName(restaurant.getName()))
                .subscribe(persistedRestaurant -> selectedRestaurantMutableLiveData.setValue(persistedRestaurant));
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void initRestaurants(String authUserEmail) {
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
                    if (!restaurantsForKey.isEmpty())
                        selectedRestaurantMutableLiveData.setValue(restaurantsForKey.get(0));
                    updateAllRestaurantsWithPersistedValues();
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
                    resetDbOnDailyScheduleDatePassed(currentUsers.get(0));
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

    public void initData(String authUserMail) {
        initRestaurants(authUserMail);
        initUsers(authUserMail);
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
                    updateRestaurantForKey(restaurantForName.getKey());
                    selectedRestaurantKeyMutableLiveData.setValue(restaurantForName.getKey());
                    return getInterestedUsersForRestaurantKeyInteractor.getInterestedUsersForRestaurantKey(restaurantForName.getKey());
                })
                .subscribe(usersForRestaurant -> interestedUsersMutableLiveData.setValue(usersForRestaurant));
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
    private void updateAllRestaurantsWithPersistedValues() {
        List<Restaurant> updatedRestaurants = new ArrayList<>();
        getAllPersistedRestaurantsInteractor.getAllPersistedRestaurants()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(persistedRestaurants -> {
                    for (Restaurant persistedRestaurant : persistedRestaurants) {
                        for (Restaurant generatedRestaurant : allRestaurantsMutableLiveData.getValue()) {
                            if (persistedRestaurant.getName().equals(generatedRestaurant.getName())) {
                                generatedRestaurant.setNumberOfInterestedUsers(persistedRestaurant.getNumberOfInterestedUsers());
                                generatedRestaurant.setFanList(persistedRestaurant.getFanList());
                            }
                            if (!updatedRestaurants.contains(generatedRestaurant))
                                updatedRestaurants.add(generatedRestaurant);
                        }
                    }
                    allRestaurantsMutableLiveData.setValue(updatedRestaurants);
                });
    }

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

    private <T> Restaurant getRestaurantOnUserClick(T param) {
        Restaurant selectedRestaurant = new Restaurant();

        if (param instanceof Integer) {
            selectedRestaurant = allRestaurantsMutableLiveData.getValue().get((Integer) param);
            selectedRestaurantMutableLiveData.setValue(selectedRestaurant);
        }
        if (param instanceof String) {
            for (Restaurant restaurant : Objects.requireNonNull(allRestaurantsMutableLiveData.getValue()))
                if (restaurant.getName().equals(param)) selectedRestaurant = restaurant;
            selectedRestaurantMutableLiveData.setValue(selectedRestaurant);
        }

        if (!(param instanceof String))
            if (!(param instanceof Integer))
                Log.e("WRONG_PARAMETER", "initSelectedRestaurant: Must pass a string restaurantName or an int restaurantPosition ", new Throwable());

        return selectedRestaurant;
    }
}