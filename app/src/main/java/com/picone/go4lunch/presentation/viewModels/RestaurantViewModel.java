package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.interactors.restaurantInteractors.GetAllRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.GetInterestedColleagueInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.GetRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantInteractors.UpdateInterestedColleagueInteractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RestaurantViewModel extends ViewModel {

    private MutableLiveData<List<Restaurant>> allRestaurantsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Restaurant> restaurantMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> interestedColleagueMutableLiveData = new MutableLiveData<>();
    //interactors
    private GetAllRestaurantsInteractor getAllRestaurantsInteractor;
    private GetRestaurantInteractor getRestaurant;
    private GetInterestedColleagueInteractor getInterestedColleagueInteractor;
    private UpdateInterestedColleagueInteractor updateInterestedColleagueInteractor;

    @ViewModelInject
    public RestaurantViewModel(GetAllRestaurantsInteractor getAllRestaurantsInteractor, GetRestaurantInteractor getRestaurant
            , GetInterestedColleagueInteractor getInterestedColleagueInteractor,
                               UpdateInterestedColleagueInteractor updateInterestedColleagueInteractor) {
        this.getAllRestaurantsInteractor = getAllRestaurantsInteractor;
        this.getRestaurant = getRestaurant;
        this.getInterestedColleagueInteractor = getInterestedColleagueInteractor;
        this.updateInterestedColleagueInteractor = updateInterestedColleagueInteractor;
        this.allRestaurantsMutableLiveData.setValue(new ArrayList<>());
        this.interestedColleagueMutableLiveData.setValue(new ArrayList<>());
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
    public List<Restaurant> getGeneratedRestaurants(){
        return getAllRestaurantsInteractor.getGeneratedRestaurants();
    }

    public LiveData<Restaurant> getRestaurant() {
        return restaurantMutableLiveData;
    }

    public void selectRestaurant(User user) {
        restaurantMutableLiveData.setValue(user.getSelectedRestaurant());
    }

    public void selectRestaurant(int position) {
        restaurantMutableLiveData.setValue(getRestaurant.getRestaurant(position));
    }

    public void addInterestedUser(User user) {
        if (!Objects.requireNonNull(interestedColleagueMutableLiveData.getValue()).contains(user))
            updateInterestedColleagueInteractor.updateUserInterestedColleague(user)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onComplete() {
                            Log.i("addInterestedUser", "onComplete:  user added");
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
    }

    //TODO return restaurant, require user.
    public LiveData<List<User>> getInterestedColleague(Restaurant restaurant) {
        Disposable disposable = getInterestedColleagueInteractor.getInterestedColleague(restaurant).subscribe(restaurant1 -> interestedColleagueMutableLiveData.setValue(restaurant1));
        return interestedColleagueMutableLiveData;
    }

}
