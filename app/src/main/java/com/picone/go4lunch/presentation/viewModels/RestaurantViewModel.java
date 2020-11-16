package com.picone.go4lunch.presentation.viewModels;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.picone.core.domain.entity.pOJOprediction.Prediction;
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
import com.picone.core.utils.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

import static com.picone.core.utils.ConstantParameter.MAPS_KEY;
import static com.picone.core.utils.ConstantParameter.TODAY;
import static com.picone.core.utils.FindInListUtil.getCurrentUserInfoForUid;
import static com.picone.core.utils.FindInListUtil.getRestaurantDailyScheduleOnToday;
import static com.picone.core.utils.FindInListUtil.getRestaurantForPlaceId;
import static com.picone.core.utils.FindInListUtil.getUserDailyScheduleOnToday;

public class RestaurantViewModel extends BaseViewModel {

    private MutableLiveData<User> currentUserMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Restaurant> selectedRestaurantMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> interestedUsersMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDataLoadingMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Restaurant>> allRestaurantsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> likeCounterMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Restaurant>> allDbRestaurantsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> filteredUsersMutableLiveData = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<String>> fanListMutableLiveData = new MutableLiveData<>();


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

    public LiveData<List<String>> getFanList = fanListMutableLiveData;
    public LiveData<Boolean> isDataLoading = isDataLoadingMutableLiveData;
    public LiveData<List<User>> getInterestedUsersForRestaurant = interestedUsersMutableLiveData;
    public LiveData<Restaurant> getSelectedRestaurant = selectedRestaurantMutableLiveData;
    public LiveData<List<Restaurant>> getAllRestaurants = allRestaurantsMutableLiveData;
    public LiveData<User> getCurrentUser = currentUserMutableLiveData;
    public LiveData<Location> getCurrentLocation = locationMutableLiveData;
    public LiveData<Integer> getLikeCounter = likeCounterMutableLiveData;
    public LiveData<List<Restaurant>> getAllDbRestaurants = allDbRestaurantsMutableLiveData;
    public LiveData<List<User>> getAllFilteredUsers = filteredUsersMutableLiveData;

    //---------------------------------------------SETTER----------------------------------------------
    public void resetSelectedRestaurant() {
        selectedRestaurantMutableLiveData.setValue(null);
        fanListMutableLiveData.setValue(null);
    }

    public void setLikeCounterMutableLiveData(int fanListSize) {
        likeCounterMutableLiveData.setValue(fanListSize);
    }

    public void setCurrentLocation(Location location) {
        locationMutableLiveData.setValue(location);
    }

    public void setAllDbRestaurants() {
        compositeDisposable.add(
                getAllPersistedRestaurantsInteractor.getAllPersistedRestaurants()
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(restaurants -> allDbRestaurantsMutableLiveData.setValue(restaurants),
                        throwable -> checkException()));
    }

    public void setCurrentUser(String authUserEmail) {
        compositeDisposable.add(
                getCurrentUserForEmailInteractor.getCurrentUserForEmail(authUserEmail)
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(currentUsers -> currentUserMutableLiveData.setValue(currentUsers.get(0)),
                        throwable -> checkException()));
    }

    public void setInterestedUsersForRestaurant(String placeId, List<Restaurant> allRestaurants) {
        Restaurant restaurant = getRestaurantForPlaceId(placeId, allRestaurants);
        if (restaurant != null) {
            selectedRestaurantMutableLiveData.setValue(restaurant);
            if (restaurant.getFanList() != null && !restaurant.getFanList().isEmpty())
                fanListMutableLiveData.setValue(restaurant.getFanList());
            if (!restaurant.getRestaurantDailySchedules().isEmpty() && getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()) != null
                    && getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()).getInterestedUsers() != null)
                interestedUsersMutableLiveData.setValue(getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()).getInterestedUsers());
            else interestedUsersMutableLiveData.setValue(new ArrayList<>());
        }
    }

    public void setAllRestaurantFromMaps(boolean isReset) {
        isDataLoadingMutableLiveData.setValue(true);
        if (allRestaurantsMutableLiveData.getValue() == null || isReset)
            compositeDisposable.add(
                    fetchRestaurantFromPlaceInteractor.fetchRestaurantFromPlace(locationMutableLiveData.getValue(), MAPS_KEY)
                    .subscribeOn(schedulerProvider.getIo())
                    .observeOn(schedulerProvider.getUi())
                    .flatMap(this::fetchPlaceDetail)
                    .subscribe(this::updateAllRestaurantsWithPersistedValues,
                            throwable -> checkException()));
    }

    //--------------------------------------------MAPS DETAIL----------------------------------------------

    public Observable<List<Restaurant>> fetchPlaceDetail(List<Restaurant> nearBySearchRestaurants) {
        return Observable.create(emitter -> {
            for (Restaurant nearBySearchRestaurant : nearBySearchRestaurants)
                compositeDisposable.add(
                        fetchRestaurantDetailFromPlaceInteractor.getRestaurantDetail(nearBySearchRestaurant, MAPS_KEY)
                        .subscribeOn(schedulerProvider.getIo())
                        .observeOn(schedulerProvider.getUi())
                        .flatMap(restaurantWithDetailAdded -> fetchPlaceDistance(locationMutableLiveData.getValue(), nearBySearchRestaurant))
                        .flatMap(restaurantWithDistanceAdded -> Observable.create((ObservableOnSubscribe<List<Restaurant>>)
                                emitter1 -> emitter1.onNext(nearBySearchRestaurants)))
                        .subscribe(emitter::onNext, throwable -> checkException()));
        });
    }

    private Observable<Restaurant> fetchPlaceDistance(Location mCurrentLocation, Restaurant restaurantWithDetailAdded) {
        return fetchRestaurantDistanceInteractor.getRestaurantDistance(restaurantWithDetailAdded, mCurrentLocation, MAPS_KEY)
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .flatMap(restaurantWithDistanceAdded ->
                        Observable.create(emitter ->
                                emitter.onNext(restaurantWithDistanceAdded)));
    }

    //---------------------------------------------ACTIONS----------------------------------------------

    public void filterExistingResults(String query, List<User> allUsers, String locationStr, String googleKey) {
        List<Restaurant> filteredRestaurant = new ArrayList<>();
        List<User> filteredUsers = new ArrayList<>();
        compositeDisposable.add(
                getPredictionInteractor.getPredictions(query, googleKey, locationStr)
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(predictionResponse -> {
                    for (Prediction response : predictionResponse.getPredictions()) {
                        for (Restaurant restaurant : Objects.requireNonNull(allRestaurantsMutableLiveData.getValue())) {
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
                }, throwable -> checkException()));
    }

    public void addUserToRestaurant(Restaurant selectedRestaurant, List<Restaurant> allRestaurants) {
        isDataLoadingMutableLiveData.setValue(true);

        if (Objects.requireNonNull(currentUserMutableLiveData.getValue()).getUserDailySchedules() == null ||
                getUserDailyScheduleOnToday(currentUserMutableLiveData.getValue().getUserDailySchedules()) == null)
            updateRestaurantDailySchedule(selectedRestaurant);

        else if (!getUserDailyScheduleOnToday(currentUserMutableLiveData.getValue().getUserDailySchedules()).getRestaurantPlaceId()
                .equals(selectedRestaurant.getPlaceId())) {
            persistRestaurant(firstChoiceRestaurantWithUserRemoved(allRestaurants));
            updateRestaurantDailySchedule(selectedRestaurant);
        }
    }

    public void persistRestaurant(Restaurant restaurant) {
        compositeDisposable.add(
                addRestaurantCompletable(restaurant).subscribe(() -> {}, throwable -> checkException()));
    }

    public void sendNotification(String token, String title, String message) {
        compositeDisposable.add(
                sendNotificationInteractor.sendNotification(token, title, message)
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(o -> {
                }, throwable -> checkException()));
    }

    public void cancelReservation(List<Restaurant> allRestaurants) {
        User currentUser = currentUserMutableLiveData.getValue();
        assert currentUser != null;
        Restaurant firstChoiceRestaurant = getRestaurantForPlaceId(getUserDailyScheduleOnToday(currentUser.getUserDailySchedules()).getRestaurantPlaceId(), allRestaurants);

        getRestaurantDailyScheduleOnToday(firstChoiceRestaurant.getRestaurantDailySchedules()).getInterestedUsers().remove(
                getCurrentUserInfoForUid(getRestaurantDailyScheduleOnToday(firstChoiceRestaurant.getRestaurantDailySchedules()).getInterestedUsers(), currentUser));
        if (getRestaurantDailyScheduleOnToday(firstChoiceRestaurant.getRestaurantDailySchedules()).getInterestedUsers().isEmpty())
            firstChoiceRestaurant.getRestaurantDailySchedules().remove(getRestaurantDailyScheduleOnToday(firstChoiceRestaurant.getRestaurantDailySchedules()));

        currentUser.getUserDailySchedules().remove(getUserDailyScheduleOnToday(currentUser.getUserDailySchedules()));
        compositeDisposable.add(
                addRestaurantCompletable(firstChoiceRestaurant)
                .andThen(updateUserChosenRestaurantInteractor.updateUserChosenRestaurant(currentUser))
                .subscribe(() -> {}, throwable -> checkException()));
    }

    //---------------------------------------------UPDATE----------------------------------------------

    private void updateRestaurantDailySchedule(@NonNull Restaurant selectedRestaurant) {

        if (selectedRestaurant.getRestaurantDailySchedules() == null)
            selectedRestaurant.setRestaurantDailySchedules(new ArrayList<>());
        if (getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules()) == null)
            selectedRestaurant.getRestaurantDailySchedules().add(new RestaurantDailySchedule(TODAY, new ArrayList<>()));
        if (getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules()) != null) {
            getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules()).getInterestedUsers().add(createFormatUser());
        }
        interestedUsersMutableLiveData.setValue(getRestaurantDailyScheduleOnToday(selectedRestaurant.getRestaurantDailySchedules()).getInterestedUsers());
        persistRestaurant(selectedRestaurant);
    }

    public void updateUserDailySchedule(@NonNull User user, Restaurant selectedRestaurant) {
        if (user.getUserDailySchedules() == null) user.setUserDailySchedules(new ArrayList<>());
        if (getUserDailyScheduleOnToday(user.getUserDailySchedules()) == null)
            user.getUserDailySchedules().add(new UserDailySchedule(TODAY, selectedRestaurant.getPlaceId(), selectedRestaurant.getName()));
        if (getUserDailyScheduleOnToday(user.getUserDailySchedules()) != null) {
            getUserDailyScheduleOnToday(user.getUserDailySchedules()).setRestaurantName(selectedRestaurant.getName());
            getUserDailyScheduleOnToday(user.getUserDailySchedules()).setRestaurantPlaceId(selectedRestaurant.getPlaceId());
        }
        compositeDisposable.add(
                updateUserChosenRestaurantInteractor.updateUserChosenRestaurant(user)
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(() -> isDataLoadingMutableLiveData.setValue(false), throwable -> checkException()));
    }

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
                        else restaurantFromMap.setRestaurantDailySchedules(new ArrayList<>());
                    }
                }
            }
            allRestaurantsMutableLiveData.setValue(allRestaurants);
            isDataLoadingMutableLiveData.setValue(false);
        }
    }

    public void updateFanList(@NonNull Restaurant restaurant) {
        User currentUser = currentUserMutableLiveData.getValue();
        List<String> fanList = new ArrayList<>();
        assert currentUser != null;
        if (restaurant.getFanList() != null && !restaurant.getFanList().isEmpty()
                && !restaurant.getFanList().contains(currentUser.getUid()))
            fanList = restaurant.getFanList();

        fanList.add(currentUser.getUid());
        restaurant.setFanList(fanList);
        fanListMutableLiveData.setValue(fanList);
        compositeDisposable.add(
                addRestaurantCompletable(restaurant).subscribe(() -> likeCounterMutableLiveData.setValue(restaurant.getFanList().size()), throwable -> checkException()));
    }

    //---------------------------------------------HELPER----------------------------------------------
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
    private Restaurant firstChoiceRestaurantWithUserRemoved(List<Restaurant> allRestaurants) {
        Restaurant restaurant = getRestaurantForPlaceId(getUserDailyScheduleOnToday(Objects.requireNonNull(currentUserMutableLiveData.getValue()).getUserDailySchedules()).getRestaurantPlaceId(), allRestaurants);
        RestaurantDailySchedule restaurantDailySchedule = getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules());
        User userInfoToDelete = getCurrentUserInfoForUid(restaurantDailySchedule.getInterestedUsers(), currentUserMutableLiveData.getValue());
        restaurantDailySchedule.getInterestedUsers().remove(userInfoToDelete);
        if (restaurantDailySchedule.getInterestedUsers().isEmpty())
            restaurant.getRestaurantDailySchedules().remove(restaurantDailySchedule);
        return restaurant;
    }

    private Completable addRestaurantCompletable (Restaurant restaurant){
        return  addRestaurantInteractor.addRestaurant(restaurant)
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi());
    }
}