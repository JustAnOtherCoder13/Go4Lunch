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
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.go4lunch.presentation.viewModels.LoginViewModel;
import com.picone.go4lunch.presentation.viewModels.RestaurantViewModel;
import com.picone.go4lunch.presentation.viewModels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
abstract class BaseFragment extends Fragment {

    List<User> mUsers = new ArrayList<>();
    List<Restaurant> mRestaurants = new ArrayList<>();

    ListRecyclerViewAdapter mAdapter;
    @Inject
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager mCallbackManager;
    NavController mNavController;
    LottieAnimationView mAnimationView;

    //View Model
    LoginViewModel mLoginViewModel;

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
        mNavController = mainActivity.mNavController;
        mGoogleSignInClient = mainActivity.mGoogleSignInClient;
        mCallbackManager = CallbackManager.Factory.create();
    }

    void showAppBars(boolean isVisible) {
        mainActivity.setMenuVisibility(isVisible);
    }

    void playLoadingAnimation(boolean bol, LottieAnimationView mAnimationView) {
        this.mAnimationView = mAnimationView;
        if (bol) {
            mAnimationView.setVisibility(View.VISIBLE);
            mAnimationView.playAnimation();
        } else {
            if (mAnimationView!=null) {
                mAnimationView.setVisibility(View.GONE);
                mAnimationView.pauseAnimation();
            }
        }
    }
}


