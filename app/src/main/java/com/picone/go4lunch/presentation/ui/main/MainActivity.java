package com.picone.go4lunch.presentation.ui.main;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.ActivityMainBinding;
import com.picone.go4lunch.presentation.viewModels.LoginViewModel;
import com.picone.go4lunch.presentation.viewModels.RestaurantViewModel;
import com.picone.go4lunch.presentation.viewModels.UserViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.scopes.ActivityScoped;

@ActivityScoped
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    //TODO add push message on 12h with chosen restaurant name, address, and list of interested users
    //TODO create a chat


    public ActivityMainBinding mBinding;

    @Inject
    protected GoogleSignInClient mGoogleSignInClient;
    @Inject
    protected FirebaseAuth mFirebaseAuth;

    private UserViewModel mUserViewModel;
    private LoginViewModel mLoginViewModel;
    private RestaurantViewModel mRestaurantViewModel;
    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mRestaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
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
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (mFirebaseAuth.getCurrentUser() != null || accessToken != null && !accessToken.isExpired()) {
            mLoginViewModel.authenticate(true);
            mRestaurantViewModel.setCurrentUser(mFirebaseAuth.getCurrentUser().getEmail());
            Toast.makeText(this, getResources().getString(R.string.welcome_back_message) + mFirebaseAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Objects.requireNonNull(mNavController.getCurrentDestination()).getId() == R.id.authenticationFragment) {
            this.finish();
        }
    }

    private void initMenuButton() {
        mBinding.topNavBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.top_nav_bar_menu_button:
                    mBinding.drawerLayout.open();
                    break;
                case R.id.top_nav_search_button:
                    initSearchView(item);
                    break;
            }
            return false;
        });
        mBinding.navView.setNavigationItemSelectedListener(item -> {
            mBinding.drawerLayout.close();
            switch (item.getItemId()) {
                case R.id.your_lunch_drawer_layout:
                    mRestaurantViewModel.getCurrentUser.observe(this,user -> {
                        if (user.getUserDailySchedule()!=null){
                            mRestaurantViewModel.initSelectedRestaurant(user.getUserDailySchedule().getRestaurantName());
                            mNavController.navigate(R.id.restaurantDetailFragment);
                        }
                        else Toast.makeText(this,"You haven't choose a restaurant yet",Toast.LENGTH_SHORT).show();
                    });
                    //TODO add setting view to change language, access notification, avoid reservation
                case R.id.settings_drawer_layout:
                    break;
                case R.id.logout_drawer_layout:
                    signOut();
                    break;
            }
            return false;
        });
    }

    private void initSearchView(MenuItem item) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = item.getActionView().findViewById(R.id.top_nav_search_button);
        assert searchManager != null;
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search restaurants");
        searchView.setBackgroundColor(Color.WHITE);
        searchView.setOnQueryTextListener(getOnQueryTextListener());
        item.setOnActionExpandListener(geOnActionExpandListener(searchView));
        View deleteButton = searchView.findViewById(R.id.search_close_btn);
        deleteButton.setOnClickListener(getOnClickListener(searchView));
    }

    @NonNull
    private View.OnClickListener getOnClickListener(SearchView searchView) {
        return v -> {
            EditText editText = findViewById(R.id.search_src_text);
            editText.setText("");
            searchView.setQuery("", false);
            mRestaurantViewModel.resetFilteredRestaurant();
            mRestaurantViewModel.getRestaurantFromMaps();
        };
    }

    @NonNull
    private MenuItem.OnActionExpandListener geOnActionExpandListener(SearchView searchView) {
        return new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                searchView.setOnQueryTextListener(getOnQueryTextListener());
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mRestaurantViewModel.resetFilteredRestaurant();
                mRestaurantViewModel.getRestaurantFromMaps();
                return true;
            }
        };
    }

    private SearchView.OnQueryTextListener getOnQueryTextListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mRestaurantViewModel.getPrediction(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() < 2) {
                    return false;
                }
                mRestaurantViewModel.getPrediction(newText);

                //newText.length() == 0
                // loader --> getRetosFromMaps
                //   mRestaurantViewModel.getRestaurantFromMaps();


                return true;
            }
        };
    }

    private void setUpNavigation() {
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.authenticationFragment, R.id.mapsFragment, R.id.listFragment, R.id.workmatesFragment).build();
        NavigationUI.setupWithNavController(mBinding.bottomNavigation, mNavController);
        NavigationUI.setupWithNavController(mBinding.topNavBar, mNavController, appBarConfiguration);
    }

    public void setMenuVisibility(Boolean bool) {
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

    private void signOut() {
        LoginManager.getInstance().logOut();
        mFirebaseAuth.signOut();
        mLoginViewModel.authenticate(false);
        mUserViewModel.resetUserCompletionState();
    }

    private void initLoginViewModel() {
        mLoginViewModel.getAuthenticationState().observe(this,
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