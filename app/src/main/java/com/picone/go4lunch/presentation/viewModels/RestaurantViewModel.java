package com.picone.go4lunch.presentation.viewModels;

import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.RestaurantDailySchedule;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.interactors.restaurantsInteractors.AddRestaurantInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetAllRestaurantsInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantForNameInteractor;
import com.picone.core.domain.interactors.restaurantsInteractors.GetRestaurantInteractor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RestaurantViewModel extends ViewModel {

    private final static Calendar CALENDAR = Calendar.getInstance();
    private final static int MY_DAY_OF_MONTH = CALENDAR.get(Calendar.DAY_OF_MONTH);
    private final static int MY_MONTH = CALENDAR.get(Calendar.MONTH);
    private final static int MY_YEAR = CALENDAR.get(Calendar.YEAR);

    private Date today;




    private MutableLiveData<User> _currentUser = new MutableLiveData<>();
    private MutableLiveData<Restaurant> selectedRestaurantMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Restaurant> persistedRestaurantMutableLiveData = new MutableLiveData<>();

    private GetAllRestaurantsInteractor getAllRestaurantsInteractor;
    private GetRestaurantInteractor getRestaurant;
    private GetRestaurantForNameInteractor getRestaurantForNameInteractor;
    private AddRestaurantInteractor addRestaurantInteractor;

    @ViewModelInject
    public RestaurantViewModel(GetAllRestaurantsInteractor getAllRestaurantsInteractor
            , GetRestaurantInteractor getRestaurant, GetRestaurantForNameInteractor getRestaurantForNameInteractor
    ,AddRestaurantInteractor addRestaurantInteractor) {
        this.getAllRestaurantsInteractor = getAllRestaurantsInteractor;
        this.getRestaurant = getRestaurant;
        this.getRestaurantForNameInteractor = getRestaurantForNameInteractor;
        this.addRestaurantInteractor = addRestaurantInteractor;
    }

    public List<Restaurant> getAllRestaurants() {
        return getAllRestaurantsInteractor.getGeneratedRestaurants();
    }

    public LiveData<Restaurant> getSelectedRestaurant = selectedRestaurantMutableLiveData;

    public void getRestaurantForName(String restaurantName){
        getRestaurantForNameInteractor.getRestaurantForName(restaurantName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Restaurant>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Restaurant> restaurants) {
                        if (restaurants.isEmpty()) {Log.i("TAG", "onNext: restaurant not exist"+restaurants);}
                        else{
                            persistedRestaurantMutableLiveData.setValue(restaurants.get(0));
                            Log.i("TAG", "onNext: restaurant exist"+restaurants.get(0));}

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void addRestaurant(Restaurant restaurant){


        getRestaurantForNameInteractor.getRestaurantForName(restaurant.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Restaurant>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Restaurant> restaurants) {
                        if (restaurants.isEmpty()) {
                            try {
                                today = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(MY_DAY_OF_MONTH + "/" + MY_MONTH + "/" + MY_YEAR);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(today);
                            RestaurantDailySchedule dailySchedule = new RestaurantDailySchedule(date,new ArrayList<>());
                            restaurant.setRestaurantDailySchedule(dailySchedule);
                            Log.i("TAG", "onNext: restaurant not exist"+restaurants+" "+dailySchedule);
                            addRestaurantInteractor.addRestaurant(restaurant)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new CompletableObserver() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onComplete() {
                                            Log.i("TAG", "onComplete: restaurant added");
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }
                                    });
                            }
                        else{
                            persistedRestaurantMutableLiveData.setValue(restaurants.get(0));
                            Log.i("TAG", "onNext: restaurant exist"+restaurants.get(0));}

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        /**/
    }



    public void setSelectedRestaurant (int position){
        selectedRestaurantMutableLiveData.setValue(getRestaurant.getRestaurant(position));
    }

}
