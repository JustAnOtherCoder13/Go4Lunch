package com.picone.go4lunch.presentation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.FragmentAuthenticationBinding;

import java.util.Objects;

public class AuthenticationFragment extends BaseFragment {

    private static final int RC_SIGN_IN = 13250;

    private FragmentAuthenticationBinding mBinding;
    LottieAnimationView mAnimationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAuthenticationBinding.inflate(inflater, container, false);
        initView();
        showAppBars(false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAuthenticationAborted();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(getContext(), R.string.google_auth_failed, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        mAnimationView = mBinding.animationView;
        mAnimationView.setAnimation(R.raw.loading_animation);
        mAnimationView.setVisibility(View.GONE);
        mBinding.loginWithFacebook.setOnClickListener(v -> signInWithFacebook());
        mBinding.loginWithGoogle.setOnClickListener(v -> signInWithGoogle());
    }

    private void initAuthenticationAborted() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        mLoginViewModel.refuseAuthentication();
                    }
                });
    }

    //--------------------Authentication with google----------------------------

    private void signInWithGoogle() {
        playLoadingAnimation(true,mAnimationView);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(requireContext(), getResources().getString(R.string.welcome_message) + Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName(), Toast.LENGTH_LONG).show();
                        mLoginViewModel.authenticate(true);
                        mNavController.navigateUp();
                    } else {
                        Toast.makeText(getContext(), R.string.google_auth_failed, Toast.LENGTH_SHORT).show();
                    }
                });
    }

//------------------------------------Facebook authentication----------------------------

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(requireContext(), getResources().getString(R.string.welcome_message) + Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName(), Toast.LENGTH_LONG).show();
                        mLoginViewModel.authenticate(true);
                        mNavController.navigateUp();
                    } else {
                        Toast.makeText(requireContext(), R.string.facebook_auth_failed,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithFacebook() {
        playLoadingAnimation(true,mAnimationView);
        mBinding.loginWithFacebook.setReadPermissions("email", "public_profile");
        mBinding.loginWithFacebook.setFragment(this);
        mBinding.loginWithFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(requireContext(), R.string.facebook_auth_canceled, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(requireContext(), R.string.facebook_auth_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }
}