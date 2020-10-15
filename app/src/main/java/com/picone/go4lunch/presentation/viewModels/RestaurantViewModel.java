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
import com.picone.core.domain.interactors.restaurant.restaurantInteractors.GetRestaurantFromFirebaseInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetCurrentUserForEmailInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetInterestedUsersForRestaurantKeyInteractor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
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


    private AddRestaurantInteractor addRestaurantInteractor;
    private UpdateUserChosenRestaurantInteractor updateUserChosenRestaurantInteractor;
    private GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor;
    private GetInterestedUsersForRestaurantKeyInteractor getInterestedUsersForRestaurantKeyInteractor;
    private UpdateNumberOfInterestedUsersForRestaurantInteractor updateNumberOfInterestedUsersForRestaurantInteractor;
    private GetAllPersistedRestaurantsInteractor getAllPersistedRestaurantsInteractor;
    private FetchRestaurantFromPlaceInteractor fetchRestaurantFromPlaceInteractor;
    private FetchRestaurantDistanceInteractor fetchRestaurantDistanceInteractor;
    private FetchRestaurantDetailFromPlaceInteractor fetchRestaurantDetailFromPlaceInteractor;
    private UpdateFanListForRestaurantInteractor updateFanListForRestaurantInteractor;
    private GetFanListForRestaurantInteractor getFanListForRestaurantInteractor;
    private GetPredictionInteractor getPredictionInteractor;
    private GetRestaurantFromFirebaseInteractor getRestaurantFromFirebaseInteractor;

    @ViewModelInject
    public RestaurantViewModel(AddRestaurantInteractor addRestaurantInteractor, UpdateUserChosenRestaurantInteractor updateUserChosenRestaurantInteractor
            , GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor
            , GetInterestedUsersForRestaurantKeyInteractor getInterestedUsersForRestaurantKeyInteractor
            , UpdateNumberOfInterestedUsersForRestaurantInteractor updateNumberOfInterestedUsersForRestaurantInteractor
            , FetchRestaurantFromPlaceInteractor fetchRestaurantFromPlaceInteractor, GetRestaurantFromFirebaseInteractor getRestaurantFromFirebaseInteractor
            , GetAllPersistedRestaurantsInteractor getAllPersistedRestaurantsInteractor
            , UpdateFanListForRestaurantInteractor updateFanListForRestaurantInteractor, GetFanListForRestaurantInteractor getFanListForRestaurantInteractor
            , FetchRestaurantDistanceInteractor fetchRestaurantDistanceInteractor, FetchRestaurantDetailFromPlaceInteractor fetchRestaurantDetailFromPlaceInteractor
            , GetPredictionInteractor getPredictionInteractor) {
        this.addRestaurantInteractor = addRestaurantInteractor;
        this.updateUserChosenRestaurantInteractor = updateUserChosenRestaurantInteractor;
        this.getCurrentUserForEmailInteractor = getCurrentUserForEmailInteractor;
        this.getInterestedUsersForRestaurantKeyInteractor = getInterestedUsersForRestaurantKeyInteractor;
        this.updateNumberOfInterestedUsersForRestaurantInteractor = updateNumberOfInterestedUsersForRestaurantInteractor;
        this.getAllPersistedRestaurantsInteractor = getAllPersistedRestaurantsInteractor;
        this.fetchRestaurantFromPlaceInteractor = fetchRestaurantFromPlaceInteractor;
        this.getFanListForRestaurantInteractor = getFanListForRestaurantInteractor;
        this.updateFanListForRestaurantInteractor = updateFanListForRestaurantInteractor;
        this.fetchRestaurantDetailFromPlaceInteractor = fetchRestaurantDetailFromPlaceInteractor;
        this.fetchRestaurantDistanceInteractor = fetchRestaurantDistanceInteractor;
        this.getPredictionInteractor = getPredictionInteractor;
        this.getRestaurantFromFirebaseInteractor= getRestaurantFromFirebaseInteractor;
        DATE = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(Calendar.getInstance().getTime());
    }

    public LiveData<Boolean> isDataLoading = isDataLoadingMutableLiveData;
    public LiveData<List<User>> getInterestedUsersForRestaurant = interestedUsersMutableLiveData;
    public LiveData<Restaurant> getSelectedRestaurant = selectedRestaurantMutableLiveData;
    public LiveData<List<Restaurant>> getAllRestaurants = allRestaurantsMutableLiveData;
    public LiveData<User> getCurrentUser = currentUserMutableLiveData;
    public LiveData<Location> getCurrentLocation = locationMutableLiveData;

    public void setCurrentLocation(Location location) {
        locationMutableLiveData.setValue(location);
    }

    public void resetSelectedRestaurant() {
        selectedRestaurantMutableLiveData.setValue(null);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void setCurrentUser(String authUserEmail) {
        getCurrentUserForEmailInteractor.getCurrentUserForEmail(authUserEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currentUsers ->
                        currentUserMutableLiveData.setValue(currentUsers.get(0)));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void getRestaurantFromMaps() {
        fetchRestaurantFromPlaceInteractor.fetchRestaurantFromPlace_(locationMutableLiveData.getValue(), MAPS_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(this::fetchPlaceDetail)
                .flatMap(this::updateAllRestaurantsWithPersistedValues)
                .subscribe(restaurants ->
                        allRestaurantsMutableLiveData.setValue(restaurants));
    }

    @SuppressLint("CheckResult")
    private Observable<Restaurant> fetchPlaceDistance(Location mCurrentLocation, Restaurant restaurant) {
        return fetchRestaurantDistanceInteractor.getRestaurantDistance_(restaurant, mCurrentLocation, MAPS_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(restaurant1 -> Observable.create(emitter -> emitter.onNext(restaurant1)));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private Observable<List<Restaurant>> fetchPlaceDetail(List<Restaurant> restaurantsFromMap) {
        return Observable.create(emitter -> {
            for (Restaurant restaurantFromMap : restaurantsFromMap)
                fetchRestaurantDetailFromPlaceInteractor.getRestaurantDetail(restaurantFromMap, MAPS_KEY)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(restaurant1 -> fetchPlaceDistance(locationMutableLiveData.getValue(), restaurantFromMap))
                        .flatMap(restaurant -> Observable.create((ObservableOnSubscribe<List<Restaurant>>)
                                emitter1 -> emitter1.onNext(restaurantsFromMap)))
                        .subscribe(emitter::onNext);
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public <T> void initSelectedRestaurant(T param) {
        Restaurant selectedRestaurant = getRestaurantOnUserClick(param);
        getRestaurantFromFirebaseInteractor.getRestaurantFromFirebase(selectedRestaurant.getPlaceId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .switchIfEmpty(addRestaurantInteractor.addRestaurant(selectedRestaurant)
                        .andThen(getRestaurantFromFirebaseInteractor.getRestaurantFromFirebase(selectedRestaurant.getPlaceId())))
                .flatMap(restaurantForName -> {
                    selectedRestaurantMutableLiveData.setValue(restaurantForName);
                    return getInterestedUsersForRestaurantKeyInteractor.getInterestedUsersForRestaurantKey(restaurantForName.getPlaceId());
                })
                .subscribe(usersForRestaurant -> interestedUsersMutableLiveData.setValue(usersForRestaurant));
    }

    @SuppressLint("CheckResult")
    private Observable<List<Restaurant>> updateAllRestaurantsWithPersistedValues(List<Restaurant> allRestaurantsFromMap) {
        return getAllPersistedRestaurantsInteractor.getAllPersistedRestaurants()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(persistedRestaurants -> Observable.create(emitter -> {
                    for (Restaurant persistedRestaurant : persistedRestaurants) {
                        for (Restaurant restaurantFromMap : allRestaurantsFromMap) {
                            if (persistedRestaurant.getPlaceId().equals(restaurantFromMap.getPlaceId())) {
                                restaurantFromMap.setNumberOfInterestedUsers(persistedRestaurant.getNumberOfInterestedUsers());
                                restaurantFromMap.setFanList(persistedRestaurant.getFanList());
                            }
                        }
                    }
                    emitter.onNext(allRestaurantsFromMap);
                }));
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
                    allRestaurantsMutableLiveData.setValue(filteredRestaurant);
                });
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
                .flatMapCompletable(persistedFanList -> {
                    if (!persistedFanList.contains(currentUser.getUid())){
                        persistedFanList.add(currentUser.getUid());
                        selectedRestaurantMutableLiveData.getValue().setFanList(persistedFanList);}
                    //TODO if restaurantMutable not set, fanList don't update if set create a loop cause restaurant detail get restaurantMutable values
                    //selectedRestaurantMutableLiveData.setValue(restaurant);
                    return updateFanListForRestaurantInteractor.updateFanListForRestaurant(restaurant.getName(), persistedFanList);
                })
                .subscribe(() -> {
                }, throwable -> {
                });
    }

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    @SuppressLint("CheckResult")
    public void addUserToRestaurant() {
        isDataLoadingMutableLiveData.setValue(true);
        if (currentUserMutableLiveData.getValue().getUserDailySchedule() == null)
            updateUserDailySchedule();
        else
            getRestaurantFromFirebaseInteractor.getRestaurantFromFirebase(currentUserMutableLiveData.getValue().getUserDailySchedule().getRestaurantKey())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMapCompletable(restaurant -> {
                        int interestedUsers = restaurant.getNumberOfInterestedUsers() - 1;
                        return updateNumberOfInterestedUsersForRestaurantInteractor
                                .updateNumberOfInterestedUsersForRestaurant(restaurant.getPlaceId(), interestedUsers);
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

    @SuppressWarnings("ConstantConditions")
    private <T> Restaurant getRestaurantOnUserClick(T param) {
        Restaurant selectedRestaurant = new Restaurant();

        if (param instanceof Integer) {
            selectedRestaurant = allRestaurantsMutableLiveData.getValue().get((Integer) param);
        }
        else if (param instanceof String) {
            for (Restaurant restaurant : allRestaurantsMutableLiveData.getValue())
                if (restaurant.getName().equals(param)) selectedRestaurant = restaurant;
        }
        else
            Log.e("WRONG_PARAMETER", "initSelectedRestaurant: Must pass a string restaurantName or an int restaurantPosition ", new Throwable());

        return selectedRestaurant;
    }
}