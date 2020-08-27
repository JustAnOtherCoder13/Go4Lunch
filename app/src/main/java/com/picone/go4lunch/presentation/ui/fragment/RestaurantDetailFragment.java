package com.picone.go4lunch.presentation.ui.fragment;

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
    public final static int MY_DAY_OF_MONTH = CALENDAR.get(Calendar.DAY_OF_MONTH);
    public final static int MY_MONTH = CALENDAR.get(Calendar.MONTH);
    public final static int MY_YEAR = CALENDAR.get(Calendar.YEAR);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        if (!mRestaurantViewModel.getSelectedRestaurant().hasObservers()) {
            if (getArguments() != null) {
                mRestaurantViewModel.setSelectedRestaurant(getArguments().getInt("position"));
            }
        }

        Date today = null;
        try {
            today = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(MY_DAY_OF_MONTH + "/" + MY_MONTH + "/" + MY_YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Date finalToday = today;
        mRestaurantViewModel.getSelectedRestaurant().observe(getViewLifecycleOwner(), selectedRestaurant -> {
            Log.i(TAG, "initView: selected restaurantObserve");
            mUserViewModel.getAllUsers().observe(getViewLifecycleOwner(), allUsers -> {
                User currentUser = null;
                for (User user : allUsers) {
                    if (user.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                        currentUser = user;
                    }
                }
                initFabClickListener(currentUser, selectedRestaurant, null);
                Log.i(TAG, "initView: getCurrentUser ");
                User finalCurrentUser = currentUser;
                mRestaurantViewModel.getInterestedUsersForRestaurant(finalToday, selectedRestaurant.getName()).observe(getViewLifecycleOwner(),
                        persistedUsers -> {
                            Log.i(TAG, "initView: persistedUsersObserve");
                            initFabClickListener(finalCurrentUser, selectedRestaurant, persistedUsers);
                            mAdapter.updateUsers(persistedUsers);
                        });

            });
            mBinding.restaurantNameDetailTextView.setText(selectedRestaurant.getName());
            mBinding.foodStyleAndAddressDetailTextView.setText(selectedRestaurant.getFoodType()
                    .concat(" - ").concat(selectedRestaurant.getAddress()));
        });

    }


    private void initFabClickListener(User currentUser, Restaurant selectedRestaurant, List<User> interestedUsers) {
        Date today = null;
        try {
            today = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).parse(MY_DAY_OF_MONTH + "/" + MY_MONTH + "/" + MY_YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date finalToday = today;
        if (interestedUsers == null) {
            mBinding.checkIfSelectedDetailFab.setOnClickListener(v -> {

                mRestaurantViewModel.addRestaurant(selectedRestaurant);
                mRestaurantViewModel.getCompletionState().observe(getViewLifecycleOwner(), completionState -> {
                    Log.i(TAG, "onChanged: " + completionState);
                    switch (completionState) {
                        case RESTAURANT_ON_COMPLETE:
                            Log.i(TAG, "initFabClickListener:  restaurant added");
                            assert finalToday != null;
                            mRestaurantViewModel.addDailySchedule(finalToday, null, selectedRestaurant);
                            break;
                        case DAILY_SCHEDULE_ON_COMPLETE:
                            Log.i(TAG, "initFabClickListener:  dailySchedule added");
                            mRestaurantViewModel.updateInterestedUsers(finalToday, selectedRestaurant.getName(), currentUser);
                            break;
                        case USERS_ON_COMPLETE:
                            Log.i(TAG, "initFabClickListener: user added");
                            mRestaurantViewModel.getInterestedUsersForRestaurant(finalToday,selectedRestaurant.getName()).observe(getViewLifecycleOwner(),
                                    persistedUsers -> {
                                        mAdapter.updateUsers(persistedUsers);
                                        Log.i(TAG, "initFabClickListener: update users");
                                    });
                            break;
                    }
                });
            });
        } else {
            mBinding.checkIfSelectedDetailFab.setOnClickListener(v ->

                    mRestaurantViewModel.getInterestedUsersForRestaurant(finalToday, selectedRestaurant.getName())
                            .observe(getViewLifecycleOwner(), persistedUsers -> {
                                Log.i(TAG, "initFabClickListener: persistedUsersObserve" + persistedUsers);

                                boolean mybol = false;
                                for (User persistedUser : persistedUsers) {
                                    if (currentUser.getEmail().equals(persistedUser.getEmail())) {
                                        mybol = true;
                                        Log.i(TAG, "initFabClickListener: interestedUsersFound");
                                        break;
                                    }
                                }
                                if (!mybol) {
                                    Log.i(TAG, "initFabClickListener: interestedUsersChangeAdded");
                                    mRestaurantViewModel.updateInterestedUsers(finalToday, selectedRestaurant.getName(), currentUser);
                                }
                            }));
        }

    }

    private void initRecyclerView() {
        mAdapter = new RecyclerViewAdapter(new ArrayList<>(), TAG);
        mBinding.recyclerViewRestaurantDetail.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerViewRestaurantDetail.setAdapter(mAdapter);
    }
}