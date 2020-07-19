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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.FragmentAuthenticationBinding;
import com.picone.go4lunch.presentation.viewModels.LoginViewModel;

import java.util.Objects;

public class AuthenticationFragment extends Fragment {

    private static final int RC_SIGN_IN = 13250;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;
    private FragmentAuthenticationBinding mBinding;
    private LoginViewModel loginViewModel;
    private NavController navController;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        initAuthentication();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAuthenticationBinding.inflate(inflater, container, false);
        initView();
        setNavVisibility();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLoginViewModel();
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
        mBinding.loginWithFacebook.setLoginText(getResources().getString(R.string.sign_in_with_facebook_authentication));
        mBinding.loginWithFacebook.setOnClickListener(v -> signInWithFacebook());
        mBinding.loginWithGoogle.setOnClickListener(v -> signInWithGoogle());
    }

    private void initAuthentication() {
        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), getGoogleSignInOptions());
        mCallbackManager = CallbackManager.Factory.create();
    }

    private void initLoginViewModel() {
        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        loginViewModel.authenticationState.observe(getViewLifecycleOwner(),
                authenticationState -> {
                    switch (authenticationState) {
                        case AUTHENTICATED:
                            navController.navigateUp();
                            break;
                        case INVALID_AUTHENTICATION:
                        case UNAUTHENTICATED:
                            Toast.makeText(requireContext(), getResources().getString(R.string.invalid_authentication), Toast.LENGTH_LONG).show();
                            break;
                    }
                });
        if (mAuth.getCurrentUser() != null) {
            loginViewModel.authenticate(true);
            Toast.makeText(requireContext(), getResources().getString(R.string.welcome_back_message) + mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_LONG).show();
        }
    }

    private void setNavVisibility() {
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.setBottomNavAndToolbarVisibility(false);
    }

    private void initAuthenticationAborted() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        loginViewModel.refuseAuthentication();
                    }
                });
    }

    //--------------------Authentication with google----------------------------

    private GoogleSignInOptions getGoogleSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        assert firebaseUser != null;
                        loginViewModel.authenticate(true);
                        Toast.makeText(requireContext(), getResources().getString(R.string.welcome_message) + firebaseUser.getDisplayName(), Toast.LENGTH_LONG).show();
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
                        loginViewModel.authenticate(true);
                    } else {
                        Toast.makeText(requireContext(), R.string.facebook_auth_failed,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithFacebook() {
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