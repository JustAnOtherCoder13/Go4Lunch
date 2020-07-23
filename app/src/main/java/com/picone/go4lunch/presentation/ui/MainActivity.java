package com.picone.go4lunch.presentation.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.ActivityMainBinding;
import com.picone.go4lunch.presentation.viewModels.LoginViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    public FirebaseAuth firebaseAuth;
    public NavController mNavController;
    public GoogleSignInClient mGoogleSignInClient;
    public LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mGoogleSignInClient = GoogleSignIn.getClient(this, getGoogleSignInOptions());
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        initMenuButton();
        setUpNavigation();
        initLoginViewModel();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null) {
            loginViewModel.authenticate(true);
            Toast.makeText(this, getResources().getString(R.string.welcome_back_message) + firebaseAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void initMenuButton() {
        mBinding.topNavBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.top_nav_bar_menu_button:
                    mBinding.drawerLayout.open();
                    break;
                case R.id.top_nav_search_button:

                    break;
            }
            return false;
        });
        mBinding.navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.your_lunch_drawer_layout:
                case R.id.settings_drawer_layout:
                    break;
                case R.id.logout_drawer_layout:
                    mBinding.drawerLayout.close();
                    signOut();
                    break;
            }
            return false;
        });
    }

    private void setUpNavigation() {
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.authenticationFragment, R.id.mapsFragment, R.id.listFragment, R.id.drawer_layout).build();
        NavigationUI.setupWithNavController(mBinding.bottomNavigation, mNavController);
        NavigationUI.setupWithNavController(mBinding.topNavBar, mNavController, appBarConfiguration);
    }

    void setMenuVisibility(Boolean bool) {
        if (bool) {
            mBinding.topNavBar.setVisibility(View.VISIBLE);
            mBinding.bottomNavigation.setVisibility(View.VISIBLE);
            mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            mBinding.topNavBar.setVisibility(View.GONE);
            mBinding.bottomNavigation.setVisibility(View.GONE);
            mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    private GoogleSignInOptions getGoogleSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    public void signOut() {
        firebaseAuth.signOut();
        loginViewModel.authenticate(false);
    }

    private void initLoginViewModel() {
        loginViewModel.authenticationState.observe(this,
                authenticationState -> {
                    switch (authenticationState) {
                        case AUTHENTICATED:
                            mNavController.navigateUp();
                            break;
                        case INVALID_AUTHENTICATION:
                        case UNAUTHENTICATED:
                            mNavController.navigate(R.id.authenticationFragment);
                            break;
                    }
                });
    }
}