package com.picone.go4lunch.presentation.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {


    public enum AuthenticationState {
        UNAUTHENTICATED,        // Initial state, the user needs to authenticate
        AUTHENTICATED,          // The user has authenticated successfully
        INVALID_AUTHENTICATION  // Authentication failed
    }

    public final MutableLiveData<AuthenticationState> authenticationState =
            new MutableLiveData<>();

    public LoginViewModel() {
        authenticationState.setValue(AuthenticationState.UNAUTHENTICATED);
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
