package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.interactors.userInteractors.AddUserInteractor;
import com.picone.core.domain.interactors.userInteractors.GetAllUsersInteractor;
import com.picone.core.domain.interactors.userInteractors.GetCurrentUserForEmailInteractor;

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
    public LiveData<AddUserState> getAddUserState = addUserStateMutableLiveData;

    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    public LiveData<User> getCurrentUser = userMutableLiveData;

    private MutableLiveData<List<User>> usersMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> _getCurrentUserForEmailMutableLiveData = new MutableLiveData<>();
    //interactors
    private GetAllUsersInteractor getAllUsersInteractor;
    private AddUserInteractor addUserInteractor;
    private GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor;

    private final SavedStateHandle savedStateHandle;


    @ViewModelInject
    public UserViewModel(GetAllUsersInteractor getAllUsersInteractor,
                         AddUserInteractor addUserInteractor,
                         GetCurrentUserForEmailInteractor getCurrentUserForEmailInteractor,
                         @Assisted SavedStateHandle savedStateHandle) {
        this.getAllUsersInteractor = getAllUsersInteractor;
        this.addUserInteractor = addUserInteractor;
        this.getCurrentUserForEmailInteractor = getCurrentUserForEmailInteractor;
        this.savedStateHandle = savedStateHandle;
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

    @SuppressLint("CheckResult")
    public LiveData<List<User>> getCurrentUserForEmail(String authCurrentUserEmail){
        getCurrentUserForEmailInteractor.getCurrentUserForEmail(authCurrentUserEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> _getCurrentUserForEmailMutableLiveData.setValue(users));
        return _getCurrentUserForEmailMutableLiveData;
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
}