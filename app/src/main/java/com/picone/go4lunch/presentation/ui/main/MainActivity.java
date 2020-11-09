package com.picone.go4lunch.presentation.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.core.domain.entity.user.SettingValues;
import com.picone.core.domain.entity.user.User;
import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.ActivityMainBinding;
import com.picone.go4lunch.presentation.helpers.CustomAdapter;
import com.picone.go4lunch.presentation.helpers.ErrorHandler;
import com.picone.go4lunch.presentation.helpers.LocaleHelper;
import com.picone.go4lunch.presentation.helpers.SearchViewHelper;
import com.picone.go4lunch.presentation.viewModels.ChatViewModel;
import com.picone.go4lunch.presentation.viewModels.LoginViewModel;
import com.picone.go4lunch.presentation.viewModels.RestaurantViewModel;
import com.picone.go4lunch.presentation.viewModels.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.scopes.ActivityScoped;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;
import static com.picone.core.utils.FindInListUtil.getRestaurantDailyScheduleOnToday;
import static com.picone.core.utils.FindInListUtil.getUserDailyScheduleOnToday;


@ActivityScoped
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding mBinding;
    public LottieAnimationView mAnimationView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initViewModel();
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        searchViewHelper = new SearchViewHelper(this, mRestaurantViewModel, mUserViewModel);
        initLoadingAnimation();
        initToolBar();
        initMenuButton();
        initInComingNavigation();
        initNavigation();
    }

    private void initToolBar() {
        setSupportActionBar(mBinding.topNavBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_icon);
        getSupportActionBar().setTitle(R.string.i_am_hungry_title);
    }

    private BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;

    @Override
    protected void onStart() {
        super.onStart();
        initBottomSheet();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (mFirebaseAuth.getCurrentUser() != null || accessToken != null && !accessToken.isExpired()) {

            mRestaurantViewModel.getAllDbRestaurants.observe(this, restaurants ->
                    mRestaurantViewModel.updateAllRestaurantsWithPersistedValues(null));
            mLoginViewModel.authenticate(true);
            mRestaurantViewModel.getSelectedRestaurant.observe(this, restaurant -> {
                if (restaurant != null)
                    mNavController.navigate(R.id.restaurantDetailFragment);
            });
            mRestaurantViewModel.getUserChosenRestaurant.observe(this, this::initNotificationMessage);

            mRestaurantViewModel.getAllFilteredUsers.observe(this, users ->
                    mUserViewModel.setAllUsersMutableLiveData(users));

            mRestaurantViewModel.getErrorState.observe(this, error_state -> {
                if (error_state.equals(ErrorHandler.ON_ERROR)) {
                    Toast.makeText(this, ErrorHandler.ON_ERROR.label, Toast.LENGTH_LONG).show();
                    playLoadingAnimation(false);
                }
            });
            Toast.makeText(this, getResources().getString(R.string.welcome_back_message) + mFirebaseAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_LONG).show();
        }
    }

    private void initBottomSheet() {
        mBinding.bottomSheetInclude.settingsLayout.setVisibility(View.GONE);
        bottomSheetBehavior = BottomSheetBehavior.from(mBinding.bottomSheetInclude.bottomSheet);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == STATE_EXPANDED) {
                    mBinding.bottomSheetInclude.settingsLayout.setVisibility(View.VISIBLE);
                    mBinding.bottomSheetInclude.openBtn.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                    mBinding.bottomSheetInclude.openBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));

                } else {
                    mBinding.bottomSheetInclude.settingsLayout.setVisibility(View.GONE);
                    mBinding.bottomSheetInclude.openBtn.setImageResource(R.drawable.ic_settings_icon);
                    mBinding.bottomSheetInclude.openBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        mBinding.bottomSheetInclude.openBtn.setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() == STATE_EXPANDED) {
                bottomSheetBehavior.setState(STATE_COLLAPSED);
            } else {
                initDropDownMenu();
                bottomSheetBehavior.setState(STATE_EXPANDED);}
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_nav_bar_menu, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        boolean bol = mNavController.getCurrentDestination().getId() == R.id.mapsFragment;
        if (bol) {
            LocaleHelper.persist(this, Objects.requireNonNull(mRestaurantViewModel.getCurrentUser.getValue()).getSettingValues().getChosenLanguage());
            finish();
        } else super.onBackPressed();
        mRestaurantViewModel.resetSelectedRestaurant();
        mNavController.navigateUp();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mBinding.drawerLayout.open();
                break;
            case R.id.top_nav_search_button:
                searchViewHelper.initSearchView(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViewModel() {
        mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mRestaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
        mChatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
    }

    //--------------------------------- BUTTONS ------------------------------------------

    private void initMenuButton() {
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
                        mRestaurantViewModel.setInterestedUsersForRestaurant(getUserDailyScheduleOnToday(mRestaurantViewModel.getCurrentUser.getValue().getUserDailySchedules()).getRestaurantPlaceId(), mRestaurantViewModel.getAllRestaurants.getValue());
                    } else
                        Toast.makeText(this, R.string.haven_t_chose_restaurant, Toast.LENGTH_SHORT).show();
                    break;

                case R.id.settings_drawer_layout:
                    break;
                case R.id.logout_drawer_layout:
                    signOut();
                    break;
                case R.id.chat_drawer_layout:
                    mChatViewModel.setAllMessages();
                    mNavController.navigate(R.id.chatFragment);
            }
    }

    private void initDropDownMenu() {
        String[] languages = {(getString(R.string.English)), (getString(R.string.French))};
        int[] flags = {(R.drawable.ic_united_kingdom_flag_30), (R.drawable.ic_french_flag_30)};

        CustomAdapter adapter = new CustomAdapter(this, languages, flags);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.bottomSheetInclude.languageTxtView.setAdapter(adapter);
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
        NavigationUI.setupWithNavController(mBinding.bottomNavigation, mNavController);
    }

    //--------------------------------- UI VISIBILITY -----------------------------------------

    public void setStatusBarTransparency(boolean isTransparent) {
        if (isTransparent) {

            if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
                setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
                setWindowFlag(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, true);
            }

            if (Build.VERSION.SDK_INT >= 21) {
                setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
                setWindowFlag(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, true);
            }
        } else {
            if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
                setWindowFlag(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, false);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            if (Build.VERSION.SDK_INT >= 21) {
                setWindowFlag(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, false);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }
    }

    public void setMenuVisibility(Boolean isVisible) {
        if (isVisible) {
            mBinding.topNavBar.setVisibility(View.VISIBLE);
            mBinding.bottomNavigation.setVisibility(View.VISIBLE);
            mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            mBinding.topNavBar.setVisibility(View.GONE);
            mBinding.bottomNavigation.setVisibility(View.GONE);
            mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    //--------------------------------- HELPERS ------------------------------------------

    private void initNotificationMessage(Restaurant restaurant) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (restaurant != null
                    && restaurant.getRestaurantDailySchedules() != null
                    && getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()) != null
                    && Objects.requireNonNull(mRestaurantViewModel.getCurrentUser.getValue()).getSettingValues().isNotificationSet()) {

                List<User> userToPass = new ArrayList<>(getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()).getInterestedUsers());

                for (User user : getRestaurantDailyScheduleOnToday(restaurant.getRestaurantDailySchedules()).getInterestedUsers())
                    if (user.getUid().equals(mRestaurantViewModel.getCurrentUser.getValue().getUid()))
                        userToPass.remove(user);

                mRestaurantViewModel.sendNotification(task.getResult(),
                        getString(R.string.today_lunch), createMessage(restaurant.getName(), restaurant.getAddress(), UserListToString(userToPass)));
            }
        });
    }

    private String createMessage(String restaurantName, String restaurantAddress, String interestedUsers) {
        return (getString(R.string.notification_you_are_eating) + restaurantName + getString(R.string.notification_at) + restaurantAddress + getString(R.string.notification_with) + interestedUsers);
    }

    private String UserListToString(List<User> interestedUsers) {
        String interestedUsersStr = " ";
        if (interestedUsers != null && !interestedUsers.isEmpty())
            for (User interestedUser : interestedUsers)

                if (interestedUsersStr.trim().isEmpty())
                    interestedUsersStr = interestedUser.getName();
                else
                    interestedUsersStr = interestedUsersStr.concat(", ").concat(interestedUser.getName());

        return interestedUsersStr;
    }

    public void saveChanges() {
        SettingValues currentUserSettingValues = Objects.requireNonNull(mRestaurantViewModel.getCurrentUser.getValue()).getSettingValues();
        String language = Objects.requireNonNull(mBinding.bottomSheetInclude.languageSpinnerSettings.getEditText()).getText().toString();

        if (!language.equalsIgnoreCase(currentUserSettingValues.getChosenLanguage())
                || mBinding.bottomSheetInclude.notificationSwitchButton.isChecked() != currentUserSettingValues.isNotificationSet()
                || mBinding.bottomSheetInclude.cancelReservationToggleButton.isChecked()) {

            setLanguageAndRestart(language);

            mUserViewModel.updateUserSettingValues(Objects.requireNonNull(mRestaurantViewModel.getCurrentUser.getValue())
                    , new SettingValues(Objects.requireNonNull(mBinding.bottomSheetInclude.languageSpinnerSettings.getEditText()).getText().toString().trim(),
                            mBinding.bottomSheetInclude.notificationSwitchButton.isChecked()));

            if (mBinding.bottomSheetInclude.cancelReservationToggleButton.isChecked()) {
                if (getUserDailyScheduleOnToday(Objects.requireNonNull
                        (mRestaurantViewModel.getCurrentUser.getValue()).getUserDailySchedules()) == null)
                    Toast.makeText(this, R.string.haven_t_chose_restaurant, Toast.LENGTH_SHORT).show();
                else
                    mRestaurantViewModel.cancelReservation(mRestaurantViewModel.getAllRestaurants.getValue());
            }
        }
    }

    private void setLanguageAndRestart(String language) {
        if (!language.equalsIgnoreCase(Objects.requireNonNull(mRestaurantViewModel.getCurrentUser.getValue()).getSettingValues().getChosenLanguage())) {
            LocaleHelper.setNewLocale(this, language);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void signOut() {
        LoginManager.getInstance().logOut();
        mFirebaseAuth.signOut();
        mLoginViewModel.authenticate(false);
        mUserViewModel.resetUserCompletionState();
    }

    private void setWindowFlag(final int bits, boolean on) {
        Window win = this.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) winParams.flags |= bits;
        else winParams.flags &= ~bits;
        win.setAttributes(winParams);
    }

    private void initLoadingAnimation() {
        mAnimationView = mBinding.animationViewInclude.animationView;
        mAnimationView.setAnimation(R.raw.loading_animation);
        mAnimationView.setVisibility(View.GONE);
        playLoadingAnimation(true);
    }

    public void playLoadingAnimation(boolean bol) {
        if (bol) {
            mAnimationView.setVisibility(View.VISIBLE);
            mAnimationView.playAnimation();
        } else if (mAnimationView != null) {
            mAnimationView.setVisibility(View.GONE);
            mAnimationView.pauseAnimation();
        }
    }
}
