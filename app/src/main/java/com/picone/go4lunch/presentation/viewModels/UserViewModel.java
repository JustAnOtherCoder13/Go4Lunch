package com.picone.go4lunch.presentation.viewModels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.User;
import com.picone.core.domain.interactors.AddUserInteractor;
import com.picone.core.domain.interactors.GetAllUsersInteractor;
import com.picone.core.domain.interactors.GetUserInteractor;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class UserViewModel extends ViewModel {

    private MutableLiveData<List<User>> usersMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private GetAllUsersInteractor getAllUsersInteractor;
    private GetUserInteractor getUserInteractor;
    private AddUserInteractor addUserInteractor;


    @ViewModelInject
    public UserViewModel(GetAllUsersInteractor getAllUsersInteractor, GetUserInteractor getUserInteractor,
                         AddUserInteractor addUserInteractor) {
        this.getAllUsersInteractor = getAllUsersInteractor;
        this.getUserInteractor = getUserInteractor;
        this.addUserInteractor = addUserInteractor;
        Disposable disposable = getAllUsersInteractor.getAllUsers().subscribe(users ->usersMutableLiveData.setValue(users));
    }


    public LiveData<List<User>> getAllUsers(){return usersMutableLiveData;}

    public User getUser(int position){return getUserInteractor.getUser(position);}

    public void addUser(User user){
        addUserInteractor.addUser(user);}

    public void setCurrentUser (String uid, String name, String email, String avatar){
        userMutableLiveData.setValue(new User(uid,name,email,avatar));
    }

    public LiveData<User> getCurrentUser(){
        return userMutableLiveData;
    }
}