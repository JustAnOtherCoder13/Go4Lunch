package com.picone.go4lunch.presentation.ui.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.go4lunch.databinding.FragmentRestaurantDetailBinding;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;
import com.picone.go4lunch.presentation.utils.RecyclerViewAdapter;
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

    private FragmentRestaurantDetailBinding mBinding;
    private RecyclerViewAdapter mAdapter;
    private final static Calendar CALENDAR = Calendar.getInstance();
    private final static int MY_DAY_OF_MONTH = CALENDAR.get(Calendar.DAY_OF_MONTH);
    private final static int MY_MONTH = CALENDAR.get(Calendar.MONTH);
    private final static int MY_YEAR = CALENDAR.get(Calendar.YEAR);
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

    @SuppressWarnings("ConstantConditions")
    //suppress warning is safe cause CurrentUser must be initialized to enter app
    private void initView() {
        Date finalToday = today;

        if (getArguments() != null) {
            mRestaurantViewModel.setSelectedRestaurant(getArguments().getInt("position"));
        }

        mRestaurantViewModel.getSelectedRestaurant.observe(getViewLifecycleOwner(), selectedRestaurant -> {
            mUserViewModel.getAllUsers().observe(getViewLifecycleOwner(), allUsers -> {
                User currentUser = null;
                for (User user : allUsers) {
                    if (user.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                        currentUser = user;
                    }
                }
                User finalCurrentUser = currentUser;

                manageDailySchedule(selectedRestaurant);

                mRestaurantViewModel.isUserHasChoseRestaurant.observe(getViewLifecycleOwner(),
                        aBoolean -> { if (!aBoolean) {
                                mRestaurantViewModel.getInterestedUsersForRestaurant(finalToday, selectedRestaurant.getName()).observe(getViewLifecycleOwner(),
                                        persistedUsers -> {
                                            initRestaurantChoiceFabClickListener(finalCurrentUser, selectedRestaurant, persistedUsers);
                                            mAdapter.updateUsers(persistedUsers);
                                        });
                            }
                        });
            });
            mBinding.restaurantNameDetailTextView.setText(selectedRestaurant.getName());
            mBinding.foodStyleAndAddressDetailTextView.setText(selectedRestaurant.getFoodType()
                    .concat(" - ").concat(selectedRestaurant.getAddress()));
        });
    }

    private void manageDailySchedule(Restaurant selectedRestaurant) {
        mRestaurantViewModel.getRestaurant(selectedRestaurant.getName()).observe(getViewLifecycleOwner(),
                persistedRestaurant ->
                        mRestaurantViewModel.getDailyScheduleForRestaurant(persistedRestaurant.getName()).observe(getViewLifecycleOwner(),
                                dailySchedule -> {
                                    if (!dailySchedule.getToday().equals(today.toString())) {
                                        mRestaurantViewModel.deleteDailyScheduleForRestaurant(selectedRestaurant);
                                        Log.i(TAG, "manageDailySchedule: daily schedule is obsolete");
                                    }
                                }));
    }

    private void initAlertDialogOnUserHasAlreadyChoseRestaurant(Restaurant selectedRestaurant, User globalInterestedUser, List<User> interestedUsers) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("You have already chose a restaurant :");
        builder.setMessage("\" ".concat(globalInterestedUser.getSelectedRestaurant().getName()).concat(" \"").concat("\n \nWould you like to change?"));
        builder.setPositiveButton("Yes", (dialog, which) -> changeRestaurantForUser(globalInterestedUser, selectedRestaurant, interestedUsers));
        builder.setNegativeButton("No", null);
        builder.create().show();
    }


    private void changeRestaurantForUser(User user, Restaurant selectedRestaurant, List<User> interestedUsers) {
        mRestaurantViewModel.deleteUserInRestaurant(today, user.getSelectedRestaurant(), user);
        if (interestedUsers.isEmpty()) {
            Log.i(TAG, "changeRestaurantForUser: users empty");
            updateUsersWhenNoUserExistForThisRestaurant(today, selectedRestaurant, user);
        } else {
            Log.i(TAG, "changeRestaurantForUser: ");
            updateUsersWhenInterestedUsersExistForThisRestaurant(today, selectedRestaurant, user);

        }

        user.setSelectedRestaurant(selectedRestaurant);
        mRestaurantViewModel.addUserInGlobalList(user);
    }

    private void initRestaurantChoiceFabClickListener(User currentUser, Restaurant selectedRestaurant, List<User> interestedUsers) {

        Date finalToday = today;

        mRestaurantViewModel.getGlobalInterestedUser(currentUser).observe(getViewLifecycleOwner(), globalInterestedUser ->
                mBinding.chooseRestaurantFab.setOnClickListener(v ->
                        initAlertDialogOnUserHasAlreadyChoseRestaurant(selectedRestaurant, globalInterestedUser, interestedUsers)));

        if (interestedUsers.isEmpty()) {
            mBinding.chooseRestaurantFab.setOnClickListener(v ->
                    updateUsersWhenNoUserExistForThisRestaurant(finalToday, selectedRestaurant, currentUser));
        } else {
            mBinding.chooseRestaurantFab.setOnClickListener(v ->
                    updateUsersWhenInterestedUsersExistForThisRestaurant(finalToday, selectedRestaurant, currentUser));
        }
    }

    private void updateUsersWhenInterestedUsersExistForThisRestaurant(Date finalToday, Restaurant selectedRestaurant, User currentUser) {

        mRestaurantViewModel.getInterestedUsersForRestaurant(finalToday, selectedRestaurant.getName())
                .observe(getViewLifecycleOwner(), persistedUsers -> {

                    boolean isCurrentUserAlreadyExistForThisRestaurant = false;
                    for (User persistedUser : persistedUsers) {
                        if (currentUser.getEmail().equals(persistedUser.getEmail())) {
                            isCurrentUserAlreadyExistForThisRestaurant = true;
                            break;
                        }
                    }
                    if (!isCurrentUserAlreadyExistForThisRestaurant) {
                        mRestaurantViewModel.updateInterestedUsers(finalToday, selectedRestaurant.getName(), currentUser);
                        currentUser.setSelectedRestaurant(selectedRestaurant);
                        mRestaurantViewModel.addUserInGlobalList(currentUser);
                    }
                });
    }

    private void updateUsersWhenNoUserExistForThisRestaurant(Date finalToday, Restaurant selectedRestaurant, User currentUser) {

        mRestaurantViewModel.getCompletionState.observe(getViewLifecycleOwner(), completionState -> {

            if (completionState == RestaurantViewModel.CompletionState.RESTAURANT_IS_NOT_PERSISTED) {
                mRestaurantViewModel.addRestaurant(selectedRestaurant);
                Log.i(TAG, "updateUsersWhenNoUserExistForThisRestaurant: restaurant added");

            }

            if (completionState == RestaurantViewModel.CompletionState.RESTAURANT_ON_COMPLETE
                    || completionState == RestaurantViewModel.CompletionState.DAILY_SCHEDULE_IS_NOT_PERSISTED) {
                mRestaurantViewModel.addDailySchedule(finalToday, null, selectedRestaurant);
                Log.i(TAG, "updateUsersWhenNoUserExistForThisRestaurant: daily added");
            }

            if (completionState == RestaurantViewModel.CompletionState.DAILY_SCHEDULE_ON_COMPLETE
            || completionState == RestaurantViewModel.CompletionState.DAILY_SCHEDULE_IS_PERSISTED) {
                mRestaurantViewModel.updateInterestedUsers(finalToday, selectedRestaurant.getName(), currentUser);
                Log.i(TAG, "updateUsersWhenNoUserExistForThisRestaurant: user added");
            }


            mRestaurantViewModel.getInterestedUsersForRestaurant(finalToday, selectedRestaurant.getName()).observe(getViewLifecycleOwner(),
                    persistedUsers -> {
                        for (User persistedUser : persistedUsers) {
                            if (persistedUser.getEmail().equals(currentUser.getEmail())) {
                                persistedUser.setSelectedRestaurant(selectedRestaurant);
                                mRestaurantViewModel.addUserInGlobalList(persistedUser);
                                break;
                            }
                        }
                    });
        });
    }

    private void initRecyclerView() {
        mAdapter = new RecyclerViewAdapter(new ArrayList<>(), TAG);
        mBinding.recyclerViewRestaurantDetail.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerViewRestaurantDetail.setAdapter(mAdapter);
    }
}