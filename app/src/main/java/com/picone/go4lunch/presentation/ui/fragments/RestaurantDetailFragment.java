package com.picone.go4lunch.presentation.ui.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.go4lunch.databinding.FragmentRestaurantDetailBinding;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;
import com.picone.go4lunch.presentation.ui.fragments.adapters.ColleagueRecyclerViewAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class RestaurantDetailFragment extends BaseFragment {

    public static final String TAG = RestaurantDetailFragment.class.getName();

    private final static Calendar CALENDAR = Calendar.getInstance();
    private final static int MY_DAY_OF_MONTH = CALENDAR.get(Calendar.DAY_OF_MONTH);
    private final static int MY_MONTH = CALENDAR.get(Calendar.MONTH);
    private final static int MY_YEAR = CALENDAR.get(Calendar.YEAR);

    private FragmentRestaurantDetailBinding mBinding;
    private ColleagueRecyclerViewAdapter mAdapter;
    private Date today;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            today = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(MY_DAY_OF_MONTH + "/" + MY_MONTH + "/" + MY_YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentRestaurantDetailBinding.inflate(inflater, container, false);
        showAppBars(false);
        initRecyclerView();
        initView();
        return mBinding.getRoot();
    }

    private void initRecyclerView() {
        mAdapter = new ColleagueRecyclerViewAdapter(new ArrayList<>(), TAG);
        mBinding.recyclerViewRestaurantDetail.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerViewRestaurantDetail.setAdapter(mAdapter);
    }

    private void initView() {
        Date finalToday = today;
        if (getArguments() != null) {
            //if users come from restaurant list
            mRestaurantViewModel.setSelectedRestaurant(getArguments().getInt("position"));
            mRestaurantViewModel.getSelectedRestaurant.observe(getViewLifecycleOwner(), selectedRestaurant -> {
                mUserViewModel.getAllUsers().observe(getViewLifecycleOwner(), allUsers -> {
                    User finalCurrentUser = getCurrentPersistedUser(allUsers);
                    deleteDailyScheduleAndAssociatedUsersForRestaurantWhenDateIsPassed(selectedRestaurant);
                    mRestaurantViewModel.getAllInterestedUsersForRestaurant(finalToday, selectedRestaurant.getName()).observe(getViewLifecycleOwner(),
                            persistedUsers -> {
                                mAdapter.updateUsers(persistedUsers);
                                initRestaurantChoiceFabClickListener(finalCurrentUser, selectedRestaurant, persistedUsers);
                                mRestaurantViewModel.getGlobalCurrentUser(finalCurrentUser).observe(getViewLifecycleOwner(),
                                        globalInterestedUser -> mBinding.chooseRestaurantFab.setOnClickListener(v ->
                                                initAlertDialogOnUserHasAlreadyChoseRestaurant(selectedRestaurant,globalInterestedUser,persistedUsers)));
                            });
                });
                initRestaurantView(selectedRestaurant);
            });
        } else {
            //if user come from your lunch menu button
            mUserViewModel.getAllUsers().observe(getViewLifecycleOwner(), allUsers -> mRestaurantViewModel.getGlobalCurrentUser(getCurrentPersistedUser(allUsers)).observe(getViewLifecycleOwner(),
                    globalCurrentUser -> {
                        initRestaurantView(globalCurrentUser.getSelectedRestaurant());
                        //TODO have to manage case that user click on same restaurant
                        mRestaurantViewModel.getAllInterestedUsersForRestaurant(finalToday, globalCurrentUser.getSelectedRestaurant().getName()).observe(getViewLifecycleOwner(),
                                persistedInterestedUsers -> mAdapter.updateUsers(persistedInterestedUsers));
                    }));
        }

    }

    private void initRestaurantView(Restaurant selectedRestaurant) {
        mBinding.restaurantNameDetailTextView.setText(selectedRestaurant.getName());
        mBinding.foodStyleAndAddressDetailTextView.setText(selectedRestaurant.getFoodType()
                .concat(" - ").concat(selectedRestaurant.getAddress()));
    }

    private void initAlertDialogOnUserHasAlreadyChoseRestaurant(Restaurant selectedRestaurant, User globalInterestedUser, List<User> interestedUsers) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("\" ".concat(globalInterestedUser.getSelectedRestaurant().getName()).concat(" \"").concat("\n \nWould you like to change?"));
        builder.setPositiveButton("Yes", (dialog, which) -> changeRestaurantForUser(globalInterestedUser, selectedRestaurant, interestedUsers));
        builder.setNegativeButton("No", null);
        builder.create().show();
    }

    private void changeRestaurantForUser(User user, Restaurant selectedRestaurant, List<User> interestedUsers) {
        mRestaurantViewModel.deleteCurrentUserFromRestaurant(today, user.getSelectedRestaurant().getName(), user);
        if (interestedUsers.isEmpty()) {
            updateUsersWhenNoUserExistForThisRestaurant(today, selectedRestaurant, user);
        } else {
            updateUsersWhenInterestedUsersExistForThisRestaurant(today, selectedRestaurant, user);
        }
        user.setSelectedRestaurant(selectedRestaurant);
        mRestaurantViewModel.addCurrentUserToGlobalList(user);
    }

    @SuppressWarnings("ConstantConditions")
    //getEmail can't be null cause google or facebook auth is required.
    private User getCurrentPersistedUser(List<User> allUsers) {
        User currentUser = null;
        for (User user : allUsers) {
            if (user.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                currentUser = user;
            }
        }
        return currentUser;
    }

    private void deleteDailyScheduleAndAssociatedUsersForRestaurantWhenDateIsPassed(Restaurant selectedRestaurant) {
        mRestaurantViewModel.getDailyScheduleForRestaurant(selectedRestaurant.getName()).observe(getViewLifecycleOwner(), dailySchedule -> {
            if (!dailySchedule.getToday().equals(today.toString())) {
                if (dailySchedule.getInterestedUsers() != null) {
                    for (User persistedInterestedUser : dailySchedule.getInterestedUsers()) {
                        mRestaurantViewModel.deleteUserFromGlobalList(persistedInterestedUser);
                    }
                }
                mRestaurantViewModel.deleteDailyScheduleForRestaurant(selectedRestaurant.getName());
            }
        });
    }

    private void initRestaurantChoiceFabClickListener(User currentUser, Restaurant selectedRestaurant, List<User> interestedUsers) {
        Date finalToday = today;
        if (interestedUsers.isEmpty()) {
            mBinding.chooseRestaurantFab.setOnClickListener(v ->
                    updateUsersWhenNoUserExistForThisRestaurant(finalToday, selectedRestaurant, currentUser));
        } else {
            mBinding.chooseRestaurantFab.setOnClickListener(v ->
                    updateUsersWhenInterestedUsersExistForThisRestaurant(finalToday, selectedRestaurant, currentUser));
        }
    }

    private void updateUsersWhenInterestedUsersExistForThisRestaurant(Date finalToday, Restaurant selectedRestaurant, User currentUser) {

        mRestaurantViewModel.getAllInterestedUsersForRestaurant(finalToday, selectedRestaurant.getName())
                .observe(getViewLifecycleOwner(), persistedUsers -> {

                    boolean isCurrentUserAlreadyExistForThisRestaurant = false;
                    for (User persistedUser : persistedUsers) {
                        if (currentUser.getEmail().equals(persistedUser.getEmail())) {
                            isCurrentUserAlreadyExistForThisRestaurant = true;
                            break;
                        }
                    }
                    if (!isCurrentUserAlreadyExistForThisRestaurant){
                        mRestaurantViewModel.addCurrentUserToRestaurant(finalToday, selectedRestaurant.getName(), currentUser);
                        currentUser.setSelectedRestaurant(selectedRestaurant);
                        mRestaurantViewModel.addCurrentUserToGlobalList(currentUser);
                    }
                });
    }

    private void updateUsersWhenNoUserExistForThisRestaurant(Date finalToday, Restaurant selectedRestaurant, User currentUser) {
        mRestaurantViewModel.getPersistedRestaurant(selectedRestaurant.getName()).observe(getViewLifecycleOwner(), restaurant -> {
        });

        mRestaurantViewModel.getCompletionState.observe(getViewLifecycleOwner(), completionState -> {
            switch (completionState) {
                case RESTAURANT_IS_NOT_PERSISTED:
                    mRestaurantViewModel.addRestaurant(selectedRestaurant);
                    break;
                case RESTAURANT_ON_COMPLETE:
                case DAILY_SCHEDULE_IS_NOT_PERSISTED:
                    mRestaurantViewModel.addDailyScheduleToRestaurant(finalToday, null, selectedRestaurant.getName());
                    break;
                case DAILY_SCHEDULE_ON_COMPLETE:
                case DAILY_SCHEDULE_IS_PERSISTED:
                    mRestaurantViewModel.addCurrentUserToRestaurant(finalToday, selectedRestaurant.getName(), currentUser);
                    break;
                case PERSISTED_USERS_FOR_RESTAURANT_ON_COMPLETE:
                    mRestaurantViewModel.getAllInterestedUsersForRestaurant(finalToday,selectedRestaurant.getName()).observe(getViewLifecycleOwner(),
                            users -> {
                        currentUser.setSelectedRestaurant(selectedRestaurant);
                        mRestaurantViewModel.addCurrentUserToGlobalList(currentUser);
                    });
                    break;
            }
        });
    }
}