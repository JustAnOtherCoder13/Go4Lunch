package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.User;
import com.picone.core.domain.interactors.usersInteractors.AddUserInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetAllUsersInteractor;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserViewModel extends ViewModel {

    public enum AddUserState {
        ON_COMPLETE,          // The user has been add successfully
        ON_ERROR              // Write on db failed
    }

    private MutableLiveData<AddUserState> _addUserState = new MutableLiveData<>();
    private MutableLiveData<List<User>> _allUsers = new MutableLiveData<>();
    private MutableLiveData<User> _currentUser = new MutableLiveData<>();
    private AddUserInteractor addUserInteractor;


    @SuppressLint("CheckResult")
    @ViewModelInject
    public UserViewModel(GetAllUsersInteractor getAllUsersInteractor,
                         AddUserInteractor addUserInteractor) {
        this.addUserInteractor = addUserInteractor;
        getAllUsersInteractor.getAllUsers().subscribe(users -> _allUsers.setValue(users));
    }

    public LiveData<User> getCurrentUser = _currentUser;

    public LiveData<List<User>> getAllUsers = _allUsers;

    public void setCurrentUser(User currentUser) { _currentUser.setValue(currentUser); }

    public LiveData<AddUserState> getAddUserState() {
        return _addUserState;
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
                        _addUserState.setValue(AddUserState.ON_COMPLETE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        _addUserState.setValue(AddUserState.ON_ERROR);
                    }
                });
    }

}