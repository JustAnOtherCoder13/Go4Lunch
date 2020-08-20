package com.picone.go4lunch.presentation.ui.main;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.picone.core.domain.entity.User;
import com.picone.go4lunch.databinding.ActivityMainBinding;
import com.picone.go4lunch.databinding.DrawerMenuHeaderLayoutBinding;
import com.picone.go4lunch.presentation.viewModels.LoginViewModel;
import com.picone.go4lunch.presentation.viewModels.RestaurantViewModel;
import com.picone.go4lunch.presentation.viewModels.UserViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public abstract class BaseFragment extends Fragment {

    //Authentication injection
    @Inject
    protected FirebaseAuth mAuth;
    @Inject
    protected GoogleSignInClient mGoogleSignInClient;
    @Inject
    protected CallbackManager mCallbackManager;
    protected LottieAnimationView mAnimationView;

    //View Model
    protected LoginViewModel mLoginViewModel;
    protected UserViewModel mUserViewModel;
    protected RestaurantViewModel mRestaurantViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoginViewModel.getAuthenticationState().observe(getViewLifecycleOwner(), authenticationState -> {
            if (authenticationState == LoginViewModel.AuthenticationState.AUTHENTICATED) {
                populateDrawerMenu(mAuth.getCurrentUser());
            }
        });
    }

    private void initVariables() {
        mLoginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mRestaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
    }

    protected void showAppBars(boolean isVisible) {
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.setMenuVisibility(isVisible);
    }

    protected void playLoadingAnimation(boolean bol, LottieAnimationView mAnimationView) {
        this.mAnimationView = mAnimationView;
        if (bol) {
            mAnimationView.setVisibility(View.VISIBLE);
            mAnimationView.playAnimation();
        } else {
            if (mAnimationView != null) {
                mAnimationView.setVisibility(View.GONE);
                mAnimationView.pauseAnimation();
            }
        }
    }

    protected void populateDrawerMenu(FirebaseUser currentUser) {

        MainActivity mainActivity = (MainActivity) getActivity();
        ActivityMainBinding mBinding = mainActivity.mBinding;
        View hView = mBinding.navView.getHeaderView(0);
        DrawerMenuHeaderLayoutBinding headerLayoutBinding = DrawerMenuHeaderLayoutBinding.bind(hView);
        headerLayoutBinding.drawerUserName.setText(currentUser.getDisplayName());
        headerLayoutBinding.drawerUserMail.setText(currentUser.getEmail());
        Glide.with(this)
                .load(currentUser.getPhotoUrl())
                .circleCrop()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        headerLayoutBinding.drawerAvatar.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}


