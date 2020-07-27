package com.picone.go4lunch.presentation.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.core.data.mocks.Generator;
import com.picone.core.domain.entity.User;

import java.util.List;

public class UserViewModel extends ViewModel {

    public MutableLiveData<List<User>> usersMutableLiveData = new MutableLiveData<>();

    public UserViewModel() {
        usersMutableLiveData.setValue(Generator.generateUsers());
    }
}