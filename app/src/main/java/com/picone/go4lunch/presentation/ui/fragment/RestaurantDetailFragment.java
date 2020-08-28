package com.picone.go4lunch.presentation.ui.fragment;

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

        mRestaurantViewModel.getSelectedRestaurant().observe(getViewLifecycleOwner(), selectedRestaurant -> {
            mUserViewModel.getAllUsers().observe(getViewLifecycleOwner(), allUsers -> {
                User currentUser = null;
                User finalCurrentUser = currentUser;
                for (User user : allUsers) {
                    if (user.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                        currentUser = user;
                    }
                }

                mRestaurantViewModel.getGlobalInterestedUsers(currentUser).observe(getViewLifecycleOwner(), globalInterestedUser -> {
                    mBinding.chooseRestaurantFab.setOnClickListener(v -> {
                        //manage case in witch user want to change his choice
                        Toast.makeText(getContext(), "you have already chose a restaurant" + globalInterestedUser.getSelectedRestaurant().getName(), Toast.LENGTH_SHORT).show();
                    });
                });

                mRestaurantViewModel.isUserHasChoseRestaurant.observe(getViewLifecycleOwner(),
                        aBoolean -> {
                    if (!aBoolean) {
                        initFabClickListener(finalCurrentUser, selectedRestaurant, null);

                        mRestaurantViewModel.getInterestedUsersForRestaurant(finalToday, selectedRestaurant.getName()).observe(getViewLifecycleOwner(),
                                persistedUsers -> {
                                    initFabClickListener(finalCurrentUser, selectedRestaurant, persistedUsers);
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


    private void initFabClickListener(User currentUser, Restaurant selectedRestaurant, List<User> interestedUsers) {

        Date finalToday = today;

        if (interestedUsers == null) {
            mBinding.chooseRestaurantFab.setOnClickListener(v -> {

                mRestaurantViewModel.addRestaurant(selectedRestaurant);
                mRestaurantViewModel.getCompletionState().observe(getViewLifecycleOwner(), completionState -> {
                    switch (completionState) {
                        case RESTAURANT_ON_COMPLETE:
                            assert finalToday != null;
                            mRestaurantViewModel.addDailySchedule(finalToday, null, selectedRestaurant);
                            break;
                        case DAILY_SCHEDULE_ON_COMPLETE:
                            mRestaurantViewModel.updateInterestedUsers(finalToday, selectedRestaurant.getName(), currentUser);
                            break;
                        case PERSISTED_USERS_FOR_RESTAURANT_ON_COMPLETE:
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
                            break;
                    }
                });
            });
        } else {
            mBinding.chooseRestaurantFab.setOnClickListener(v ->

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