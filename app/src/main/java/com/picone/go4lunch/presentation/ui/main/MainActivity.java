package com.picone.go4lunch.presentation.ui.main;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.ActivityMainBinding;
import com.picone.go4lunch.presentation.viewModels.LoginViewModel;
import com.picone.go4lunch.presentation.viewModels.UserViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.scopes.ActivityScoped;

import static com.picone.go4lunch.presentation.ui.fragment.MapsFragment.MAPS_KEY;

@ActivityScoped
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    //TODO add google search view on restaurants only, update view automatically place autocomplete api
    //TODO add push message on 12h with chosen restaurant name, address, and list of interested users
    //TODO create a chat


    public ActivityMainBinding mBinding;

    @Inject
    protected GoogleSignInClient mGoogleSignInClient;
    @Inject
    protected FirebaseAuth mFirebaseAuth;

    private UserViewModel mUserViewModel;
    private LoginViewModel mLoginViewModel;
    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
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
                    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                    SearchView searchView = item.getActionView().findViewById(R.id.top_nav_search_button);
                    assert searchManager != null;
                    searchView.setSearchableInfo(
                            searchManager.getSearchableInfo(getComponentName()));
                    searchView.setQueryHint("Search restaurants");
                    searchView.setBackgroundColor(Color.WHITE);
                    searchView.setOnQueryTextListener(getOnQueryTextListener());

                    break;
            }
            return false;
        });
        mBinding.navView.setNavigationItemSelectedListener(item -> {
            mBinding.drawerLayout.close();
            switch (item.getItemId()) {
                case R.id.your_lunch_drawer_layout:
                    mNavController.navigate(R.id.restaurantDetailFragment);
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

    private SearchView.OnQueryTextListener getOnQueryTextListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getPlacePredictions(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
    }

    private void getPlacePredictions(String query) {
        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        // Create a RectangularBounds object.
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596));
        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
                .setOrigin(new LatLng(-33.8749937, 151.2041382))
                .setCountries("AU", "NZ")
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(query)
                .build();

        Places.initialize(getApplicationContext(), MAPS_KEY);
        PlacesClient placesClient = Places.createClient(this);
        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                Log.i("TAG", prediction.getPlaceId());
                Log.i("TAG", prediction.getPrimaryText(null).toString());
            }
        })/*.addOnFailureListener(exception -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e("TAG", "Place not found: " + apiException.getStatusCode());
            }
        })*/;
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