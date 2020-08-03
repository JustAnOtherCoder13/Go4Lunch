package com.picone.go4lunch.presentation.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.data.mocks.Generator;
import com.picone.core.domain.entity.User;
import com.picone.core.domain.interactors.GetAllUsers;
import com.picone.core.domain.interactors.GetUser;
import com.picone.core.domain.interactors.SetUserSelectedRestaurant;

import java.util.List;

public class UserViewModel extends ViewModel {

    public MutableLiveData<List<User>> usersMutableLiveData = new MutableLiveData<>();
    private GetAllUsers getAllUsers;
    private GetUser getUser;
    private SetUserSelectedRestaurant setUserSelectedRestaurant;

    public UserViewModel(GetAllUsers getAllUsers, GetUser getUser
    ,SetUserSelectedRestaurant setUserSelectedRestaurant) {
        this.getAllUsers = getAllUsers;
        this.getUser = getUser;
        this.setUserSelectedRestaurant = setUserSelectedRestaurant;
        usersMutableLiveData.setValue(getAllUsers.getAllUsers());
    }

    public LiveData<List<User>> getAllUsers(){return (LiveData<List<User>>)usersMutableLiveData;}

    public User getUser(int position){return getUser.getUser(position);}

    public void setSetUserSelectedRestaurant(){setUserSelectedRestaurant.setUserSelectedRestaurant();}

}