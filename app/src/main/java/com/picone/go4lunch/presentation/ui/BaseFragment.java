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
import com.picone.go4lunch.R;
import com.picone.go4lunch.presentation.viewModels.LoginViewModel;
import com.picone.go4lunch.presentation.viewModels.RestaurantViewModel;
import com.picone.go4lunch.presentation.viewModels.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

abstract class BaseFragment extends Fragment {

    List<User> mUsers = new ArrayList<>();
    List<Restaurant> mRestaurants = new ArrayList<>();

    ListRecyclerViewAdapter mAdapter;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager mCallbackManager;
    NavController mNavController;
    LottieAnimationView mAnimationView;

    //View Model
    LoginViewModel mLoginViewModel;
    RestaurantViewModel mRestaurantViewModel;
    UserViewModel mUserViewModel;
    private MainActivity mainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        initVariables();
    }

    void initVariables() {
        assert mainActivity != null;
        mRestaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
        mLoginViewModel = new ViewModelProvider(mainActivity).get(LoginViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mNavController = mainActivity.mNavController;
        mAuth = mainActivity.mFirebaseAuth;
        mGoogleSignInClient = mainActivity.mGoogleSignInClient;
        mCallbackManager = CallbackManager.Factory.create();
    }

    void showAppBars(boolean isVisible) {
        mainActivity.setMenuVisibility(isVisible);
    }

    void playLoadingAnimation(boolean isAnimationMustPlay, LottieAnimationView mAnimationView) {
        this.mAnimationView = mAnimationView;
        if (isAnimationMustPlay) {
            mAnimationView.setVisibility(View.VISIBLE);
            mAnimationView.playAnimation();
        } else {
            if (mAnimationView!=null) {
                mAnimationView.setVisibility(View.GONE);
                mAnimationView.pauseAnimation();
            }
        }
    }

    void initViewModels() {
        switch (Objects.requireNonNull(mNavController.getCurrentDestination()).getId()){
            case R.id.listFragment :
                mRestaurantViewModel.restaurantsMutableLiveData.observe(this, restaurants -> {
                    mRestaurants = restaurants;
                });
                break;
            case R.id.restaurantDetailFragment :
                mRestaurantViewModel.restaurantsMutableLiveData.observe(this, restaurants -> mRestaurants = restaurants);
                mUserViewModel.usersMutableLiveData.observe(this, users ->{
                    mUsers = users;
                } );
        }
    }
}


