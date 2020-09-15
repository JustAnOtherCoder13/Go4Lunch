package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.User;
import com.picone.core.domain.interactors.usersInteractors.AddUserInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetAllUsersInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetUserInteractor;

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

    private MutableLiveData<AddUserState> addUserStateMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> allUsersMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    private AddUserInteractor addUserInteractor;


    //suppress warning is safe cause subscribe is used to set allUsersMutableLiveData
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    @ViewModelInject
    public UserViewModel(GetAllUsersInteractor getAllUsersInteractor, GetUserInteractor getUserInteractor,
                         AddUserInteractor addUserInteractor) {
        this.addUserInteractor = addUserInteractor;
        getAllUsersInteractor.getAllUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> allUsersMutableLiveData.setValue(users));
    }


    public LiveData<List<User>> getAllUsers = allUsersMutableLiveData;

    public LiveData<User> getCurrentUser = userMutableLiveData;

    public LiveData<AddUserState> getAddUserState = addUserStateMutableLiveData;

    public void setCurrentUser(String uid, String name, String email, String avatar) {
        userMutableLiveData.setValue(new User(uid, name, email, avatar, null));
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

}