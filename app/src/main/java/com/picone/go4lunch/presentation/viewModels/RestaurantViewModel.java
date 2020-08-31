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
import com.picone.core.domain.interactors.restaurantInteractors.dailySchedule.AddDailyScheduleToRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.globalUserForRestaurant.AddCurrentUserToGlobalListInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.globalUserForRestaurant.GetGlobalCurrentUserInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurant.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.dailySchedule.DeleteDailyScheduleForRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurant.GetAllRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.dailySchedule.GetDailyScheduleInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.globalUserForRestaurant.DeleteUserFromGlobalListInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant.DeleteCurrentUserFromRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant.GetAllInterestedUsersForRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant.GetCurrentUserForRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurant.GetRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant.AddCurrentUserToRestaurantInteractor;

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
        INTERESTED_USERS_ARE_PERSISTED,
        CURRENT_USER_TO_GLOBAL_LIST_ON_COMPLETE,
        CURRENT_USER_TO_GLOBAL_LIST_ON_ERROR
    }

    private final MutableLiveData<CompletionState> onCompleteStateMutableLiveData = new MutableLiveData<>();
    public LiveData<CompletionState> getCompletionState = onCompleteStateMutableLiveData;

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
    private GetAllRestaurantsInteractor getAllRestaurantsInteractor;
    private GetRestaurantInteractor getRestaurantInteractor;
    private AddRestaurantInteractor addRestaurantInteractor;

    private GetDailyScheduleInteractor getDailyScheduleInteractor;
    private AddDailyScheduleToRestaurantInteractor addDailyScheduleToRestaurantInteractor;
    private DeleteDailyScheduleForRestaurantInteractor deleteDailyScheduleForRestaurantInteractor;

    private GetAllInterestedUsersForRestaurantInteractor getAllInterestedUsersForRestaurantInteractor;
    private GetCurrentUserForRestaurantInteractor getCurrentUserForRestaurantInteractor;
    private AddCurrentUserToRestaurantInteractor addCurrentUserToRestaurantInteractor;
    private DeleteCurrentUserFromRestaurantInteractor deleteCurrentUserFromRestaurantInteractor;

    private GetGlobalCurrentUserInteractor getGlobalCurrentUserInteractor;
    private AddCurrentUserToGlobalListInteractor addCurrentUserToGlobalListInteractor;
    private DeleteUserFromGlobalListInteractor deleteUserFromGlobalListInteractor;


    @ViewModelInject
    public RestaurantViewModel(GetAllRestaurantsInteractor getAllRestaurantsInteractor
            , GetRestaurantInteractor getRestaurantInteractor, AddRestaurantInteractor addRestaurantInteractor
            , GetDailyScheduleInteractor getDailyScheduleInteractor, AddDailyScheduleToRestaurantInteractor addDailyScheduleToRestaurantInteractor
            , DeleteDailyScheduleForRestaurantInteractor deleteDailyScheduleForRestaurantInteractor, GetAllInterestedUsersForRestaurantInteractor getAllInterestedUsersForRestaurantInteractor
            , GetCurrentUserForRestaurantInteractor getCurrentUserForRestaurantInteractor, AddCurrentUserToRestaurantInteractor addCurrentUserToRestaurantInteractor
            , DeleteCurrentUserFromRestaurantInteractor deleteCurrentUserFromRestaurantInteractor, GetGlobalCurrentUserInteractor getGlobalCurrentUserInteractor
            , AddCurrentUserToGlobalListInteractor addCurrentUserToGlobalListInteractor, DeleteUserFromGlobalListInteractor deleteUserFromGlobalListInteractor) {

        this.getAllRestaurantsInteractor = getAllRestaurantsInteractor;
        this.getRestaurantInteractor = getRestaurantInteractor;
        this.addRestaurantInteractor = addRestaurantInteractor;

        this.getDailyScheduleInteractor = getDailyScheduleInteractor;
        this.addDailyScheduleToRestaurantInteractor = addDailyScheduleToRestaurantInteractor;
        this.deleteDailyScheduleForRestaurantInteractor = deleteDailyScheduleForRestaurantInteractor;

        this.getAllInterestedUsersForRestaurantInteractor = getAllInterestedUsersForRestaurantInteractor;
        this.getCurrentUserForRestaurantInteractor = getCurrentUserForRestaurantInteractor;
        this.addCurrentUserToRestaurantInteractor = addCurrentUserToRestaurantInteractor;
        this.deleteCurrentUserFromRestaurantInteractor = deleteCurrentUserFromRestaurantInteractor;

        this.getGlobalCurrentUserInteractor = getGlobalCurrentUserInteractor;
        this.addCurrentUserToGlobalListInteractor = addCurrentUserToGlobalListInteractor;
        this.deleteUserFromGlobalListInteractor = deleteUserFromGlobalListInteractor;
    }

    //--------------------------RESTAURANT---------------------------------
    @SuppressLint("CheckResult")
    //suppress warning is safe cause getDailyScheduleInteractor is used to set
    //dailyScheduleMutableLiveData value
    public LiveData<Restaurant> getPersistedRestaurant(String restaurantName) {
        onCompleteStateMutableLiveData.setValue(CompletionState.RESTAURANT_IS_NOT_PERSISTED);
        getRestaurantInteractor.getPersistedRestaurant(restaurantName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Restaurant>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(Restaurant restaurant) {
                        restaurantMutableLiveData.setValue(restaurant);
                        onCompleteStateMutableLiveData.setValue(CompletionState.RESTAURANT_IS_PERSISTED);
                    }
                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {}
                });
        return restaurantMutableLiveData;
    }
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
                        onCompleteStateMutableLiveData.setValue(CompletionState.RESTAURANT_ON_COMPLETE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onCompleteStateMutableLiveData.setValue(CompletionState.RESTAURANT_ON_ERROR);

                    }
                });
    }

    public List<Restaurant> getGeneratedRestaurants() {
        return getAllRestaurantsInteractor.getGeneratedRestaurants();
    }

    public void setSelectedRestaurant(int position) {
        _getSelectedRestaurant.setValue(getRestaurantInteractor.getGeneratorRestaurant(position));
    }

    //--------------------------------DAILY_SCHEDULE-----------------------------------
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
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(DailySchedule dailySchedule) {
                        dailyScheduleMutableLiveData.setValue(dailySchedule);
                        onCompleteStateMutableLiveData.setValue(CompletionState.DAILY_SCHEDULE_IS_PERSISTED);
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() { }
                });
        return dailyScheduleMutableLiveData;
    }

    public void addDailyScheduleToRestaurant(Date today, List<User> interestedUsers, String selectedRestaurantName) {
        DailySchedule dailySchedule = new DailySchedule(today.toString(), interestedUsers);
        addDailyScheduleToRestaurantInteractor.addDailyScheduleToRestaurant(dailySchedule, selectedRestaurantName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onComplete() {
                        onCompleteStateMutableLiveData.setValue(CompletionState.DAILY_SCHEDULE_ON_COMPLETE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onCompleteStateMutableLiveData.setValue(CompletionState.DAILY_SCHEDULE_ON_ERROR);
                    }
                });
    }

    public void deleteDailyScheduleForRestaurant(String selectedRestaurantName) {
        deleteDailyScheduleForRestaurantInteractor.deleteDailyScheduleFromRestaurant(selectedRestaurantName);
    }

    //----------------------------INTERESTED_USER_FOR_RESTAURANT----------------------------------
    @SuppressLint("CheckResult")
    //suppress warning is safe cause getInterestedUsersForRestaurantInteractor is used to set
    //interestedUsersMutableLiveData value
    public LiveData<List<User>> getAllInterestedUsersForRestaurant(Date today, String restaurantName) {
        getAllInterestedUsersForRestaurantInteractor.getAllInterestedUsersForRestaurant(today, restaurantName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(List<User> users) {
                        interestedUsersMutableLiveData.setValue(users);
                        onCompleteStateMutableLiveData.setValue(CompletionState.INTERESTED_USERS_ARE_PERSISTED);
                    }

                    @Override
                    public void onError(Throwable e) { }

                    @Override
                    public void onComplete() {}
                });
        return interestedUsersMutableLiveData;
    }

    //suppress warning is safe cause getCurrentUserForRestaurantInteractor is used to set
    //userForRestaurantMutableLiveData value
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public LiveData<User> getCurrentUserForRestaurant(Date today, String selectedRestaurantName, User currentUser) {
        getCurrentUserForRestaurantInteractor.getCurrentUserForRestaurant(today, selectedRestaurantName, currentUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> userForRestaurantMutableLiveData.setValue(user));

        return userForRestaurantMutableLiveData;
    }

    public void addCurrentUserToRestaurant(Date today, String restaurantName, User currentUser) {
        addCurrentUserToRestaurantInteractor.addCurrentUserToRestaurant(today, restaurantName, currentUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onComplete() {
                        onCompleteStateMutableLiveData.setValue(CompletionState.PERSISTED_USERS_FOR_RESTAURANT_ON_COMPLETE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onCompleteStateMutableLiveData.setValue(CompletionState.PERSISTED_USERS_FOR_RESTAURANT_ON_ERROR);
                    }
                });
    }

    public void deleteCurrentUserFromRestaurant(Date today, String originalChosenRestaurantName, User currentUser) {
        deleteCurrentUserFromRestaurantInteractor.deleteCurrentUserFromRestaurant(today, originalChosenRestaurantName, currentUser);
    }

    //-----------------------------GLOBAL_INTERESTED_USER----------------------------

    @SuppressLint("CheckResult")
    //suppress warning is safe cause getGlobalInterestedUsersInteractor is used to set
    //globalInterestedUsersMutableLiveData value
    public LiveData<User> getGlobalCurrentUser(User currentUser) {
        getGlobalCurrentUserInteractor.getGlobalCurrentUser(currentUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

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
                    public void onComplete() { }
                });
        return globalInterestedUserMutableLiveData;
    }


    public void addCurrentUserToGlobalList(User currentUser) {
        addCurrentUserToGlobalListInteractor.addCurrentUserToGlobalList(currentUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onComplete() {
                        onCompleteStateMutableLiveData.setValue(CompletionState.CURRENT_USER_TO_GLOBAL_LIST_ON_COMPLETE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onCompleteStateMutableLiveData.setValue(CompletionState.CURRENT_USER_TO_GLOBAL_LIST_ON_ERROR);

                    }
                });
    }

    public void deleteUserFromGlobalList(User globalPersistedUser) {
        deleteUserFromGlobalListInteractor.deleteUserFromGlobalList(globalPersistedUser);
    }
}
