package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.interactors.usersInteractors.GetCurrentUserForEmailInteractor;
import com.picone.core.domain.interactors.usersInteractors.UpdateUserChosenRestaurantInteractor;

import io.reactivex.CompletableObserver;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class InterestedUserViewModel extends ViewModel {

    private MutableLiveData<User> currentUserMutableLiveData = new MutableLiveData<>();

    private GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor;
    private UpdateUserChosenRestaurantInteractor updateUserChosenRestaurantInteractor;

    @ViewModelInject
    public InterestedUserViewModel(GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor
            , UpdateUserChosenRestaurantInteractor updateUserChosenRestaurantInteractor) {
        this.getCurrentUserForEmailInteractor = getCurrentUserForEmailInteractor;
        this.updateUserChosenRestaurantInteractor = updateUserChosenRestaurantInteractor;
    }

    public LiveData<User> currentUser = currentUserMutableLiveData;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void setCurrentUserForEmail(String currentUserEmail){
        getCurrentUserForEmailInteractor.getCurrentUserForEmail(currentUserEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {
                    if (!users.isEmpty())
                    currentUserMutableLiveData.setValue(users.get(0));
                });
    }
    @SuppressWarnings("ConstantConditions")
    //currentUserMutableLiveData.getValue().getEmail() can't be null cause already set
    public void updateCurrentUserChosenRestaurant(Restaurant restaurant){
        updateUserChosenRestaurantInteractor.updateUserChosenRestaurant(currentUserMutableLiveData.getValue(), restaurant)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("TAG", "onComplete: user chosen restaurant update");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

}
