package com.picone.go4lunch.presentation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

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

public class AuthenticationFragment extends Fragment {

    private static final int RC_SIGN_IN = 13250;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;
    private FragmentAuthenticationBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), getGoogleSignInOptions());
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAuthenticationBinding.inflate(inflater, container, false);
        mBinding.loginWithFacebook.setOnClickListener(v -> signInWithFacebook());
        mBinding.loginWithGoogle.setOnClickListener(v -> signInWithGoogle());
        return mBinding.getRoot();
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
                Toast.makeText(getContext(), "failure", Toast.LENGTH_SHORT).show();
            }
        }
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

    private void googleLogOut() {
        mAuth.signOut();
        mGoogleSignInClient.revokeAccess();
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        goToMaps();
                    } else {
                        Toast.makeText(getContext(), "failure", Toast.LENGTH_SHORT).show();
                    }
                });
    }
//------------------------------------Facebook authentication----------------------------

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(requireContext(), "Authentication failed.",
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
                goToMaps();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }
    //-----------------------------------------------------------------------------------------
    private void goToMaps() {
        NavHostFragment.findNavController(this).navigate(R.id.action_global_mapsFragment);
    }
}