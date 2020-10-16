package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;
import android.location.Location;

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
    private MutableLiveData<Integer> likeCounter = new MutableLiveData<>();
    private MutableLiveData<List<Restaurant>> allDbRestaurantsMutableLiveData = new MutableLiveData<>();


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
    public LiveData<Integer> getLikeCounter = likeCounter;
    public LiveData<List<Restaurant>> getAllDbRestaurants = allDbRestaurantsMutableLiveData;

    public void setLikeCounter(int fanListSize) {
        likeCounter.setValue(fanListSize);
    }

    public void setCurrentLocation(Location location) {
        locationMutableLiveData.setValue(location);
    }

    public void resetSelectedRestaurant() {
        selectedRestaurantMutableLiveData.setValue(null);
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void setAllDbRestaurants() {
        getAllPersistedRestaurantsInteractor.getAllPersistedRestaurants()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restaurants ->
                        allDbRestaurantsMutableLiveData.setValue(restaurants));
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
        fetchRestaurantFromPlaceInteractor.fetchRestaurantFromPlace(locationMutableLiveData.getValue(), MAPS_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(this::fetchPlaceDetail)
                .subscribe(restaurants -> {
                    updateAllRestaurantsWithPersistedValues(restaurants);
                    allRestaurantsMutableLiveData.setValue(restaurants);
                });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private Observable<List<Restaurant>> fetchPlaceDetail(List<Restaurant> nearBySearchRestaurants) {
        return Observable.create(emitter -> {
            for (Restaurant nearBySearchRestaurant : nearBySearchRestaurants)
                fetchRestaurantDetailFromPlaceInteractor.getRestaurantDetail(nearBySearchRestaurant, MAPS_KEY)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(restaurantWithDetailAdded -> fetchPlaceDistance(locationMutableLiveData.getValue(), nearBySearchRestaurant))
                        .flatMap(restaurantWithDistanceAdded -> Observable.create((ObservableOnSubscribe<List<Restaurant>>)
                                emitter1 -> emitter1.onNext(nearBySearchRestaurants)))
                        .subscribe(emitter::onNext);
        });
    }

    @SuppressLint("CheckResult")
    private Observable<Restaurant> fetchPlaceDistance(Location mCurrentLocation, Restaurant restaurantWithDetailAdded) {
        return fetchRestaurantDistanceInteractor.getRestaurantDistance_(restaurantWithDetailAdded, mCurrentLocation, MAPS_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(restaurantWithDistanceAdded ->
                        Observable.create(emitter -> emitter.onNext(restaurantWithDistanceAdded)));
    }


    @SuppressLint("CheckResult")
    public void updateAllRestaurantsWithPersistedValues(List<Restaurant> allRestaurantsFromMap) {
        if (allDbRestaurantsMutableLiveData.getValue() != null)
            for (Restaurant persistedRestaurant : allDbRestaurantsMutableLiveData.getValue()) {
                for (Restaurant restaurantFromMap : allRestaurantsFromMap) {
                    if (persistedRestaurant.getPlaceId().equals(restaurantFromMap.getPlaceId())) {
                        restaurantFromMap.setFanList(persistedRestaurant.getFanList());
                        if (persistedRestaurant.getRestaurantDailySchedules() != null)
                            restaurantFromMap.setRestaurantDailySchedules(persistedRestaurant.getRestaurantDailySchedules());
                    }
                }
            }
    }

    @SuppressLint("CheckResult")
    public void initSelectedRestaurant(String placeId) {
        Restaurant restaurant = getRestaurantForPlaceId(placeId);
        if (restaurant != null) {
            selectedRestaurantMutableLiveData.setValue(getRestaurantForPlaceId(placeId));
            if (!restaurant.getRestaurantDailySchedules().isEmpty() && getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()) != null
                    && getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()).getInterestedUsers() != null)
                interestedUsersMutableLiveData.setValue(getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()).getInterestedUsers());
            else interestedUsersMutableLiveData.setValue(new ArrayList<>());
        }
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

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
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
                .subscribe(() -> likeCounter.setValue(restaurant.getFanList().size()));
    }

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    @SuppressLint("CheckResult")
    public void addUserToRestaurant() {
        isDataLoadingMutableLiveData.setValue(true);
        Restaurant selectedRestaurant = getRestaurantForPlaceId(selectedRestaurantMutableLiveData.getValue().getPlaceId());
        if (currentUserMutableLiveData.getValue().getUserDailySchedules() == null) {
            updateRestaurantDailySchedule(selectedRestaurant);
            addRestaurantInteractor.addRestaurant(selectedRestaurant)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateUserDailySchedule);
        } else {
            if (!getUserDailyScheduleOnToday(currentUserMutableLiveData.getValue().getUserDailySchedules()).getRestaurantPlaceId().equals(selectedRestaurant.getPlaceId())) {
                Restaurant restaurant = getRestaurantForPlaceId(getUserDailyScheduleOnToday(currentUserMutableLiveData.getValue().getUserDailySchedules()).getRestaurantPlaceId());
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

    private void updateRestaurantDailySchedule(Restaurant selectedRestaurant) {
        if (!selectedRestaurant.getRestaurantDailySchedules().isEmpty() &&
                getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules()) != null) {
            if (getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules()).getInterestedUsers() != null &&
                    getUserForUid(getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules()).getInterestedUsers()) == null) {
                getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules()).getInterestedUsers()
                        .add(currentUserMutableLiveData.getValue());
                interestedUsersMutableLiveData.setValue(getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules()).getInterestedUsers());
            }
        } else initNewRestaurantDailySchedule(selectedRestaurant);
    }

    private void initNewRestaurantDailySchedule(Restaurant selectedRestaurant) {
        RestaurantDailySchedule restaurantDailySchedule = new RestaurantDailySchedule(DATE, new ArrayList<>());
        List<User> interestedUsers = new ArrayList<>();
        interestedUsers.add(currentUserMutableLiveData.getValue());
        restaurantDailySchedule.setInterestedUsers(interestedUsers);
        selectedRestaurant.getRestaurantDailySchedules().add(restaurantDailySchedule);
        interestedUsersMutableLiveData.setValue(interestedUsers);
    }

    //currentUserMutableLiveData.getValue().getEmail() can't be null cause already set in restaurant list fragment
    @SuppressLint("CheckResult")
    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    private void updateUserDailySchedule() {
        User user = currentUserMutableLiveData.getValue();

        if (user.getUserDailySchedules()==null) user.setUserDailySchedules(new ArrayList<>());
        if (getUserDailyScheduleOnToday(user.getUserDailySchedules())==null)user.getUserDailySchedules().add(new UserDailySchedule(DATE,selectedRestaurantMutableLiveData.getValue().getPlaceId(),selectedRestaurantMutableLiveData.getValue().getName()));
        if (getUserDailyScheduleOnToday(user.getUserDailySchedules())!=null){
            getUserDailyScheduleOnToday(user.getUserDailySchedules()).setRestaurantName(selectedRestaurantMutableLiveData.getValue().getName());
            getUserDailyScheduleOnToday(user.getUserDailySchedules()).setRestaurantPlaceId(selectedRestaurantMutableLiveData.getValue().getPlaceId());
        }
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
                if (restaurantDailySchedule.getDate().equals(DATE)) {
                    restaurantDailyScheduleToReturn = restaurantDailySchedule;
                }
        return restaurantDailyScheduleToReturn;
    }

    public static UserDailySchedule getUserDailyScheduleOnToday(List<UserDailySchedule> dailySchedules) {
        UserDailySchedule userDailyScheduleToReturn = null;
        if (dailySchedules != null)
            for (UserDailySchedule userDailySchedule : dailySchedules)
                if (userDailySchedule.getDate().equals(DATE)) {
                    userDailyScheduleToReturn = userDailySchedule;
                }
        return userDailyScheduleToReturn;
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