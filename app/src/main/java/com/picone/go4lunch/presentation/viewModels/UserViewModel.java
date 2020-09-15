package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.User;
import com.picone.core.domain.interactors.usersInteractors.AddUserInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetAllUsersInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetCurrentUserForEmailInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetUserInteractor;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.Scheduler;
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
    private GetAllUsersInteractor getAllUsersInteractor;
    private GetUserInteractor getUserInteractor;
    private AddUserInteractor addUserInteractor;
    private GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor;


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    @ViewModelInject
    public UserViewModel(GetAllUsersInteractor getAllUsersInteractor, GetUserInteractor getUserInteractor,
                         AddUserInteractor addUserInteractor, GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor) {
        this.getAllUsersInteractor = getAllUsersInteractor;
        this.getUserInteractor = getUserInteractor;
        this.addUserInteractor = addUserInteractor;
        this.getCurrentUserForEmailInteractor = getCurrentUserForEmailInteractor;
        getAllUsersInteractor.getAllUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> usersMutableLiveData.setValue(users));
    }


    public LiveData<List<User>> getAllUsers = usersMutableLiveData;

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

    public void setCurrentUser(String uid, String name, String email, String avatar) {
        userMutableLiveData.setValue(new User(uid, name, email, avatar,null));
    }

    public LiveData<User> getCurrentUserForEmail(String authUserEmail){
        getCurrentUserForEmailInteractor.getCurrentUserForEmail(authUserEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {userMutableLiveData.setValue(users.get(0));});
        return userMutableLiveData;
    }

    public LiveData<User> getCurrentUser() {
        return userMutableLiveData;
    }

    public LiveData<AddUserState> getAddUserState() {
        return addUserStateMutableLiveData;
    }

}