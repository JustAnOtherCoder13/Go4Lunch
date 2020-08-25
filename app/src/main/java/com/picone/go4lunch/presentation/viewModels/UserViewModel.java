package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.interactors.userInteractors.AddUserInteractor;
import com.picone.core.domain.interactors.userInteractors.GetAllUsersInteractor;
import com.picone.core.domain.interactors.userInteractors.GetInterestedColleagueInteractor;
import com.picone.core.domain.interactors.userInteractors.GetUserInteractor;
import com.picone.core.domain.interactors.userInteractors.UpdateInterestedColleagueInteractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserViewModel extends ViewModel {

    public enum AddUserState {
        ON_COMPLETE,          // The user has been add successfully
        ON_ERROR              // Write on db failed
    }

    private MutableLiveData<AddUserState> addUserStateMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> usersMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> interestedColleagueMutableLiveData = new MutableLiveData<>();
    //interactors
    private GetAllUsersInteractor getAllUsersInteractor;
    private GetUserInteractor getUserInteractor;
    private AddUserInteractor addUserInteractor;
    private GetInterestedColleagueInteractor getInterestedColleagueInteractor;
    private UpdateInterestedColleagueInteractor updateInterestedColleagueInteractor;

    @ViewModelInject
    public UserViewModel(GetAllUsersInteractor getAllUsersInteractor, GetUserInteractor getUserInteractor,
                         AddUserInteractor addUserInteractor, GetInterestedColleagueInteractor getInterestedColleagueInteractor,
                         UpdateInterestedColleagueInteractor updateInterestedColleagueInteractor) {
        this.getAllUsersInteractor = getAllUsersInteractor;
        this.getUserInteractor = getUserInteractor;
        this.addUserInteractor = addUserInteractor;
        this.getInterestedColleagueInteractor = getInterestedColleagueInteractor;
        this.updateInterestedColleagueInteractor = updateInterestedColleagueInteractor;
        this.interestedColleagueMutableLiveData.setValue(new ArrayList<>());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    //suppress warning is safe cause getAllResultInteractor is used to set usersMutableLiveData value
    public LiveData<List<User>> getAllUsers() {
        getAllUsersInteractor.getAllUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> usersMutableLiveData.setValue(users));
        return usersMutableLiveData;
    }

    public User getUser(int position) {
        return getUserInteractor.getUser(position);
    }

    public void addUser(User user) {
        addUserInteractor.addUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        addUserStateMutableLiveData.setValue(AddUserState.ON_COMPLETE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        addUserStateMutableLiveData.setValue(AddUserState.ON_ERROR);
                    }
                });
    }

    public void setCurrentUser(String uid, String name, String email, String avatar, Restaurant selectedRestaurant) {
        userMutableLiveData.setValue(new User(uid, name, email, avatar, selectedRestaurant));
    }

    public LiveData<User> getCurrentUser() {
        return userMutableLiveData;
    }

    public LiveData<AddUserState> getAddUserState() {
        return addUserStateMutableLiveData;
    }

    public void addInterestedUser(User user) {
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public LiveData<List<User>> getInterestedColleague() {
        getInterestedColleagueInteractor.getInterestedColleague().subscribe(interestedUsers -> interestedColleagueMutableLiveData.setValue(interestedUsers));
        return interestedColleagueMutableLiveData;
    }


}