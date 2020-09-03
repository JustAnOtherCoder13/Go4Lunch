package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.DailySchedule;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.interactors.restaurantInteractors.dailySchedule.AddDailyScheduleToRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.dailySchedule.DeleteDailyScheduleFromRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.dailySchedule.GetDailyScheduleInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.globalUserForRestaurant.AddCurrentUserToGlobalListInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.globalUserForRestaurant.DeleteUserFromGlobalListInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.globalUserForRestaurant.GetAllGlobalUsersInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.globalUserForRestaurant.GetGlobalCurrentUserInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurant.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurant.GetAllRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.restaurant.GetRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant.AddCurrentUserToRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant.DeleteCurrentUserFromRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.userForRestaurant.GetAllInterestedUsersForRestaurantInteractor;

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
        RESTAURANT_IS_NOT_PERSISTED,
        RESTAURANT_IS_PERSISTED,
        DAILY_SCHEDULE_ON_COMPLETE,
        DAILY_SCHEDULE_ON_ERROR,
        DAILY_SCHEDULE_IS_LOADED,
        DAILY_SCHEDULE_IS_NOT_LOADED,
        PERSISTED_USERS_FOR_RESTAURANT_ON_COMPLETE,
        PERSISTED_USERS_FOR_RESTAURANT_ON_ERROR,
        INTERESTED_USERS_LOAD_ON_COMPLETE,
        CURRENT_USER_TO_GLOBAL_LIST_ON_COMPLETE,
        CURRENT_USER_TO_GLOBAL_LIST_ON_ERROR,
        DELETE_USER_ON_COMPLETE,
        DELETE_USER_ON_ERROR,
        DELETE_DAILY_SCHEDULE_ON_COMPLETE,
        DELETE_DAILY_SCHEDULE_ON_ERROR,
        DELETE_GLOBAL_USER_ON_COMPLETE,
        DELETE_GLOBAL_USER_ON_ERROR,
        GLOBAL_USERS_ON_NEXT,
        GLOBAL_USER_SUBSCRIBE,
        GLOBAL_USER_LOAD_COMPLETE
    }

    private final MutableLiveData<CompletionState> _getCompletionState = new MutableLiveData<>();
    public LiveData<CompletionState> getCompletionState = _getCompletionState;


    private MutableLiveData<Restaurant> restaurantMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Restaurant> _getSelectedRestaurant = new MutableLiveData<>();
    public LiveData<Restaurant> getSelectedRestaurant = _getSelectedRestaurant;

    private MutableLiveData<DailySchedule> dailyScheduleMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<List<User>> interestedUsersMutableLiveData = new MutableLiveData<>(new ArrayList<>());

    private MutableLiveData<List<User>> allGlobalUsersMutableLiveData = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<User> globalInterestedUserMutableLiveData = new MutableLiveData<>();

    //interactors
    private GetAllRestaurantsInteractor getAllRestaurantsInteractor;
    private GetRestaurantInteractor getRestaurantInteractor;
    private AddRestaurantInteractor addRestaurantInteractor;

    private GetDailyScheduleInteractor getDailyScheduleInteractor;
    private AddDailyScheduleToRestaurantInteractor addDailyScheduleToRestaurantInteractor;
    private DeleteDailyScheduleFromRestaurantInteractor deleteDailyScheduleFromRestaurantInteractor;

    private GetAllInterestedUsersForRestaurantInteractor getAllInterestedUsersForRestaurantInteractor;
    private AddCurrentUserToRestaurantInteractor addCurrentUserToRestaurantInteractor;
    private DeleteCurrentUserFromRestaurantInteractor deleteCurrentUserFromRestaurantInteractor;

    private GetGlobalCurrentUserInteractor getGlobalCurrentUserInteractor;
    private GetAllGlobalUsersInteractor getAllGlobalUsersInteractor;
    private AddCurrentUserToGlobalListInteractor addCurrentUserToGlobalListInteractor;
    private DeleteUserFromGlobalListInteractor deleteUserFromGlobalListInteractor;


    private final SavedStateHandle savedStateHandle;

    @ViewModelInject
    public RestaurantViewModel(GetAllRestaurantsInteractor getAllRestaurantsInteractor
            , GetRestaurantInteractor getRestaurantInteractor, AddRestaurantInteractor addRestaurantInteractor
            , GetDailyScheduleInteractor getDailyScheduleInteractor, AddDailyScheduleToRestaurantInteractor addDailyScheduleToRestaurantInteractor
            , DeleteDailyScheduleFromRestaurantInteractor deleteDailyScheduleFromRestaurantInteractor, GetAllInterestedUsersForRestaurantInteractor getAllInterestedUsersForRestaurantInteractor
            , AddCurrentUserToRestaurantInteractor addCurrentUserToRestaurantInteractor
            , DeleteCurrentUserFromRestaurantInteractor deleteCurrentUserFromRestaurantInteractor, GetGlobalCurrentUserInteractor getGlobalCurrentUserInteractor
            , GetAllGlobalUsersInteractor getAllGlobalUsersInteractor, AddCurrentUserToGlobalListInteractor addCurrentUserToGlobalListInteractor
            , DeleteUserFromGlobalListInteractor deleteUserFromGlobalListInteractor, @Assisted SavedStateHandle savedStateHandle) {

        this.getAllRestaurantsInteractor = getAllRestaurantsInteractor;
        this.getRestaurantInteractor = getRestaurantInteractor;
        this.addRestaurantInteractor = addRestaurantInteractor;

        this.getDailyScheduleInteractor = getDailyScheduleInteractor;
        this.addDailyScheduleToRestaurantInteractor = addDailyScheduleToRestaurantInteractor;
        this.deleteDailyScheduleFromRestaurantInteractor = deleteDailyScheduleFromRestaurantInteractor;

        this.getAllInterestedUsersForRestaurantInteractor = getAllInterestedUsersForRestaurantInteractor;
        this.addCurrentUserToRestaurantInteractor = addCurrentUserToRestaurantInteractor;
        this.deleteCurrentUserFromRestaurantInteractor = deleteCurrentUserFromRestaurantInteractor;

        this.getGlobalCurrentUserInteractor = getGlobalCurrentUserInteractor;
        this.getAllGlobalUsersInteractor = getAllGlobalUsersInteractor;
        this.addCurrentUserToGlobalListInteractor = addCurrentUserToGlobalListInteractor;
        this.deleteUserFromGlobalListInteractor = deleteUserFromGlobalListInteractor;

        this.savedStateHandle = savedStateHandle;
    }


    public LiveData<CompletionState> resetCompletionState() {
        _getCompletionState.setValue(CompletionState.START_STATE);
        return _getCompletionState;
    }

    //--------------------------RESTAURANT---------------------------------
    @SuppressLint("CheckResult")
    //suppress warning is safe cause getDailyScheduleInteractor is used to set
    //dailyScheduleMutableLiveData value
    public LiveData<Restaurant> getPersistedRestaurant(String restaurantName) {
        getRestaurantInteractor.getPersistedRestaurant(restaurantName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Restaurant>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        _getCompletionState.setValue(CompletionState.RESTAURANT_IS_NOT_PERSISTED);
                    }

                    @Override
                    public void onNext(Restaurant restaurant) {
                        restaurantMutableLiveData.setValue(restaurant);
                        _getCompletionState.setValue(CompletionState.RESTAURANT_IS_PERSISTED);

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
                        _getCompletionState.setValue(CompletionState.RESTAURANT_ON_COMPLETE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        _getCompletionState.setValue(CompletionState.RESTAURANT_ON_ERROR);

                    }
                });
    }

    public List<Restaurant> getGeneratedRestaurants() {
        return getAllRestaurantsInteractor.getGeneratedRestaurants();
    }

    public void setSelectedRestaurant(int position) {
        _getSelectedRestaurant.setValue(getRestaurantInteractor.getGeneratorRestaurant(position));
    }
    public void setSelectedRestaurant(Restaurant restaurant){
        _getSelectedRestaurant.setValue(restaurant);
    }

    //--------------------------------DAILY_SCHEDULE-----------------------------------
    @SuppressLint("CheckResult")
    //suppress warning is safe cause getDailyScheduleInteractor is used to set
    //dailyScheduleMutableLiveData value
    public LiveData<DailySchedule> getDailyScheduleForRestaurant(String restaurantName) {
        _getCompletionState.setValue(CompletionState.DAILY_SCHEDULE_IS_NOT_LOADED);
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
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        _getCompletionState.setValue(CompletionState.DAILY_SCHEDULE_IS_LOADED);
                    }
                });
        return dailyScheduleMutableLiveData;
    }

    public void addDailyScheduleToRestaurant(Date today, List<User> interestedUsers, String selectedRestaurantName) {
        DailySchedule dailySchedule = new DailySchedule(today, interestedUsers);
        addDailyScheduleToRestaurantInteractor.addDailyScheduleToRestaurant(dailySchedule, selectedRestaurantName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        _getCompletionState.setValue(CompletionState.DAILY_SCHEDULE_ON_COMPLETE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        _getCompletionState.setValue(CompletionState.DAILY_SCHEDULE_ON_ERROR);
                    }
                });
    }

    public void deleteDailyScheduleFromRestaurant(String selectedRestaurantName) {
        deleteDailyScheduleFromRestaurantInteractor.deleteDailyScheduleFromRestaurant(selectedRestaurantName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        _getCompletionState.setValue(CompletionState.DELETE_DAILY_SCHEDULE_ON_COMPLETE);

                    }

                    @Override
                    public void onError(Throwable e) {
                        _getCompletionState.setValue(CompletionState.DELETE_DAILY_SCHEDULE_ON_ERROR);
                    }
                });
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
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<User> interestedUsersForRestaurant) {
                        interestedUsersMutableLiveData.setValue(interestedUsersForRestaurant);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        _getCompletionState.setValue(CompletionState.INTERESTED_USERS_LOAD_ON_COMPLETE);
                    }


                });
        return interestedUsersMutableLiveData;
    }

    public void addCurrentUserToRestaurant(Date today, String restaurantName, User currentUser) {
        addCurrentUserToRestaurantInteractor.addCurrentUserToRestaurant(today, restaurantName, currentUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        _getCompletionState.setValue(CompletionState.PERSISTED_USERS_FOR_RESTAURANT_ON_COMPLETE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        _getCompletionState.setValue(CompletionState.PERSISTED_USERS_FOR_RESTAURANT_ON_ERROR);
                    }
                });
    }

    public void deleteCurrentUserFromRestaurant(Date today, String originalChosenRestaurantName, User currentUser) {
        deleteCurrentUserFromRestaurantInteractor.deleteCurrentUserFromRestaurant(today, originalChosenRestaurantName, currentUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        _getCompletionState.setValue(CompletionState.DELETE_USER_ON_COMPLETE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        _getCompletionState.setValue(CompletionState.DELETE_USER_ON_ERROR);

                    }
                });
    }

    public void resetInterestedUsers() {
        interestedUsersMutableLiveData.setValue(new ArrayList<>());
    }


    //-----------------------------GLOBAL_INTERESTED_USER----------------------------

    //suppress warning is safe cause getGlobalInterestedUsersInteractor is used to set
    //globalInterestedUsersMutableLiveData value
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public LiveData<User> getGlobalCurrentUser(User currentUser) {
        getGlobalCurrentUserInteractor.getGlobalCurrentUser(currentUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(User user) {
                        globalInterestedUserMutableLiveData.setValue(user);
                        _getCompletionState.setValue(CompletionState.GLOBAL_USERS_ON_NEXT);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        _getCompletionState.setValue(CompletionState.GLOBAL_USER_LOAD_COMPLETE);

                    }

                });
        return globalInterestedUserMutableLiveData;
    }

    public void addCurrentUserToGlobalList(User currentUser) {
        addCurrentUserToGlobalListInteractor.addCurrentUserToGlobalList(currentUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        _getCompletionState.setValue(CompletionState.GLOBAL_USER_SUBSCRIBE);
                    }

                    @Override
                    public void onComplete() {
                        _getCompletionState.setValue(CompletionState.CURRENT_USER_TO_GLOBAL_LIST_ON_COMPLETE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        _getCompletionState.setValue(CompletionState.CURRENT_USER_TO_GLOBAL_LIST_ON_ERROR);

                    }
                });
    }

    public void deleteUserFromGlobalList(User globalPersistedUser) {
        deleteUserFromGlobalListInteractor.deleteUserFromGlobalList(globalPersistedUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        _getCompletionState.setValue(CompletionState.DELETE_GLOBAL_USER_ON_COMPLETE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        _getCompletionState.setValue(CompletionState.DELETE_GLOBAL_USER_ON_ERROR);

                    }
                });
    }

    @SuppressLint("CheckResult")
    public LiveData<List<User>> getAllGlobalUsers() {
        getAllGlobalUsersInteractor.getAllGlobalUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<User> allGlobalUsers) {
                        allGlobalUsersMutableLiveData.setValue(allGlobalUsers);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }


                });
        return allGlobalUsersMutableLiveData;
    }

    public void resetGlobalUsers(){
        allGlobalUsersMutableLiveData.setValue(new ArrayList<>());
    }
}
