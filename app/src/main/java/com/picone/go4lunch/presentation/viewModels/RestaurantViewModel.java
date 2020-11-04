package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.picone.core.domain.entity.predictionPOJO.Prediction;
import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.entity.restaurant.RestaurantDailySchedule;
import com.picone.core.domain.entity.user.User;
import com.picone.core.domain.entity.user.UserDailySchedule;
import com.picone.core.domain.interactors.SendNotificationInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.FetchRestaurantDetailFromPlaceInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.FetchRestaurantDistanceInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.FetchRestaurantFromPlaceInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.placeInteractors.GetPredictionInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors.GetAllPersistedRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurantInteractors.UpdateUserChosenRestaurantInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetCurrentUserForEmailInteractor;
import com.picone.go4lunch.presentation.utils.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

import static com.picone.core.data.ConstantParameter.MAPS_KEY;
import static com.picone.core.data.ConstantParameter.TODAY;
import static com.picone.go4lunch.presentation.utils.DailyScheduleHelper.getRestaurantDailyScheduleOnToday;
import static com.picone.go4lunch.presentation.utils.DailyScheduleHelper.getUserDailyScheduleOnToday;

public class RestaurantViewModel extends BaseViewModel {

    private MutableLiveData<User> currentUserMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Restaurant> selectedRestaurantMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> interestedUsersMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDataLoadingMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Restaurant>> allRestaurantsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> likeCounter = new MutableLiveData<>();
    private MutableLiveData<List<Restaurant>> allDbRestaurantsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Restaurant> userChosenRestaurantMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> filteredUsersMutableLiveData = new MutableLiveData<>();


    private AddRestaurantInteractor addRestaurantInteractor;
    private UpdateUserChosenRestaurantInteractor updateUserChosenRestaurantInteractor;
    private GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor;
    private GetAllPersistedRestaurantsInteractor getAllPersistedRestaurantsInteractor;
    private FetchRestaurantFromPlaceInteractor fetchRestaurantFromPlaceInteractor;
    private FetchRestaurantDistanceInteractor fetchRestaurantDistanceInteractor;
    private FetchRestaurantDetailFromPlaceInteractor fetchRestaurantDetailFromPlaceInteractor;
    private GetPredictionInteractor getPredictionInteractor;
    private SendNotificationInteractor sendNotificationInteractor;
    private SchedulerProvider schedulerProvider;


    @ViewModelInject
    public RestaurantViewModel(AddRestaurantInteractor addRestaurantInteractor, UpdateUserChosenRestaurantInteractor updateUserChosenRestaurantInteractor
            , GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor
            , FetchRestaurantFromPlaceInteractor fetchRestaurantFromPlaceInteractor, GetAllPersistedRestaurantsInteractor getAllPersistedRestaurantsInteractor
            , FetchRestaurantDistanceInteractor fetchRestaurantDistanceInteractor, FetchRestaurantDetailFromPlaceInteractor fetchRestaurantDetailFromPlaceInteractor
            , GetPredictionInteractor getPredictionInteractor, SendNotificationInteractor sendNotificationInteractor, SchedulerProvider schedulerProvider) {
        this.addRestaurantInteractor = addRestaurantInteractor;
        this.updateUserChosenRestaurantInteractor = updateUserChosenRestaurantInteractor;
        this.getCurrentUserForEmailInteractor = getCurrentUserForEmailInteractor;
        this.getAllPersistedRestaurantsInteractor = getAllPersistedRestaurantsInteractor;
        this.fetchRestaurantFromPlaceInteractor = fetchRestaurantFromPlaceInteractor;
        this.fetchRestaurantDetailFromPlaceInteractor = fetchRestaurantDetailFromPlaceInteractor;
        this.fetchRestaurantDistanceInteractor = fetchRestaurantDistanceInteractor;
        this.getPredictionInteractor = getPredictionInteractor;
        this.sendNotificationInteractor = sendNotificationInteractor;
        this.schedulerProvider = schedulerProvider;
    }

    //---------------------------------------------GETTER----------------------------------------------

    public LiveData<Boolean> isDataLoading = isDataLoadingMutableLiveData;
    public LiveData<List<User>> getInterestedUsersForRestaurant = interestedUsersMutableLiveData;
    public LiveData<Restaurant> getSelectedRestaurant = selectedRestaurantMutableLiveData;
    public LiveData<List<Restaurant>> getAllRestaurants = allRestaurantsMutableLiveData;
    public LiveData<User> getCurrentUser = currentUserMutableLiveData;
    public LiveData<Location> getCurrentLocation = locationMutableLiveData;
    public LiveData<Integer> getLikeCounter = likeCounter;
    public LiveData<Restaurant> getUserChosenRestaurant = userChosenRestaurantMutableLiveData;
    public LiveData<List<Restaurant>> getAllDbRestaurants = allDbRestaurantsMutableLiveData;
    public LiveData<List<User>> getAllFilteredUsers = filteredUsersMutableLiveData;

    //---------------------------------------------SETTER----------------------------------------------

    public void resetSelectedRestaurant() {
        selectedRestaurantMutableLiveData.setValue(null);
    }

    public void setLikeCounter(int fanListSize) {
        likeCounter.setValue(fanListSize);
    }

    public void setCurrentLocation(Location location) {
        locationMutableLiveData.setValue(location);
    }

    @SuppressLint("CheckResult")
    public void setUserChosenRestaurant() {
        if (currentUserMutableLiveData.getValue() != null && getUserDailyScheduleOnToday(currentUserMutableLiveData.getValue().getUserDailySchedules()) != null)
            userChosenRestaurantMutableLiveData.setValue(
                    getRestaurantForPlaceId(
                            getUserDailyScheduleOnToday(
                                    currentUserMutableLiveData.getValue().getUserDailySchedules()).getRestaurantPlaceId()));
        isDataLoadingMutableLiveData.setValue(false);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void setAllDbRestaurants() {
        getAllPersistedRestaurantsInteractor.getAllPersistedRestaurants()
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(restaurants -> allDbRestaurantsMutableLiveData.setValue(restaurants),
                        throwable -> checkException());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void setCurrentUser(String authUserEmail) {
        getCurrentUserForEmailInteractor.getCurrentUserForEmail(authUserEmail)
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(currentUsers -> currentUserMutableLiveData.setValue(currentUsers.get(0)),
                        throwable -> checkException());
    }

    @SuppressLint("CheckResult")
    public void setInterestedUsersForRestaurant(String placeId) {

        Restaurant restaurant = getRestaurantForPlaceId(placeId);
        if (restaurant != null) {
            selectedRestaurantMutableLiveData.setValue(restaurant);
            if (!restaurant.getRestaurantDailySchedules().isEmpty() && getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()) != null
                    && getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()).getInterestedUsers() != null)
                interestedUsersMutableLiveData.setValue(getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()).getInterestedUsers());
            else interestedUsersMutableLiveData.setValue(new ArrayList<>());
        }
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void setAllRestaurantFromMaps(boolean isReset) {
        isDataLoadingMutableLiveData.setValue(true);
        if (allRestaurantsMutableLiveData.getValue() == null || isReset)
            fetchRestaurantFromPlaceInteractor.fetchRestaurantFromPlace(locationMutableLiveData.getValue(), MAPS_KEY)
                    .subscribeOn(schedulerProvider.getIo())
                    .observeOn(schedulerProvider.getUi())
                    .flatMap(this::fetchPlaceDetail)
                    .subscribe(this::updateAllRestaurantsWithPersistedValues,
                            throwable -> checkException());
    }


    //--------------------------------------------MAPS DETAIL----------------------------------------------

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Observable<List<Restaurant>> fetchPlaceDetail(List<Restaurant> nearBySearchRestaurants) {
        return Observable.create(emitter -> {
            for (Restaurant nearBySearchRestaurant : nearBySearchRestaurants)
                fetchRestaurantDetailFromPlaceInteractor.getRestaurantDetail(nearBySearchRestaurant, MAPS_KEY)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .flatMap(restaurantWithDetailAdded -> fetchPlaceDistance(locationMutableLiveData.getValue(), nearBySearchRestaurant))
                        .flatMap(restaurantWithDistanceAdded -> Observable.create((ObservableOnSubscribe<List<Restaurant>>)
                                emitter1 -> emitter1.onNext(nearBySearchRestaurants)))
                        .subscribe(emitter::onNext, throwable -> checkException());
        });
    }

    @SuppressLint("CheckResult")
    private Observable<Restaurant> fetchPlaceDistance(Location mCurrentLocation, Restaurant restaurantWithDetailAdded) {
        return fetchRestaurantDistanceInteractor.getRestaurantDistance(restaurantWithDetailAdded, mCurrentLocation, MAPS_KEY)
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .flatMap(restaurantWithDistanceAdded ->
                        Observable.create(emitter ->
                                emitter.onNext(restaurantWithDistanceAdded)));
    }

    //---------------------------------------------ACTIONS----------------------------------------------

    @SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
    @SuppressLint("CheckResult")
    public void filterExistingResults(String query, List<User> allUsers) {
        Location location = locationMutableLiveData.getValue();
        List<Restaurant> filteredRestaurant = new ArrayList<>();
        List<User> filteredUsers = new ArrayList<>();
        getPredictionInteractor.getPredictions(query, MAPS_KEY, String.valueOf(location.getLatitude()).concat(",").concat(String.valueOf(location.getLongitude())))
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(predictionResponse -> {
                    for (Prediction response : predictionResponse.getPredictions()) {
                        for (Restaurant restaurant : allRestaurantsMutableLiveData.getValue()) {
                            if (restaurant.getPlaceId().equals(response.getPlaceId()))
                                filteredRestaurant.add(restaurant);
                        }
                    }
                    for (Restaurant restaurant : filteredRestaurant) {
                        for (User user : allUsers) {
                            if (user.getUserDailySchedules() != null && getUserDailyScheduleOnToday(user.getUserDailySchedules()) != null) {
                                if (getUserDailyScheduleOnToday(user.getUserDailySchedules()).getRestaurantPlaceId().equals(restaurant.getPlaceId())) {
                                    filteredUsers.add(user);
                                }
                            }
                        }
                    }
                    allRestaurantsMutableLiveData.setValue(filteredRestaurant);
                    filteredUsersMutableLiveData.setValue(filteredUsers);
                },throwable -> checkException());
    }


    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    @SuppressLint("CheckResult")
    public void addUserToRestaurant(Restaurant selectedRestaurant,User user) {
        isDataLoadingMutableLiveData.setValue(true);

        if (currentUserMutableLiveData.getValue().getUserDailySchedules() == null ||
                getUserDailyScheduleOnToday(currentUserMutableLiveData.getValue().getUserDailySchedules()) == null)
            updateRestaurantDailySchedule(selectedRestaurant,user);

        else if (!getUserDailyScheduleOnToday(currentUserMutableLiveData.getValue().getUserDailySchedules()).getRestaurantPlaceId()
                .equals(selectedRestaurant.getPlaceId())) {
            addRestaurantInteractor.addRestaurant(FirstChoiceRestaurantWithUserRemoved())
                    .subscribeOn(schedulerProvider.getIo())
                    .observeOn(schedulerProvider.getUi())
                    .subscribe(() -> updateRestaurantDailySchedule(selectedRestaurant,user), throwable -> checkException());
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void sendNotification(String token, String title, String message) {
        sendNotificationInteractor.sendNotification(token, title, message)
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(o ->{ }, throwable -> checkException());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void cancelReservation() {
        User currentUser = currentUserMutableLiveData.getValue();
        assert currentUser != null;
        Restaurant firstChoiceRestaurant = getRestaurantForPlaceId(getUserDailyScheduleOnToday(currentUser.getUserDailySchedules()).getRestaurantPlaceId());

        getRestaurantDailyScheduleOnToday(firstChoiceRestaurant.getRestaurantDailySchedules()).getInterestedUsers().remove(
                getCurrentUserInfoForUid(getRestaurantDailyScheduleOnToday(firstChoiceRestaurant.getRestaurantDailySchedules()).getInterestedUsers()));
        if (getRestaurantDailyScheduleOnToday(firstChoiceRestaurant.getRestaurantDailySchedules()).getInterestedUsers().isEmpty())
            firstChoiceRestaurant.getRestaurantDailySchedules().remove(getRestaurantDailyScheduleOnToday(firstChoiceRestaurant.getRestaurantDailySchedules()));

        currentUser.getUserDailySchedules().remove(getUserDailyScheduleOnToday(currentUser.getUserDailySchedules()));

        addRestaurantInteractor.addRestaurant(firstChoiceRestaurant)
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .andThen(updateUserChosenRestaurantInteractor.updateUserChosenRestaurant(currentUser))
                .subscribe(()->{},throwable -> checkException());
    }

    //---------------------------------------------UPDATE----------------------------------------------

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    private void updateRestaurantDailySchedule(Restaurant selectedRestaurant,User user) {

        if (selectedRestaurant.getRestaurantDailySchedules() == null)
            selectedRestaurant.setRestaurantDailySchedules(new ArrayList<>());
        if (getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules()) == null)
            selectedRestaurant.getRestaurantDailySchedules().add(new RestaurantDailySchedule(TODAY, new ArrayList<>()));
        if (getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules()) != null) {
            getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules()).getInterestedUsers().add(createFormatUser());
        }
        interestedUsersMutableLiveData.setValue(getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules()).getInterestedUsers());
        addRestaurantInteractor.addRestaurant(selectedRestaurant)
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .andThen(updateUserDailySchedule(user))
                .subscribe(()->{}, throwable -> checkException());

    }

    //currentUserMutableLiveData.getValue().getEmail() can't be null cause already set in restaurant list fragment
    @SuppressLint("CheckResult")
    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    private Completable updateUserDailySchedule(User user) {
        if (user.getUserDailySchedules() == null) user.setUserDailySchedules(new ArrayList<>());
        if (getUserDailyScheduleOnToday(user.getUserDailySchedules()) == null)
            user.getUserDailySchedules().add(new UserDailySchedule(TODAY, selectedRestaurantMutableLiveData.getValue().getPlaceId(), selectedRestaurantMutableLiveData.getValue().getName()));
        if (getUserDailyScheduleOnToday(user.getUserDailySchedules()) != null) {
            getUserDailyScheduleOnToday(user.getUserDailySchedules()).setRestaurantName(selectedRestaurantMutableLiveData.getValue().getName());
            getUserDailyScheduleOnToday(user.getUserDailySchedules()).setRestaurantPlaceId(selectedRestaurantMutableLiveData.getValue().getPlaceId());
        }
        return
        updateUserChosenRestaurantInteractor.updateUserChosenRestaurant(user);
    }

    @SuppressLint("CheckResult")
    public void updateAllRestaurantsWithPersistedValues(List<Restaurant> allRestaurants) {
        if (allRestaurants == null)
            allRestaurants = allRestaurantsMutableLiveData.getValue();
        List<Restaurant> allDbRestaurants = allDbRestaurantsMutableLiveData.getValue();
        if (allRestaurants != null && allDbRestaurants != null) {
            for (Restaurant persistedRestaurant : allDbRestaurants) {
                for (Restaurant restaurantFromMap : allRestaurants) {
                    if (persistedRestaurant.getPlaceId().equals(restaurantFromMap.getPlaceId())) {
                        restaurantFromMap.setFanList(persistedRestaurant.getFanList());
                        if (persistedRestaurant.getRestaurantDailySchedules() != null)
                            restaurantFromMap.setRestaurantDailySchedules(persistedRestaurant.getRestaurantDailySchedules());
                    }
                }
            }
            allRestaurantsMutableLiveData.setValue(allRestaurants);
            isDataLoadingMutableLiveData.setValue(false);
        }
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
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(() -> likeCounter.setValue(restaurant.getFanList().size()),throwable -> checkException());
    }

    //---------------------------------------------HELPER----------------------------------------------

    private User getCurrentUserInfoForUid(List<User> allUsers) {
        User userToReturn = null;
        if (allUsers != null)
            for (User user : allUsers)
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

    @NonNull
    private User createFormatUser() {
        return new User(Objects.requireNonNull(currentUserMutableLiveData.getValue()).getUid(),
                currentUserMutableLiveData.getValue().getName(),
                null,
                currentUserMutableLiveData.getValue().getAvatar(),
                null,
                null);
    }

    @NonNull
    private Restaurant FirstChoiceRestaurantWithUserRemoved() {
        Restaurant restaurant = getRestaurantForPlaceId(getUserDailyScheduleOnToday(Objects.requireNonNull(currentUserMutableLiveData.getValue()).getUserDailySchedules()).getRestaurantPlaceId());
        RestaurantDailySchedule restaurantDailySchedule = getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules());
        User userInfoToDelete = getCurrentUserInfoForUid(restaurantDailySchedule.getInterestedUsers());
        restaurantDailySchedule.getInterestedUsers().remove(userInfoToDelete);
        if (restaurantDailySchedule.getInterestedUsers().isEmpty())
            restaurant.getRestaurantDailySchedules().remove(restaurantDailySchedule);
        return restaurant;
    }
}