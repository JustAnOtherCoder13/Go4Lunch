package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.User;
import com.picone.core.domain.entity.UserDailySchedule;
import com.picone.core.domain.entity.predictionPOJO.Prediction;
import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.entity.restaurant.RestaurantDailySchedule;
import com.picone.core.domain.interactors.restaurant.placeInteractors.FetchRestaurantDetailFromPlaceInteractor;
import com.picone.core.domain.interactors.restaurant.placeInteractors.FetchRestaurantDistanceInteractor;
import com.picone.core.domain.interactors.restaurant.placeInteractors.FetchRestaurantFromPlaceInteractor;
import com.picone.core.domain.interactors.restaurant.placeInteractors.GetPredictionInteractor;
import com.picone.core.domain.interactors.restaurant.restaurantDetailInteractors.UpdateUserChosenRestaurantInteractor;
import com.picone.core.domain.interactors.restaurant.restaurantInteractors.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurant.restaurantInteractors.GetAllPersistedRestaurantsInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetCurrentUserForEmailInteractor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
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
    private GetAllPersistedRestaurantsInteractor getAllPersistedRestaurantsInteractor;
    private FetchRestaurantFromPlaceInteractor fetchRestaurantFromPlaceInteractor;
    private FetchRestaurantDistanceInteractor fetchRestaurantDistanceInteractor;
    private FetchRestaurantDetailFromPlaceInteractor fetchRestaurantDetailFromPlaceInteractor;
    private GetPredictionInteractor getPredictionInteractor;

    @ViewModelInject
    public RestaurantViewModel(AddRestaurantInteractor addRestaurantInteractor, UpdateUserChosenRestaurantInteractor updateUserChosenRestaurantInteractor
            , GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor
            , FetchRestaurantFromPlaceInteractor fetchRestaurantFromPlaceInteractor, GetAllPersistedRestaurantsInteractor getAllPersistedRestaurantsInteractor
            , FetchRestaurantDistanceInteractor fetchRestaurantDistanceInteractor, FetchRestaurantDetailFromPlaceInteractor fetchRestaurantDetailFromPlaceInteractor
            , GetPredictionInteractor getPredictionInteractor) {
        this.addRestaurantInteractor = addRestaurantInteractor;
        this.updateUserChosenRestaurantInteractor = updateUserChosenRestaurantInteractor;
        this.getCurrentUserForEmailInteractor = getCurrentUserForEmailInteractor;
        this.getAllPersistedRestaurantsInteractor = getAllPersistedRestaurantsInteractor;
        this.fetchRestaurantFromPlaceInteractor = fetchRestaurantFromPlaceInteractor;
        this.fetchRestaurantDetailFromPlaceInteractor = fetchRestaurantDetailFromPlaceInteractor;
        this.fetchRestaurantDistanceInteractor = fetchRestaurantDistanceInteractor;
        this.getPredictionInteractor = getPredictionInteractor;
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

    @SuppressLint("CheckResult")
    public void initSelectedRestaurant(String placeId) {
        Restaurant restaurant = getRestaurantForPlaceId(placeId);
            if (restaurant !=null) {
                selectedRestaurantMutableLiveData.setValue(getRestaurantForPlaceId(placeId));
                if (!restaurant.getRestaurantDailySchedules().isEmpty() && getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()) != null
                        && getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()).getInterestedUsers() != null)
                    interestedUsersMutableLiveData.setValue(getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()).getInterestedUsers());
                else interestedUsersMutableLiveData.setValue(new ArrayList<>());
            }
        addRestaurantInteractor.addRestaurant(restaurant)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

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

    @SuppressWarnings({"ConstantConditions"})
    @SuppressLint("CheckResult")
    public void updateFanList() {
        Restaurant restaurant = getRestaurantForPlaceId(selectedRestaurantMutableLiveData.getValue().getPlaceId());
        User currentUser = currentUserMutableLiveData.getValue();
        List<String> fanList = new ArrayList<>();
        fanList.add(currentUser.getUid());

        if (restaurant.getFanList() == null || restaurant.getFanList().isEmpty())
            restaurant.setFanList(fanList);

        else if (!restaurant.getFanList().contains(currentUser.getUid()))
            restaurant.getFanList().add(currentUser.getUid());

        addRestaurantInteractor.addRestaurant(restaurant)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void updateRestaurantDailySchedule(Restaurant selectedRestaurant) {
        if (!selectedRestaurant.getRestaurantDailySchedules().isEmpty() &&
                getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules()) != null ){
            if (getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules()).getInterestedUsers() != null &&
                getUserForUid(getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules()).getInterestedUsers()) == null) {
            getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules()).getInterestedUsers()
                    .add(currentUserMutableLiveData.getValue());
            interestedUsersMutableLiveData.setValue(getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules()).getInterestedUsers());
        }} else initNewRestaurantDailySchedule(selectedRestaurant);
    }

    private void initNewRestaurantDailySchedule(Restaurant selectedRestaurant) {
        RestaurantDailySchedule restaurantDailySchedule = new RestaurantDailySchedule(DATE, new ArrayList<>());
        List<User> interestedUsers = new ArrayList<>();
        interestedUsers.add(currentUserMutableLiveData.getValue());
        restaurantDailySchedule.setInterestedUsers(interestedUsers);
        selectedRestaurant.getRestaurantDailySchedules().add(restaurantDailySchedule);
        interestedUsersMutableLiveData.setValue(interestedUsers);
    }

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    @SuppressLint("CheckResult")
    public void addUserToRestaurant() {
        isDataLoadingMutableLiveData.setValue(true);
        Restaurant selectedRestaurant = getRestaurantForPlaceId(selectedRestaurantMutableLiveData.getValue().getPlaceId());
        if (currentUserMutableLiveData.getValue().getUserDailySchedule() == null) {
            updateRestaurantDailySchedule(selectedRestaurant);
            addRestaurantInteractor.addRestaurant(selectedRestaurant)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateUserDailySchedule);
        } else {
            if (!currentUserMutableLiveData.getValue().getUserDailySchedule().getRestaurantPlaceId().equals(selectedRestaurant.getPlaceId())) {
               Restaurant restaurant = getRestaurantForPlaceId(currentUserMutableLiveData.getValue().getUserDailySchedule().getRestaurantPlaceId());
               RestaurantDailySchedule restaurantDailySchedule = getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules());
               User userToDelete = getUserForUid(restaurantDailySchedule.getInterestedUsers());
               restaurantDailySchedule.getInterestedUsers().remove(userToDelete);
               if (restaurantDailySchedule.getInterestedUsers().isEmpty())
                   restaurant.getRestaurantDailySchedules().remove(restaurantDailySchedule);

                updateRestaurantDailySchedule(selectedRestaurant);
                addRestaurantInteractor.addRestaurant(restaurant)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .andThen(addRestaurantInteractor.addRestaurant(selectedRestaurant))
                        .subscribe(this::updateUserDailySchedule);
            }
        }
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, throwable -> {
                });
    }

    public static RestaurantDailySchedule getRestaurantDailyScheduleOnToday(List<RestaurantDailySchedule> dailySchedules) {
        RestaurantDailySchedule restaurantDailyScheduleToReturn = null;
        if (dailySchedules != null)
            for (RestaurantDailySchedule restaurantDailySchedule : dailySchedules)
                if (restaurantDailySchedule.getDate().equals(DATE)){
                    Log.i("TAG", "getRestaurantDailyScheduleOnToday: daily schedule ok ");
                    restaurantDailyScheduleToReturn = restaurantDailySchedule;}

        return restaurantDailyScheduleToReturn;
    }

    private User getUserForUid(List<User> users) {
        User userToReturn = null;
        if (users != null)
            for (User user : users)
                if (user.getUid().equals(Objects.requireNonNull(currentUserMutableLiveData.getValue()).getUid()))
                    userToReturn = user;

        return userToReturn;
    }

    private Restaurant getRestaurantForPlaceId(String placeId) {
        Restaurant restaurantToReturn = null;
        for (Restaurant restaurant : Objects.requireNonNull(allRestaurantsMutableLiveData.getValue())) {
            if (restaurant.getPlaceId().equals(placeId))
                restaurantToReturn = restaurant;
        }
        return restaurantToReturn;
    }
}