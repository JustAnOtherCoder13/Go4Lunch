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
import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.FragmentRestaurantDetailBinding;
import com.picone.go4lunch.presentation.ui.fragments.adapters.ColleagueRecyclerViewAdapter;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;
import com.picone.go4lunch.presentation.viewModels.RestaurantViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        initView();
    }

    private void initRecyclerView() {
        mAdapter = new ColleagueRecyclerViewAdapter(new ArrayList<>(), TAG);
        mBinding.recyclerViewRestaurantDetail.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerViewRestaurantDetail.setAdapter(mAdapter);
    }

    @SuppressWarnings("ConstantConditions")
    //mAuth.getEmail can't be null cause user have to connect with facebook or google account
    private void initView() {
        mBinding.chooseRestaurantFab.setVisibility(View.GONE);
        mUserViewModel.getCurrentUserForEmail(mAuth.getCurrentUser().getEmail()).observe(getViewLifecycleOwner(),
                currentPersistedUser -> {
                    User finalCurrentUser = currentPersistedUser.get(0);
                    Date finalToday = today;

                    if (getArguments() != null) {
                        //if user come from restaurant list
                        mBinding.chooseRestaurantFab.setVisibility(View.VISIBLE);
                        mRestaurantViewModel.setSelectedRestaurant(getArguments().getInt("position"));
                        mRestaurantViewModel.getSelectedRestaurant.observe(getViewLifecycleOwner(), selectedRestaurant -> {
                            initRestaurantDetail(selectedRestaurant);
                            deleteDailyScheduleAndAssociatedUsersForRestaurantWhenDateIsPassed(selectedRestaurant);
                            mRestaurantViewModel.getAllInterestedUsersForRestaurant(finalToday, selectedRestaurant.getName()).observe(getViewLifecycleOwner(),
                                    persistedUsers -> {
                                        mAdapter.updateUsers(persistedUsers);
                                        mRestaurantViewModel.getCompletionState.observe(getViewLifecycleOwner(),
                                                completionState -> {
                                                    if (completionState.equals(RestaurantViewModel.CompletionState.INTERESTED_USERS_LOADED)) {
                                                        initClickOnFirstTimeChoice(finalCurrentUser, finalToday, selectedRestaurant, persistedUsers);
                                                        //In case current user have already chose a restaurant
                                                        mRestaurantViewModel.getGlobalCurrentUser(finalCurrentUser).observe(getViewLifecycleOwner(),
                                                                globalInterestedUser -> mBinding.chooseRestaurantFab.setOnClickListener(v -> {
                                                                    initClickOnUserHaveAlreadyChoseRestaurant(selectedRestaurant, persistedUsers, globalInterestedUser);
                                                                }));
                                                    }
                                                });
                                    });
                        });
                    } else {
                        //if user come from your lunch menu button
                        mRestaurantViewModel.getGlobalCurrentUser(finalCurrentUser).observe(getViewLifecycleOwner(),
                                globalCurrentUser -> {
                                    initRestaurantDetail(globalCurrentUser.getSelectedRestaurant());
                                    mRestaurantViewModel.getAllInterestedUsersForRestaurant(finalToday, globalCurrentUser.getSelectedRestaurant().getName()).observe(getViewLifecycleOwner(),
                                            persistedInterestedUsers -> mAdapter.updateUsers(persistedInterestedUsers));
                                });
                    }
                });
    }

    private void initClickOnUserHaveAlreadyChoseRestaurant(Restaurant selectedRestaurant, List<User> persistedUsers, User globalInterestedUser) {
        if (!globalInterestedUser.getSelectedRestaurant().getName().equals(selectedRestaurant.getName()))
            initAlertDialogWhenUserHaveAlreadyChoseRestaurant(selectedRestaurant, globalInterestedUser, persistedUsers);
        else {
            Toast.makeText(getContext(), R.string.you_have_already_chose_this_restaurant, Toast.LENGTH_SHORT).show();
        }
    }

    private void initClickOnFirstTimeChoice(User finalCurrentUser, Date finalToday, Restaurant selectedRestaurant, List<User> persistedUsers) {
        if (persistedUsers.isEmpty()) {
            mBinding.chooseRestaurantFab.setOnClickListener(v ->
                    updateUsersWhenNoUserExistForThisRestaurant(finalToday, selectedRestaurant, finalCurrentUser));
        } else
            mBinding.chooseRestaurantFab.setOnClickListener(v ->
                    updateUsersWhenInterestedUsersExistForThisRestaurant(finalToday, selectedRestaurant, finalCurrentUser));
    }

    private void initRestaurantDetail(Restaurant selectedRestaurant) {
        mBinding.restaurantNameDetailTextView.setText(selectedRestaurant.getName());
        mBinding.foodStyleAndAddressDetailTextView.setText(selectedRestaurant.getFoodType()
                .concat(" - ").concat(selectedRestaurant.getAddress()));
    }

    private void initAlertDialogWhenUserHaveAlreadyChoseRestaurant(Restaurant selectedRestaurant, User globalInterestedUser, List<User> interestedUsers) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.you_have_already_choose_a_restaurant);
        builder.setMessage("\" ".concat(globalInterestedUser.getSelectedRestaurant().getName()).concat(" \"").concat("\n \nWould you like to change?"));
        builder.setPositiveButton(R.string.positive_button, (dialog, which) -> changeRestaurantForUser(globalInterestedUser, selectedRestaurant, interestedUsers));
        builder.setNegativeButton(R.string.negative_button, null);
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

    private void deleteDailyScheduleAndAssociatedUsersForRestaurantWhenDateIsPassed(Restaurant selectedRestaurant) {
        mRestaurantViewModel.getDailyScheduleForRestaurant(selectedRestaurant.getName()).observe(getViewLifecycleOwner(), dailySchedule -> {
            if (!dailySchedule.getToday().equals(today)) {
                if (dailySchedule.getInterestedUsers() != null) {
                    for (User persistedInterestedUser : dailySchedule.getInterestedUsers()) {
                        mRestaurantViewModel.deleteUserFromGlobalList(persistedInterestedUser);
                        mRestaurantViewModel.deleteCurrentUserFromRestaurant(dailySchedule.getToday(), selectedRestaurant.getName(), persistedInterestedUser);
                    }
                } else {
                    mRestaurantViewModel.deleteDailyScheduleFromRestaurant(selectedRestaurant.getName());
                }
            }
        });
    }

    private void updateUsersWhenInterestedUsersExistForThisRestaurant(Date finalToday, Restaurant selectedRestaurant, User currentUser) {
        mRestaurantViewModel.getAllInterestedUsersForRestaurant(finalToday, selectedRestaurant.getName())
                .observe(getViewLifecycleOwner(), persistedUsers -> {
                    mRestaurantViewModel.addCurrentUserToRestaurant(finalToday, selectedRestaurant.getName(), currentUser);
                    currentUser.setSelectedRestaurant(selectedRestaurant);
                    mRestaurantViewModel.addCurrentUserToGlobalList(currentUser);
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
                case DAILY_SCHEDULE_IS_NOT_LOADED:
                    mRestaurantViewModel.addDailyScheduleToRestaurant(finalToday, null, selectedRestaurant.getName());
                    break;
                case DAILY_SCHEDULE_ON_COMPLETE:
                case DAILY_SCHEDULE_IS_LOADED:
                    mRestaurantViewModel.addCurrentUserToRestaurant(finalToday, selectedRestaurant.getName(), currentUser);
                    break;
                case PERSISTED_USERS_FOR_RESTAURANT_ON_COMPLETE:
                    mRestaurantViewModel.getAllInterestedUsersForRestaurant(finalToday, selectedRestaurant.getName()).observe(getViewLifecycleOwner(),
                            users -> {
                                currentUser.setSelectedRestaurant(selectedRestaurant);
                                mRestaurantViewModel.addCurrentUserToGlobalList(currentUser);
                            });
                    break;
            }
        });
    }
}