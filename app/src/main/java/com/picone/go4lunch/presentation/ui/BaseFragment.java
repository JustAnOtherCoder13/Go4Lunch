package com.picone.go4lunch.presentation.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.picone.go4lunch.R;
import com.picone.go4lunch.presentation.viewModels.LoginViewModel;

abstract class BaseFragment extends Fragment {

    public FirebaseAuth mAuth;
    public GoogleSignInClient mGoogleSignInClient;
    public CallbackManager mCallbackManager;
    public LoginViewModel mLoginViewModel;
    public NavController mNavController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginViewModel = MainActivity.LOGIN_VIEW_MODEL;
        mNavController = NavHostFragment.findNavController(this);
        initAuthentication();
    }

    public void initAuthentication() {
        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), getGoogleSignInOptions());
        mCallbackManager = CallbackManager.Factory.create();
    }

    private GoogleSignInOptions getGoogleSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    public void setNavVisibility(boolean bol) {
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.setBottomNavAndToolbarVisibility(bol);
    }

}
