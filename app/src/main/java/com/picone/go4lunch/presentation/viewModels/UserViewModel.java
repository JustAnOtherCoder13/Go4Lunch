package com.picone.go4lunch.presentation.viewModels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.domain.entity.User;
import com.picone.core.domain.interactors.AddUser;
import com.picone.core.domain.interactors.GetAllUsers;
import com.picone.core.domain.interactors.GetUser;

import java.util.List;

public class UserViewModel extends ViewModel {

    private MutableLiveData<List<User>> usersMutableLiveData = new MutableLiveData<>();
    private GetAllUsers getAllUsers;
    private GetUser getUser;
    private AddUser addUser;


    @ViewModelInject
    public UserViewModel(GetAllUsers getAllUsers, GetUser getUser,
                         AddUser addUser) {
        this.getAllUsers = getAllUsers;
        this.getUser = getUser;
        this.addUser = addUser;
        usersMutableLiveData.setValue(getAllUsers.getAllUsers());
    }


    public LiveData<List<User>> getAllUsers(){return usersMutableLiveData;}

    public User getUser(int position){return getUser.getUser(position);}

    public void addUser(User user){addUser.addUser(user);}
}