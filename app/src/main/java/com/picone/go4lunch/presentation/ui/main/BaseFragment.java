package com.picone.go4lunch.presentation.ui.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.picone.go4lunch.databinding.DrawerMenuHeaderLayoutBinding;
import com.picone.go4lunch.presentation.viewModels.ChatViewModel;
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
    //View Model
    protected LoginViewModel mLoginViewModel;
    protected UserViewModel mUserViewModel;
    protected RestaurantViewModel mRestaurantViewModel;
    protected ChatViewModel mChatViewModel;

    protected LottieAnimationView mAnimationView;

    private MainActivity mainActivity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModels();
        mainActivity = (MainActivity) getActivity();
    }

    //Current user can't be null cause set when enter app
    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoginViewModel.getAuthenticationState().observe(getViewLifecycleOwner(), authenticationState -> {
            if (authenticationState == LoginViewModel.AuthenticationState.AUTHENTICATED) {
                populateDrawerMenu(mAuth.getCurrentUser());
            }
        });
    }

    protected void setAppBarVisibility(boolean isVisible) {
        mainActivity.setMenuVisibility(isVisible);
    }

    protected void setStatusBarTransparency(boolean isTransparent) {
        mainActivity.setStatusBarTransparency(isTransparent);
    }

    protected void setPageTitle(int title) {
        Objects.requireNonNull(mainActivity.getSupportActionBar()).setTitle(title);
    }

    protected void playLoadingAnimation(boolean bol) {
        mainActivity.playLoadingAnimation(bol);
        mAnimationView = mainActivity.mAnimationView;
    }

    private void initViewModels() {
        mLoginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mRestaurantViewModel = new ViewModelProvider(requireActivity()).get(RestaurantViewModel.class);
        mChatViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
    }

    private void populateDrawerMenu(@NonNull FirebaseUser currentUser) {
        View hView = mainActivity.mBinding.navView.getHeaderView(0);
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