package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.entity.UserDailySchedule;
import com.picone.core.domain.entity.predictionPOJO.Prediction;
import com.picone.core.domain.entity.restaurant.RestaurantDailySchedule;
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
import com.picone.core.domain.interactors.restaurant.restaurantInteractors.GetRestaurantFromFirebaseInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetCurrentUserForEmailInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetInterestedUsersForRestaurantKeyInteractor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
        this.getRestaurantFromFirebaseInteractor = getRestaurantFromFirebaseInteractor;
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
    public void initSelectedRestaurant(String placeId) {
        Restaurant selectedRestaurant = new Restaurant();
        for (Restaurant restaurant : Objects.requireNonNull(allRestaurantsMutableLiveData.getValue()))
            if (restaurant.getPlaceId().equals(placeId)) {
                selectedRestaurantMutableLiveData.setValue(restaurant);
                selectedRestaurant = restaurant;
            }

        getRestaurantFromFirebaseInteractor.getRestaurantFromFirebase(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .switchIfEmpty(addRestaurantInteractor.addRestaurant(selectedRestaurant)
                        .toObservable())
                .flatMap(restaurantFromFirebase ->
                        getInterestedUsersForRestaurantKeyInteractor.getInterestedUsersForRestaurantKey(placeId))
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
                                if (persistedRestaurant.getRestaurantDailySchedules() != null)
                                    restaurantFromMap.setRestaurantDailySchedules(persistedRestaurant.getRestaurantDailySchedules());
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
        getFanListForRestaurantInteractor.getFanListForRestaurant(restaurant.getPlaceId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .switchIfEmpty(updateFanListForRestaurantInteractor.updateFanListForRestaurant(restaurant.getPlaceId(), fanList)
                        .andThen(getFanListForRestaurantInteractor.getFanListForRestaurant(restaurant.getPlaceId())))
                .flatMapCompletable(persistedFanList -> {
                    if (!persistedFanList.contains(currentUser.getUid())) {
                        persistedFanList.add(currentUser.getUid());
                        selectedRestaurantMutableLiveData.getValue().setFanList(persistedFanList);
                    }
                    //TODO if restaurantMutable not set, fanList don't update if set create a loop cause restaurant detail get restaurantMutable values
                    //selectedRestaurantMutableLiveData.setValue(restaurant);
                    return updateFanListForRestaurantInteractor.updateFanListForRestaurant(restaurant.getPlaceId(), persistedFanList);
                })
                .subscribe(() -> {
                }, throwable -> {
                });
    }

    private void updateRestaurantDailySchedule(Restaurant selectedRestaurant) {
        if (!selectedRestaurant.getRestaurantDailySchedules().isEmpty()
                && getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules()) != null) {
            RestaurantDailySchedule restaurantDailySchedule = getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules());

            if (restaurantDailySchedule.getInterestedUsers().isEmpty())
                restaurantDailySchedule.getInterestedUsers().add(currentUserMutableLiveData.getValue());
            else
                for (User user : restaurantDailySchedule.getInterestedUsers())
                    if (user.getUid().equals(currentUserMutableLiveData.getValue().getUid()))
                        return;
            restaurantDailySchedule.getInterestedUsers().add(currentUserMutableLiveData.getValue());
        } else {
            RestaurantDailySchedule restaurantDailySchedule = new RestaurantDailySchedule(DATE, new ArrayList<>());
            List<User> interestedUsers = new ArrayList<>();
            interestedUsers.add(currentUserMutableLiveData.getValue());
            restaurantDailySchedule.setInterestedUsers(interestedUsers);
            selectedRestaurant.getRestaurantDailySchedules().add(restaurantDailySchedule);
        }
    }

    private RestaurantDailySchedule getRestaurantDailyScheduleOnToday(List<RestaurantDailySchedule> dailySchedules) {
        RestaurantDailySchedule restaurantDailyScheduleToReturn = null;
        for (RestaurantDailySchedule restaurantDailySchedule : dailySchedules)
            if (restaurantDailySchedule.getDate().equals(DATE))
                restaurantDailyScheduleToReturn = restaurantDailySchedule;

        return restaurantDailyScheduleToReturn;
    }

    private User getUserForUid(List<User> users){
        User userToReturn = null;
        for (User user: users)
            if (user.getUid().equals(currentUserMutableLiveData.getValue().getUid()))
                userToReturn=user;

            return userToReturn;
    }

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    @SuppressLint("CheckResult")
    public void addUserToRestaurant() {
        isDataLoadingMutableLiveData.setValue(true);
        Restaurant selectedRestaurant = selectedRestaurantMutableLiveData.getValue();

        if (currentUserMutableLiveData.getValue().getUserDailySchedule() == null) {
            updateUserDailySchedule();
            updateRestaurantDailySchedule(selectedRestaurant);
            addRestaurantInteractor.addRestaurant(selectedRestaurant)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
        } else
            getRestaurantFromFirebaseInteractor.getRestaurantFromFirebase(currentUserMutableLiveData.getValue().getUserDailySchedule().getRestaurantKey())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMapCompletable(restaurant -> {
                        RestaurantDailySchedule restaurantDailySchedule = getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules());
                        User userToDelete = getUserForUid(restaurantDailySchedule.getInterestedUsers());
                        restaurantDailySchedule.getInterestedUsers().remove(userToDelete);
                        updateRestaurantDailySchedule(selectedRestaurant);
                        return addRestaurantInteractor.addRestaurant(restaurant).andThen(addRestaurantInteractor.addRestaurant(selectedRestaurant));
                    })
                    .subscribe(this::updateUserDailySchedule);
    }

    //currentUserMutableLiveData.getValue().getEmail() can't be null cause already set in restaurant list fragment
    @SuppressLint("CheckResult")
    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    private void updateUserDailySchedule() {
        User user = currentUserMutableLiveData.getValue();
        user.setUserDailySchedule(new UserDailySchedule(DATE
                , selectedRestaurantMutableLiveData.getValue().getPlaceId()
                , selectedRestaurantMutableLiveData.getValue().getName()));

        updateUserChosenRestaurantInteractor.updateUserChosenRestaurant(user)
                .doFinally(() -> isDataLoadingMutableLiveData.setValue(false))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, throwable -> {
                });
    }
}