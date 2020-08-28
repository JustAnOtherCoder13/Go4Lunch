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
import com.picone.core.domain.interactors.restaurantInteractors.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.AddUserInGlobalListInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.GetAllRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.GetDailyScheduleInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.GetGlobalInterestedUserInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.GetInterestedUsersForRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.GetRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.UpdateInterestedUsersInteractor;

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
        DAILY_SCHEDULE_ON_COMPLETE,
        DAILY_SCHEDULE_ON_ERROR,
        PERSISTED_USERS_FOR_RESTAURANT_ON_COMPLETE,
        PERSISTED_USERS_FOR_RESTAURANT_ON_ERROR,
    }

    private final MutableLiveData<CompletionState> onCompleteStateMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Restaurant>> allRestaurantsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Restaurant> restaurantMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Restaurant> selectedRestaurantMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<DailySchedule> dailyScheduleMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> interestedUsersMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> interestedColleagueMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<User> globalInterestedUsersMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> _isUserHasChoseRestaurant = new MutableLiveData<>();
    public LiveData<Boolean> isUserHasChoseRestaurant = _isUserHasChoseRestaurant;
    //interactors
    private AddRestaurantInteractor addRestaurantInteractor;
    private GetAllRestaurantsInteractor getAllRestaurantsInteractor;
    private GetRestaurantInteractor getRestaurantInteractor;
    private GetDailyScheduleInteractor getDailyScheduleInteractor;
    private AddDailyScheduleInteractor addDailyScheduleInteractor;
    private GetInterestedUsersForRestaurantInteractor getInterestedUsersForRestaurantInteractor;
    private UpdateInterestedUsersInteractor updateInterestedUsersInteractor;
    private GetGlobalInterestedUserInteractor getGlobalInterestedUserInteractor;
    private AddUserInGlobalListInteractor addUserInGlobalListInteractor;


    @ViewModelInject
    public RestaurantViewModel(GetAllRestaurantsInteractor getAllRestaurantsInteractor
            , GetRestaurantInteractor getRestaurantInteractor, GetDailyScheduleInteractor getDailyScheduleInteractor
            , UpdateInterestedUsersInteractor updateInterestedUsersInteractor
            , AddDailyScheduleInteractor addDailyScheduleInteractor, GetInterestedUsersForRestaurantInteractor getInterestedUsersForRestaurantInteractor
            , AddRestaurantInteractor addRestaurantInteractor, GetGlobalInterestedUserInteractor getGlobalInterestedUserInteractor
            , AddUserInGlobalListInteractor addUserInGlobalListInteractor) {

        this.addRestaurantInteractor = addRestaurantInteractor;
        this.getAllRestaurantsInteractor = getAllRestaurantsInteractor;
        this.getRestaurantInteractor = getRestaurantInteractor;
        this.getDailyScheduleInteractor = getDailyScheduleInteractor;
        this.addDailyScheduleInteractor = addDailyScheduleInteractor;
        this.updateInterestedUsersInteractor = updateInterestedUsersInteractor;
        this.getInterestedUsersForRestaurantInteractor = getInterestedUsersForRestaurantInteractor;
        this.getGlobalInterestedUserInteractor = getGlobalInterestedUserInteractor;
        this.addUserInGlobalListInteractor = addUserInGlobalListInteractor;
        this.allRestaurantsMutableLiveData.setValue(new ArrayList<>());
        this.interestedColleagueMutableLiveData.setValue(new ArrayList<>());
    }

    public LiveData<CompletionState> getCompletionState() {
        return onCompleteStateMutableLiveData;
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
        getRestaurantInteractor.getRestaurant(restaurantName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restaurant -> restaurantMutableLiveData.setValue(restaurant));
        return restaurantMutableLiveData;
    }

    public List<Restaurant> getGeneratorRestaurants() {
        return getAllRestaurantsInteractor.getGeneratedRestaurants();
    }

    public void setSelectedRestaurant(int position) {
        selectedRestaurantMutableLiveData.setValue(getRestaurantInteractor.getGeneratorRestaurant(position));
    }

    public LiveData<Restaurant> getSelectedRestaurant() {
        return selectedRestaurantMutableLiveData;
    }

    //-------------------------- Daily schedule ---------------------------------------


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    //suppress warning is safe cause getDailyScheduleInteractor is used to set
    //dailyScheduleMutableLiveData value
    public LiveData<DailySchedule> getDailyScheduleForRestaurant(String restaurantName) {
        getDailyScheduleInteractor.getDailyScheduleForRestaurant(restaurantName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dailySchedule -> dailyScheduleMutableLiveData.setValue(dailySchedule));
        return dailyScheduleMutableLiveData;
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
                .subscribe(interestedUsers -> interestedUsersMutableLiveData.setValue(interestedUsers));
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
    public LiveData<User> getGlobalInterestedUsers(User user) {

        getGlobalInterestedUserInteractor.getGlobalInterestedUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        _isUserHasChoseRestaurant.setValue(false);
                    }

                    @Override
                    public void onNext(User user) {
                        globalInterestedUsersMutableLiveData.setValue(user);
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
        return globalInterestedUsersMutableLiveData;
    }

}
