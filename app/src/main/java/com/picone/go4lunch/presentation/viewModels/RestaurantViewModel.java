package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.DailySchedule;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.interactors.restaurantInteractors.AddDailyScheduleInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurant.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurant.DeleteDailyScheduleForRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant.AddUserInGlobalListInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurant.GetAllRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.GetDailyScheduleInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant.DeleteUserInRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant.GetGlobalInterestedUserInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant.GetInterestedUsersForRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurant.GetRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant.GetUserForRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant.UpdateInterestedUsersInteractor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RestaurantViewModel extends ViewModel {

    public enum CompletionState {
        START_STATE,
        RESTAURANT_ON_COMPLETE,
        RESTAURANT_ON_ERROR,
        RESTAURANT_IS_PERSISTED,
        RESTAURANT_IS_NOT_PERSISTED,
        DAILY_SCHEDULE_ON_COMPLETE,
        DAILY_SCHEDULE_ON_ERROR,
        DAILY_SCHEDULE_IS_PERSISTED,
        DAILY_SCHEDULE_IS_NOT_PERSISTED,
        PERSISTED_USERS_FOR_RESTAURANT_ON_COMPLETE,
        PERSISTED_USERS_FOR_RESTAURANT_ON_ERROR,
        INTERESTED_USERS_ARE_PERSISTED

    }

    private final MutableLiveData<CompletionState> onCompleteStateMutableLiveData = new MutableLiveData<>();
    public LiveData<CompletionState> getCompletionState = onCompleteStateMutableLiveData;
    private MutableLiveData<List<Restaurant>> allRestaurantsMutableLiveData = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Restaurant> restaurantMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Restaurant> _getSelectedRestaurant = new MutableLiveData<>();
    public LiveData<Restaurant> getSelectedRestaurant = _getSelectedRestaurant;
    private MutableLiveData<DailySchedule> dailyScheduleMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> interestedUsersMutableLiveData = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<User> globalInterestedUserMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<User> userForRestaurantMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> _isUserHasChoseRestaurant = new MutableLiveData<>(false);
    public LiveData<Boolean> isUserHasChoseRestaurant = _isUserHasChoseRestaurant;
    //interactors
    private AddRestaurantInteractor addRestaurantInteractor;
    private GetAllRestaurantsInteractor getAllRestaurantsInteractor;
    private GetRestaurantInteractor getRestaurantInteractor;
    private GetDailyScheduleInteractor getDailyScheduleInteractor;
    private AddDailyScheduleInteractor addDailyScheduleInteractor;
    private DeleteDailyScheduleForRestaurantInteractor deleteDailyScheduleForRestaurantInteractor;
    private GetInterestedUsersForRestaurantInteractor getInterestedUsersForRestaurantInteractor;
    private UpdateInterestedUsersInteractor updateInterestedUsersInteractor;
    private GetGlobalInterestedUserInteractor getGlobalInterestedUserInteractor;
    private AddUserInGlobalListInteractor addUserInGlobalListInteractor;
    private DeleteUserInRestaurantInteractor deleteUserInRestaurantInteractor;
    private GetUserForRestaurantInteractor getUserForRestaurantInteractor;


    @ViewModelInject
    public RestaurantViewModel(GetAllRestaurantsInteractor getAllRestaurantsInteractor
            , GetRestaurantInteractor getRestaurantInteractor, GetDailyScheduleInteractor getDailyScheduleInteractor
            , UpdateInterestedUsersInteractor updateInterestedUsersInteractor
            , AddDailyScheduleInteractor addDailyScheduleInteractor, GetInterestedUsersForRestaurantInteractor getInterestedUsersForRestaurantInteractor
            , AddRestaurantInteractor addRestaurantInteractor, GetGlobalInterestedUserInteractor getGlobalInterestedUserInteractor
            , AddUserInGlobalListInteractor addUserInGlobalListInteractor, DeleteUserInRestaurantInteractor deleteUserInRestaurantInteractor
            , GetUserForRestaurantInteractor getUserForRestaurantInteractor, DeleteDailyScheduleForRestaurantInteractor deleteDailyScheduleForRestaurantInteractor) {

        this.addRestaurantInteractor = addRestaurantInteractor;
        this.getAllRestaurantsInteractor = getAllRestaurantsInteractor;
        this.getRestaurantInteractor = getRestaurantInteractor;
        this.getDailyScheduleInteractor = getDailyScheduleInteractor;
        this.addDailyScheduleInteractor = addDailyScheduleInteractor;
        this.deleteDailyScheduleForRestaurantInteractor = deleteDailyScheduleForRestaurantInteractor;
        this.updateInterestedUsersInteractor = updateInterestedUsersInteractor;
        this.getInterestedUsersForRestaurantInteractor = getInterestedUsersForRestaurantInteractor;
        this.getGlobalInterestedUserInteractor = getGlobalInterestedUserInteractor;
        this.addUserInGlobalListInteractor = addUserInGlobalListInteractor;
        this.deleteUserInRestaurantInteractor = deleteUserInRestaurantInteractor;
        this.getUserForRestaurantInteractor = getUserForRestaurantInteractor;
    }

    //----------------------------- Restaurant -----------------------------------

    public void addRestaurant(Restaurant restaurant) {
        addRestaurantInteractor.addRestaurant(restaurant)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("test", "onComplete: restaurant added");
                        onCompleteStateMutableLiveData.setValue(CompletionState.RESTAURANT_ON_COMPLETE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onCompleteStateMutableLiveData.setValue(CompletionState.RESTAURANT_ON_ERROR);

                    }
                });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    //suppress warning is safe cause getAllRestaurantsInteractor is used to set
    //allRestaurantsMutableLiveData value
    public LiveData<List<Restaurant>> getAllRestaurants() {
        getAllRestaurantsInteractor.getAllRestaurants()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restaurants -> allRestaurantsMutableLiveData.setValue(restaurants));
        return allRestaurantsMutableLiveData;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    //suppress warning is safe cause getDailyScheduleInteractor is used to set
    //dailyScheduleMutableLiveData value
    public LiveData<Restaurant> getRestaurant(String restaurantName) {
        onCompleteStateMutableLiveData.setValue(CompletionState.RESTAURANT_IS_NOT_PERSISTED);
        getRestaurantInteractor.getRestaurant(restaurantName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Restaurant>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Restaurant restaurant) {
                        restaurantMutableLiveData.setValue(restaurant);
                        onCompleteStateMutableLiveData.setValue(CompletionState.RESTAURANT_IS_PERSISTED);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return restaurantMutableLiveData;
    }

    public List<Restaurant> getGeneratorRestaurants() {
        return getAllRestaurantsInteractor.getGeneratedRestaurants();
    }

    public void setSelectedRestaurant(int position) {
        _getSelectedRestaurant.setValue(getRestaurantInteractor.getGeneratorRestaurant(position));
    }

    //-------------------------- Daily schedule ---------------------------------------


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    //suppress warning is safe cause getDailyScheduleInteractor is used to set
    //dailyScheduleMutableLiveData value
    public LiveData<DailySchedule> getDailyScheduleForRestaurant(String restaurantName) {
        onCompleteStateMutableLiveData.setValue(CompletionState.DAILY_SCHEDULE_IS_NOT_PERSISTED);
        getDailyScheduleInteractor.getDailyScheduleForRestaurant(restaurantName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DailySchedule>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DailySchedule dailySchedule) {
                        dailyScheduleMutableLiveData.setValue(dailySchedule);
                        onCompleteStateMutableLiveData.setValue(CompletionState.DAILY_SCHEDULE_IS_PERSISTED);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return dailyScheduleMutableLiveData;
    }

    public void deleteDailyScheduleForRestaurant(Restaurant selectedRestaurant){
        deleteDailyScheduleForRestaurantInteractor.deleteDailyScheduleForRestaurant(selectedRestaurant);
    }

    public void addDailySchedule(Date today, List<User> interestedUsers, Restaurant restaurant) {
        DailySchedule dailySchedule = new DailySchedule(today.toString(), interestedUsers);
        addDailyScheduleInteractor.addDailySchedule(dailySchedule, restaurant)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        onCompleteStateMutableLiveData.setValue(CompletionState.DAILY_SCHEDULE_ON_COMPLETE);

                        Log.i("daily schedule", "onComplete: DailySchedule Added");
                    }

                    @Override
                    public void onError(Throwable e) {
                        onCompleteStateMutableLiveData.setValue(CompletionState.DAILY_SCHEDULE_ON_ERROR);

                    }
                });
    }

    //------------------------------ Interested users --------------------------------

    public void updateInterestedUsers(Date today, String restaurantName, User user) {
        updateInterestedUsersInteractor.updateInterestedUser(today, restaurantName, user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        onCompleteStateMutableLiveData.setValue(CompletionState.PERSISTED_USERS_FOR_RESTAURANT_ON_COMPLETE);
                        Log.i("addInterestedUser", "onComplete:  user added");
                    }

                    @Override
                    public void onError(Throwable e) {
                        onCompleteStateMutableLiveData.setValue(CompletionState.PERSISTED_USERS_FOR_RESTAURANT_ON_ERROR);
                    }
                });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    //suppress warning is safe cause getInterestedUsersForRestaurantInteractor is used to set
    //interestedUsersMutableLiveData value
    public LiveData<List<User>> getInterestedUsersForRestaurant(Date today, String restaurantName) {
        getInterestedUsersForRestaurantInteractor.getInterestedUserForRestaurant(today, restaurantName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<User> users) {
                        interestedUsersMutableLiveData.setValue(users);
                        onCompleteStateMutableLiveData.setValue(CompletionState.INTERESTED_USERS_ARE_PERSISTED);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return interestedUsersMutableLiveData;
    }

    public void addUserInGlobalList(User currentUser) {
        addUserInGlobalListInteractor.addUserInGlobalList(currentUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("onComplete", "onComplete: user added in global list");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @SuppressLint("CheckResult")
    //suppress warning is safe cause getGlobalInterestedUsersInteractor is used to set
    //globalInterestedUsersMutableLiveData value
    public LiveData<User> getGlobalInterestedUser(User user) {

        getGlobalInterestedUserInteractor.getGlobalInterestedUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {


                    }

                    @Override
                    public void onNext(User user) {
                        globalInterestedUserMutableLiveData.setValue(user);
                        _isUserHasChoseRestaurant.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        _isUserHasChoseRestaurant.setValue(false);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
        return globalInterestedUserMutableLiveData;
    }

    @SuppressLint("CheckResult")
    public LiveData<User> getUserForRestaurant(Date today, Restaurant originalChosenRestaurant, User currentUser){
        getUserForRestaurantInteractor.getUserForRestaurant(today, originalChosenRestaurant, currentUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> userForRestaurantMutableLiveData.setValue(user));

        return userForRestaurantMutableLiveData;
    }

    public void deleteUserInRestaurant(Date today, Restaurant originalChosenRestaurant, User currentUser){
        deleteUserInRestaurantInteractor.deleteUserForRestaurant(today, originalChosenRestaurant, currentUser);
    }
}
