package com.picone.go4lunch.presentation.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.picone.go4lunch.presentation.viewModels.LoginViewModel;

abstract class BaseFragment extends Fragment {

    public FirebaseAuth mAuth;
    public GoogleSignInClient mGoogleSignInClient;
    public CallbackManager mCallbackManager;
    public LoginViewModel mLoginViewModel;
    public NavController mNavController;
    private MainActivity mainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mLoginViewModel = mainActivity.loginViewModel;
        mNavController = mainActivity.mNavController;
        initAuthentication();
    }

    public void initAuthentication() {
        mAuth = mainActivity.firebaseAuth;
        mGoogleSignInClient = mainActivity.mGoogleSignInClient;
        mCallbackManager = CallbackManager.Factory.create();
    }

    public void setNavVisibility(boolean bol) {
        mainActivity.setMenuVisibility(bol);
    }

}
