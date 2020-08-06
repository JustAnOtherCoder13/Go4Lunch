package com.picone.go4lunch.presentation.viewModels;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

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
    private final SavedStateHandle savedStateHandle;


    @ViewModelInject
    public UserViewModel(GetAllUsers getAllUsers,@Assisted SavedStateHandle savedStateHandle) {
        this.getAllUsers = getAllUsers;
        this.savedStateHandle = savedStateHandle;
        usersMutableLiveData.setValue(getAllUsers.getAllUsers());
    }

    public LiveData<List<User>> getAllUsers(){return usersMutableLiveData;}

    public User getUser(int position){return getUser.getUser(position);}

    public void setSetUserSelectedRestaurant(){setUserSelectedRestaurant.setUserSelectedRestaurant();}

}