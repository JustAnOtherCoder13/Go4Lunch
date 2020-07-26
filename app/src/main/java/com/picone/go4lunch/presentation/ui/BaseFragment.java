package com.picone.go4lunch.presentation.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.picone.go4lunch.presentation.viewModels.LoginViewModel;

abstract class BaseFragment extends Fragment {

    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager mCallbackManager;
    LoginViewModel mLoginViewModel;
    NavController mNavController;
    LottieAnimationView mAnimationView;
    private MainActivity mainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        initVariables();
    }

    void initVariables() {
        assert mainActivity != null;
        mLoginViewModel = new ViewModelProvider(mainActivity).get(LoginViewModel.class);
        mAnimationView = mainActivity.mAnimationView;
        mNavController = mainActivity.mNavController;
        mAuth = mainActivity.mFirebaseAuth;
        mGoogleSignInClient = mainActivity.mGoogleSignInClient;
        mCallbackManager = CallbackManager.Factory.create();
    }

    void setNavVisibility(boolean bol) {
        mainActivity.setMenuVisibility(bol);
    }

    void playLoadingAnimation(boolean bol) {
        if (bol) {
            mAnimationView.setVisibility(View.VISIBLE);
            mAnimationView.playAnimation();
        } else {
            mAnimationView.setVisibility(View.GONE);
            mAnimationView.pauseAnimation();
        }
    }
}


