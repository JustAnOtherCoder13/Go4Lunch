package com.picone.go4lunch.presentation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.FragmentAuthenticationBinding;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;
import com.picone.go4lunch.presentation.viewModels.UserViewModel;

import java.util.Arrays;
import java.util.Objects;

import static com.picone.core.utils.ConstantParameter.RC_SIGN_IN;

public class AuthenticationFragment extends BaseFragment {

    private FragmentAuthenticationBinding mBinding;
    private NavController mNavController;
    private boolean isNewUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAuthenticationBinding.inflate(inflater, container, false);
        initButtons();
        setAppBarVisibility(false);
        setStatusBarTransparency(true);
        setSettingViewVisibility();
        playLoadingAnimation(false);
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAuthenticationAborted();
        mUserViewModel.getCurrentUser.observe(getViewLifecycleOwner(), currentUser -> {
            if (isNewUser) mUserViewModel.addUser(currentUser);
            mRestaurantViewModel.setCurrentUser(currentUser.getEmail());
        });
        mUserViewModel.getAddUserState.observe(getViewLifecycleOwner(), userCompletionState -> {
            if (userCompletionState == UserViewModel.UserCompletionState.ON_COMPLETE)
                mNavController.navigateUp();
        });
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
                playLoadingAnimation(false);
                Toast.makeText(getContext(), R.string.google_auth_failed, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initButtons() {
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

    //----------------------------------Google authentication----------------------------

    private void signInWithGoogle() {
        playLoadingAnimation(true);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        mLoginViewModel.authenticate(true);
                        setCurrentUser(task);
                        Toast.makeText(requireContext(), getResources().getString(R.string.welcome_message) + Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName(), Toast.LENGTH_LONG).show();
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
                        mLoginViewModel.authenticate(true);
                        setCurrentUser(task);
                        Toast.makeText(requireContext(), getResources().getString(R.string.welcome_message) + Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(requireContext(), R.string.facebook_auth_failed,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithFacebook() {
        playLoadingAnimation(true);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(getString(R.string.email), getString(R.string.public_profile)));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                playLoadingAnimation(false);
                Toast.makeText(requireContext(), R.string.facebook_auth_canceled, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(requireContext(), R.string.facebook_auth_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setCurrentUser(Task<AuthResult> task) {
        if (task.isSuccessful()) {
            isNewUser = Objects.requireNonNull(Objects.requireNonNull(task.getResult())
                    .getAdditionalUserInfo())
                    .isNewUser();
        }
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = "";
        String name = "";
        String email = "";
        String photoUrl = "";
        if (currentUser != null) {
            for (UserInfo profile : currentUser.getProviderData()) {
                uid = profile.getUid();
                name = profile.getDisplayName();
                email = profile.getEmail();
                photoUrl = Objects.requireNonNull(profile.getPhotoUrl()).toString();
            }
            mUserViewModel.createCurrentUser(uid, name, email, photoUrl);
        }
    }
}