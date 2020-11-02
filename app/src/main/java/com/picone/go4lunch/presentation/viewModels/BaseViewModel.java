package com.picone.go4lunch.presentation.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.picone.go4lunch.presentation.utils.ErrorHandler;

import java.io.IOException;

public class BaseViewModel extends ViewModel {

    protected MutableLiveData<ErrorHandler.ERROR_STATE> errorState = new MutableLiveData<>(ErrorHandler.ERROR_STATE.NO_ERROR);
    public LiveData<ErrorHandler.ERROR_STATE> getErrorState = errorState;


    protected void checkException(Throwable throwable) {
        if (throwable instanceof IOException)
            errorState.setValue(ErrorHandler.ERROR_STATE.NO_CONNEXION_ERROR);
    }

}
