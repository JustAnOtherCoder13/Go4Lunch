package com.picone.go4lunch.presentation.ui.main;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.picone.core.domain.entity.user.SettingValues;
import com.picone.core.domain.entity.user.User;
import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.ActivityMainBinding;
import com.picone.go4lunch.databinding.SpinnerItemBinding;
import com.picone.go4lunch.presentation.utils.CustomAdapter;
import com.picone.go4lunch.presentation.viewModels.ChatViewModel;
import com.picone.go4lunch.presentation.viewModels.LoginViewModel;
import com.picone.go4lunch.presentation.viewModels.RestaurantViewModel;
import com.picone.go4lunch.presentation.viewModels.UserViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.scopes.ActivityScoped;

import static com.picone.go4lunch.presentation.utils.DailyScheduleHelper.getRestaurantDailyScheduleOnToday;
import static com.picone.go4lunch.presentation.utils.DailyScheduleHelper.getUserDailyScheduleOnToday;


@ActivityScoped
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    //TODO create a chat

    public ActivityMainBinding mBinding;
    private SpinnerItemBinding spinnerItemBinding;

    @Inject
    protected GoogleSignInClient mGoogleSignInClient;
    @Inject
    protected FirebaseAuth mFirebaseAuth;

    private UserViewModel mUserViewModel;
    private LoginViewModel mLoginViewModel;
    private RestaurantViewModel mRestaurantViewModel;
    private ChatViewModel mChatViewModel;
    private NavController mNavController;
    private SettingValues settingValues;
    private boolean isReservationIsCancelled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mRestaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
        mChatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        spinnerItemBinding = SpinnerItemBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        initMenuButton();
        setUpNavigation();
        initLoginViewModel();
        mRestaurantViewModel.getUserChosenRestaurant.observe(this, restaurant ->
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                    //TODO remove current user from interested users list
                    if (getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()) != null)
                        mRestaurantViewModel.sendNotification(task.getResult(), createMessage(restaurant.getName(), restaurant.getAddress(), UserListToString(getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()).getInterestedUsers())));
                }));
    }


    private String createMessage(String restaurantName, String restaurantAddress, String interestedUsers) {
        return (getString(R.string.notification_you_are_eating) + restaurantName + getString(R.string.notification_at) + restaurantAddress + getString(R.string.notification_with) + interestedUsers);
    }

    private String UserListToString(List<User> interestedUsers) {
        String interestedUsersStr = null;
        for (User interestedUser : interestedUsers)
            if (interestedUsersStr == null)
                interestedUsersStr = interestedUser.getName();
            else
                interestedUsersStr = interestedUsersStr.concat(", ").concat(interestedUser.getName());
        return interestedUsersStr;
    }

    @Override
    protected void onStart() {
        super.onStart();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (mFirebaseAuth.getCurrentUser() != null || accessToken != null && !accessToken.isExpired()) {
            mBinding.settingsViewInclude.settings.setVisibility(View.GONE);
            mBinding.settingsViewInclude.settingsFrame.setVisibility(View.GONE);
            mBinding.settingsViewInclude.saveChangesNoButtonSettings.setOnClickListener(v -> {
                mBinding.settingsViewInclude.settings.setVisibility(View.GONE);
                mBinding.settingsViewInclude.settingsFrame.setVisibility(View.GONE);
            });
            mBinding.settingsViewInclude.saveChangesYesButtonSettings.setOnClickListener(v -> {
                Log.i("TAG", "onStart: "+settingValues.getChosenLanguage()+" notif "+mBinding.settingsViewInclude.notificationSwitchButton.isChecked()+" "+isReservationIsCancelled);
            });
            mBinding.settingsViewInclude.cancelReservationBtn.setOnClickListener(v -> {
                isReservationIsCancelled = true;
            });
            mLoginViewModel.authenticate(true);
            mRestaurantViewModel.setCurrentUser(mFirebaseAuth.getCurrentUser().getEmail());
            mRestaurantViewModel.resetSelectedRestaurant();
            mUserViewModel.setAllDbUsers();
            mRestaurantViewModel.setAllDbRestaurants();

            mRestaurantViewModel.getSelectedRestaurant.observe(this, restaurant -> {
                if (restaurant != null)
                    mNavController.navigate(R.id.restaurantDetailFragment);
            });
            Toast.makeText(this, getResources().getString(R.string.welcome_back_message) + mFirebaseAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Objects.requireNonNull(mNavController.getCurrentDestination()).getId() == R.id.authenticationFragment) {
            this.finish();
        } else mNavController.navigateUp();
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
                    mRestaurantViewModel.getCurrentUser.observe(this, user -> {
                        if (user.getUserDailySchedules() != null && getUserDailyScheduleOnToday(user.getUserDailySchedules()) != null) {
                            mRestaurantViewModel.initSelectedRestaurant(getUserDailyScheduleOnToday(user.getUserDailySchedules()).getRestaurantPlaceId());
                        } else
                            Toast.makeText(this, "You haven't choose a restaurant yet", Toast.LENGTH_SHORT).show();
                    });
                    break;

                case R.id.settings_drawer_layout:
                    settingValues = new SettingValues(mRestaurantViewModel.getCurrentUser.getValue().getSettingValues().getChosenLanguage(),mRestaurantViewModel.getCurrentUser.getValue().getSettingValues().isNotificationSet());
                    isReservationIsCancelled = false;

                    if (mBinding.settingsViewInclude.settings.getVisibility() == View.GONE) {
                        mBinding.settingsViewInclude.settings.setVisibility(View.VISIBLE);
                        mBinding.settingsViewInclude.settingsFrame.setVisibility(View.VISIBLE);
                        initSpinner();
                    }
                    mBinding.settingsViewInclude.saveChangesNoButtonSettings.setOnClickListener(v -> {
                        mBinding.settingsViewInclude.settings.setVisibility(View.GONE);
                        mBinding.settingsViewInclude.settingsFrame.setVisibility(View.GONE);
                    });
                    break;
                case R.id.logout_drawer_layout:
                    signOut();
                    break;
                case R.id.chat_drawer_layout:
                    mChatViewModel.setAllMessages();
                    mNavController.navigate(R.id.chatFragment);
            }
            return false;
        });
    }

    private void initSpinner() {
        String[] languages;
        int[] flags;

        if (settingValues.getChosenLanguage().equalsIgnoreCase(getString(R.string.French))) {
            languages = new String[]{(getString(R.string.French)), (getString(R.string.English))};
            flags = new int[]{(R.drawable.ic_french_flag_30), (R.drawable.ic_united_kingdom_flag_30)};
        } else {
            spinnerItemBinding.spinnerTextView.setText(getString(R.string.English));
            languages = new String[]{(getString(R.string.English)), (getString(R.string.French))};
            flags = new int[]{(R.drawable.ic_united_kingdom_flag_30), (R.drawable.ic_french_flag_30)};
        }

        CustomAdapter adapter = new CustomAdapter(this, languages, flags);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.settingsViewInclude.languageSpinnerSettings.setAdapter(adapter);

        mBinding.settingsViewInclude.languageSpinnerSettings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(R.id.spinnerTextView);
                   settingValues.setChosenLanguage((String) textView.getText());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void setStatusBarTransparency(boolean isTransparent) {

        if (isTransparent) {
            if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
                setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
            }

            if (Build.VERSION.SDK_INT >= 21) {
                setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        } else {
            if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }

        }
    }

    public void setWindowFlag(final int bits, boolean on) {
        Window win = this.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
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