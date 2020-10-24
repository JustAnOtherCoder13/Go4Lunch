package com.picone.go4lunch.presentation.ui.main;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.picone.go4lunch.presentation.utils.CustomAdapter;
import com.picone.go4lunch.presentation.utils.SearchViewHelper;
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

    public ActivityMainBinding mBinding;

    @Inject
    protected GoogleSignInClient mGoogleSignInClient;
    @Inject
    protected FirebaseAuth mFirebaseAuth;

    private UserViewModel mUserViewModel;
    private LoginViewModel mLoginViewModel;
    private RestaurantViewModel mRestaurantViewModel;
    private ChatViewModel mChatViewModel;
    private NavController mNavController;
    private SearchViewHelper searchViewHelper;
    private boolean isReservationIsCancelled = false;


    //TODO change settings doesn't update ui
    //TODO pass in multilanguage
    //TODO pass menu programaticaly?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        searchViewHelper = new SearchViewHelper(this, mRestaurantViewModel);

        initViewModel();
        initMenuButton();
        initInComingNavigation();
        initNavigation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (mFirebaseAuth.getCurrentUser() != null || accessToken != null && !accessToken.isExpired()) {
            initViewModelsValues();
            initSettingButtons();
            mRestaurantViewModel.getSelectedRestaurant.observe(this, restaurant -> {
                if (restaurant != null)
                    mNavController.navigate(R.id.restaurantDetailFragment);
            });
            mRestaurantViewModel.getUserChosenRestaurant.observe(this, restaurant ->
                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                        //TODO remove current user from interested users list
                        if (getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()) != null
                                && Objects.requireNonNull(mRestaurantViewModel.getCurrentUser.getValue()).getSettingValues().isNotificationSet())
                            mRestaurantViewModel.sendNotification(task.getResult(), createMessage(restaurant.getName(), restaurant.getAddress(), UserListToString(getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()).getInterestedUsers())));
                    }));
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

    private void initViewModel() {
        mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mRestaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
        mChatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
    }

    //--------------------------------- BUTTONS ------------------------------------------

    private void initMenuButton() {
        mBinding.topNavBar.setOnMenuItemClickListener(item -> {
            initTopNavBarItems(item);
            return false;
        });
        mBinding.navView.setNavigationItemSelectedListener(item -> {
            mBinding.drawerLayout.close();
            initDrawerMenuItems(item);
            return false;
        });
    }

    private void initDrawerMenuItems(MenuItem item) {
        if (mRestaurantViewModel.getCurrentUser.getValue() != null)
            switch (item.getItemId()) {
                case R.id.your_lunch_drawer_layout:
                    if (mRestaurantViewModel.getCurrentUser.getValue().getUserDailySchedules() != null && getUserDailyScheduleOnToday(mRestaurantViewModel.getCurrentUser.getValue().getUserDailySchedules()) != null) {
                        mRestaurantViewModel.setInterestedUsersForRestaurant(getUserDailyScheduleOnToday(mRestaurantViewModel.getCurrentUser.getValue().getUserDailySchedules()).getRestaurantPlaceId());
                    } else
                        Toast.makeText(this, R.string.haven_t_chose_restaurant, Toast.LENGTH_SHORT).show();
                    break;

                case R.id.settings_drawer_layout:
                    isReservationIsCancelled = false;

                    if (mBinding.settingsViewInclude.settings.getVisibility() == View.GONE) {
                        setSettingsVisibility(true);
                        initDropDownMenu();
                    }
                    mBinding.settingsViewInclude.saveChangesNoButtonSettings.setOnClickListener(v ->
                            setSettingsVisibility(false));
                    break;
                case R.id.logout_drawer_layout:
                    signOut();
                    break;
                case R.id.chat_drawer_layout:
                    mChatViewModel.setAllMessages();
                    //TODO change navigation
                    mNavController.navigate(R.id.chatFragment);
            }
    }

    private void initTopNavBarItems(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.top_nav_bar_menu_button:
                mBinding.drawerLayout.open();
                break;
            case R.id.top_nav_search_button:
                searchViewHelper.initSearchView(item);
                break;
        }
    }

    private void initDropDownMenu() {
        String[] languages = {(getString(R.string.English)), (getString(R.string.French))};
        int[] flags = {(R.drawable.ic_united_kingdom_flag_30), (R.drawable.ic_french_flag_30)};

        CustomAdapter adapter = new CustomAdapter(this, languages, flags);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.settingsViewInclude.languageTxtView.setAdapter(adapter);
    }

    private void initSettingButtons() {
        setSettingsVisibility(false);
        mBinding.settingsViewInclude.saveChangesNoButtonSettings.setOnClickListener(v ->
                setSettingsVisibility(false));
        mBinding.settingsViewInclude.saveChangesYesButtonSettings.setOnClickListener(v ->
                initAlertDialog());
        mBinding.settingsViewInclude.cancelReservationBtn.setOnClickListener(v -> {
            if (getUserDailyScheduleOnToday(Objects.requireNonNull
                    (mRestaurantViewModel.getCurrentUser.getValue()).getUserDailySchedules()) != null)
                isReservationIsCancelled = true;
            else
                Toast.makeText(this, R.string.haven_t_chose_restaurant, Toast.LENGTH_SHORT).show();
        });
    }

    private void initAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.change_settings)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, (dialog, which) -> saveChanges())
                .create()
                .show();
    }

    //--------------------------------- NAVIGATION ------------------------------------------

    private void initInComingNavigation() {
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

    private void initNavigation() {
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.authenticationFragment, R.id.mapsFragment, R.id.listFragment, R.id.workmatesFragment).build();
        NavigationUI.setupWithNavController(mBinding.bottomNavigation, mNavController);
        NavigationUI.setupWithNavController(mBinding.topNavBar, mNavController, appBarConfiguration);
    }

    private void saveChanges() {
        mUserViewModel.updateUserSettingValues(Objects.requireNonNull(mRestaurantViewModel.getCurrentUser.getValue())
                , new SettingValues(Objects.requireNonNull(mBinding.settingsViewInclude.languageSpinnerSettings.getEditText()).getText().toString().trim(),
                        mBinding.settingsViewInclude.notificationSwitchButton.isChecked()));
        if (isReservationIsCancelled) {
            mRestaurantViewModel.cancelReservation();
        }
        setSettingsVisibility(false);
    }

    private void signOut() {
        LoginManager.getInstance().logOut();
        mFirebaseAuth.signOut();
        mLoginViewModel.authenticate(false);
        mUserViewModel.resetUserCompletionState();
    }

    //--------------------------------- UPDATE VALUE ------------------------------------------

    private void initViewModelsValues() {
        mLoginViewModel.authenticate(true);
        mRestaurantViewModel.resetSelectedRestaurant();
        mUserViewModel.setAllDbUsers();
        mRestaurantViewModel.setAllDbRestaurants();
        mUserViewModel.getAllUsers.observe(this, users ->
                mRestaurantViewModel.setCurrentUser(Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getEmail()));
        mRestaurantViewModel.getAllDbRestaurants.observe(this, restaurants ->
                mRestaurantViewModel.updateAllRestaurantsWithPersistedValues(restaurants));
    }

    //--------------------------------- UI VISIBILITY ------------------------------------------

    private void setSettingsVisibility(boolean isVisible) {
        if (isVisible) {
            mBinding.settingsViewInclude.settings.setVisibility(View.VISIBLE);
            mBinding.settingsViewInclude.settingsFrame.setVisibility(View.VISIBLE);
        } else {
            mBinding.settingsViewInclude.settings.setVisibility(View.GONE);
            mBinding.settingsViewInclude.settingsFrame.setVisibility(View.GONE);
        }
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

    public void setWindowFlag(final int bits, boolean on) {
        Window win = this.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) winParams.flags |= bits;
        else winParams.flags &= ~bits;
        win.setAttributes(winParams);
    }
}