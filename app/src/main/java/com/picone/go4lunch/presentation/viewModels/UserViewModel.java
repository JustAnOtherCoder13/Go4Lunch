package com.picone.go4lunch.presentation.viewModels;

import androidx.annotation.NonNull;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.picone.core.domain.entity.user.SettingValues;
import com.picone.core.domain.entity.user.User;
import com.picone.core.domain.interactors.usersInteractors.AddUserInteractor;
import com.picone.core.domain.interactors.usersInteractors.GetAllUsersInteractor;
import com.picone.core.domain.interactors.usersInteractors.UpdateUserInteractor;
import com.picone.core.utils.SchedulerProvider;

import java.util.List;

import static com.picone.core.utils.ConstantParameter.SETTING_START_VALUE;

public class UserViewModel extends BaseViewModel {

    public enum UserCompletionState {
        START_STATE,
        ON_COMPLETE,          // The user has been add successfully
        ON_ERROR              // Write on db failed
    }

    private MutableLiveData<UserCompletionState> UserCompletionStateMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<User>> allUsersMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

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
        if (avatar == null)avatar = "";
        userMutableLiveData.setValue(new User(uid, name, email, avatar, null, SETTING_START_VALUE));
    }

    public void setAllUsersMutableLiveData(List<User> filteredUsers) {
        allUsersMutableLiveData.setValue(filteredUsers);
    }

    public void setAllDbUsers() {
        compositeDisposable.add(
                getAllUsersInteractor.getAllUsers()
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(users -> allUsersMutableLiveData.setValue(users), throwable -> checkException()));
    }

    public void addUser(User currentUser) {
        compositeDisposable.add(
                addUserInteractor.addUser(currentUser)
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(() -> UserCompletionStateMutableLiveData.setValue(UserCompletionState.ON_COMPLETE)
                        , throwable -> {
                            UserCompletionStateMutableLiveData.setValue(UserCompletionState.ON_ERROR);
                            checkException();
                        }));
    }

    public void updateUserSettingValues(@NonNull User currentUser, SettingValues settingValues) {
        currentUser.setSettingValues(settingValues);
        compositeDisposable.add(
                updateUserInteractor.updateUser(currentUser)
                .subscribeOn(schedulerProvider.getIo())
                .observeOn(schedulerProvider.getUi())
                .subscribe(()->{},throwable -> checkException()));
    }
}