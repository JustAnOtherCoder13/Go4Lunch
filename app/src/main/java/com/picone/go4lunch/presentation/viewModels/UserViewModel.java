package com.picone.go4lunch.presentation.viewModels;

import android.annotation.SuppressLint;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.picone.core.domain.entity.user.SettingValues;
import com.picone.core.domain.entity.user.User;
import com.picone.core.domain.interactors.usersInteractors.AddUserInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetAllUsersInteractor;
import com.picone.core.domain.interactors.usersInteractors.UpdateUserInteractor;
import com.picone.go4lunch.presentation.utils.SchedulerProvider;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.picone.go4lunch.presentation.utils.ConstantParameter.SETTING_START_VALUE;

public class UserViewModel extends BaseViewModel {

    public enum UserCompletionState {
        START_STATE,
        ON_COMPLETE,          // The user has been add successfully
        ON_ERROR              // Write on db failed
    }

    private MutableLiveData<UserCompletionState> UserCompletionStateMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> allUsersMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    public AddUserInteractor addUserInteractor;
    public GetAllUsersInteractor getAllUsersInteractor;
    public UpdateUserInteractor updateUserInteractor;
    private SchedulerProvider schedulerProvider;


    //suppress warning is safe cause subscribe is used to set allUsersMutableLiveData
    @SuppressLint("CheckResult")
    @ViewModelInject
    public UserViewModel(GetAllUsersInteractor getAllUsersInteractor, AddUserInteractor addUserInteractor, UpdateUserInteractor updateUserInteractor,SchedulerProvider schedulerProvider) {
        this.addUserInteractor = addUserInteractor;
        this.getAllUsersInteractor = getAllUsersInteractor;
        this.updateUserInteractor = updateUserInteractor;
        this.schedulerProvider = schedulerProvider;
    }

    public LiveData<List<User>> getAllUsers() {return allUsersMutableLiveData;}
    public LiveData<User> getCurrentUser = userMutableLiveData;
    public LiveData<UserCompletionState> getAddUserState = UserCompletionStateMutableLiveData;

    public void resetUserCompletionState() {
        UserCompletionStateMutableLiveData.setValue(UserCompletionState.START_STATE);
    }

    public void createCurrentUser(String uid, String name, String email, String avatar) {
        userMutableLiveData.setValue(new User(uid, name, email, avatar, null, SETTING_START_VALUE));
    }

    public void setAllUsersMutableLiveData(List<User> filteredUsers) {
        allUsersMutableLiveData.setValue(filteredUsers);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void setAllDbUsers() {
        getAllUsersInteractor.getAllUsers()
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(users -> allUsersMutableLiveData.setValue(users), throwable -> checkException());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void addUser(User currentUser) {
        addUserInteractor.addUser(currentUser)
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(() -> UserCompletionStateMutableLiveData.setValue(UserCompletionState.ON_COMPLETE)
                        , throwable -> {
                            UserCompletionStateMutableLiveData.setValue(UserCompletionState.ON_ERROR);
                            checkException();
                        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    public void updateUserSettingValues(User currentUser, SettingValues settingValues) {
        currentUser.setSettingValues(settingValues);
        updateUserInteractor.updateUser(currentUser)
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(()->{},throwable -> checkException());
    }
}