package com.picone.go4lunch.presentation.ui.main;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.picone.core.domain.entity.User;
import com.picone.go4lunch.presentation.viewModels.LoginViewModel;
import com.picone.go4lunch.presentation.viewModels.UserViewModel;

import java.util.List;

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

    protected List<User> mUsers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUserViewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> {
            mUsers = users;
        });
    }

    private void initVariables() {
        mLoginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
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
}


