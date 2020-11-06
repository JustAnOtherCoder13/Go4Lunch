package com.picone.go4lunch.presentation.viewModels;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    public enum AuthenticationState {
        UNAUTHENTICATED,
        AUTHENTICATED,
        INVALID_AUTHENTICATION
    }

    private final MutableLiveData<AuthenticationState> authenticationState = new MutableLiveData<>();

    @ViewModelInject
    public LoginViewModel() {
        authenticationState.setValue(AuthenticationState.UNAUTHENTICATED);
    }

    public LiveData<AuthenticationState> getAuthenticationState() {
        return authenticationState;
    }

    public void authenticate(boolean bool) {
        if (bool) {
            authenticationState.setValue(AuthenticationState.AUTHENTICATED);
        } else {
            authenticationState.setValue(AuthenticationState.INVALID_AUTHENTICATION);
        }
    }

    public void refuseAuthentication() {
        authenticationState.setValue(AuthenticationState.UNAUTHENTICATED);
    }
}
