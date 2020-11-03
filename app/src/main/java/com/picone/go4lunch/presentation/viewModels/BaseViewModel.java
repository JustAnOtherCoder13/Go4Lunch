package com.picone.go4lunch.presentation.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.go4lunch.presentation.utils.ErrorHandler;

import java.io.IOException;

public class BaseViewModel extends ViewModel {

    protected MutableLiveData<ErrorHandler> errorState = new MutableLiveData<>(ErrorHandler.NO_ERROR);
    public LiveData<ErrorHandler> getErrorState = errorState;


    protected void checkException() {
        errorState.setValue(ErrorHandler.ON_ERROR);
    }
}
