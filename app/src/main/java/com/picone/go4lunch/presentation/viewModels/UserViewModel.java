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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserViewModel extends ViewModel {

    public enum UserCompletionState {
        START_STATE,
        ON_COMPLETE,          // The user has been add successfully
        ON_ERROR              // Write on db failed
    }

    private MutableLiveData<UserCompletionState> UserCompletionStateMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> allUsersMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    private AddUserInteractor addUserInteractor;
    private GetAllUsersInteractor getAllUsersInteractor;


    //suppress warning is safe cause subscribe is used to set allUsersMutableLiveData
    @SuppressLint("CheckResult")
    @ViewModelInject
    public UserViewModel(GetAllUsersInteractor getAllUsersInteractor, AddUserInteractor addUserInteractor) {
        this.addUserInteractor = addUserInteractor;
        this.getAllUsersInteractor = getAllUsersInteractor;
    }

    public LiveData<List<User>> getAllUsers = allUsersMutableLiveData;

    public LiveData<User> getCurrentUser = userMutableLiveData;

    public LiveData<UserCompletionState> getAddUserState = UserCompletionStateMutableLiveData;

    public void resetUserCompletionState (){
        UserCompletionStateMutableLiveData.setValue(UserCompletionState.START_STATE);
    }

    public void setCurrentUser(String uid, String name, String email, String avatar) {
        userMutableLiveData.setValue(new User(uid, name, email, avatar, null));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void setAllDbUsers(){
        getAllUsersInteractor.getAllUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> allUsersMutableLiveData.setValue(users));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void addUser(User user) {
        addUserInteractor.addUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> UserCompletionStateMutableLiveData.setValue(UserCompletionState.ON_COMPLETE)
                        , throwable -> UserCompletionStateMutableLiveData.setValue(UserCompletionState.ON_ERROR)
                );
    }
}