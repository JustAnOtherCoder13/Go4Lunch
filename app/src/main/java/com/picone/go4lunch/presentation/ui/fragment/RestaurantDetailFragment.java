package com.picone.go4lunch.presentation.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.go4lunch.databinding.FragmentRestaurantDetailBinding;
import com.picone.go4lunch.presentation.utils.RecyclerViewAdapter;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDetailFragment extends BaseFragment {

    public static final String TAG = RestaurantDetailFragment.class.getName();

    private FragmentRestaurantDetailBinding mBinding;
    private RecyclerViewAdapter mAdapter;

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
        initView(container);
        return mBinding.getRoot();
    }

    @SuppressWarnings("ConstantConditions")
    //suppress warning is safe cause CurrentUser must be initialized to enter app
    private void initView(ViewGroup container) {

        mUserViewModel.getAllUsers().observe(getViewLifecycleOwner(), allUsers -> {
            for (User user : allUsers) {
                if (user.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                    populateRecyclerViewOnRestaurantSelected(user);
                    populateRecyclerViewOnMyLunchClicked(user,container);
                }
            }
        });

        mRestaurantViewModel.getRestaurant().observe(getViewLifecycleOwner(), restaurant -> {
            mBinding.restaurantNameDetailTextView.setText(restaurant.getName());
            mBinding.foodStyleAndAddressDetailTextView.setText(restaurant.getFoodType()
                    .concat(" - ").concat(restaurant.getAddress()));

        });
    }
//TODO problem when click on restaurant always go "chez jaja"
    private void populateRecyclerViewOnMyLunchClicked(User user,ViewGroup container) {
        mUserViewModel.getInterestedColleague().observe(getViewLifecycleOwner(), users -> {

            for (User interestedUser : users) {
                if (user.getEmail().equals(interestedUser.getEmail()) && interestedUser.getSelectedRestaurant() != null) {
                    mRestaurantViewModel.selectRestaurant(interestedUser);
                    getInterestedUsers(interestedUser.getSelectedRestaurant(), users);
                    break;
                }
            }
                //Snackbar.make(container, "You haven't chose a restaurant, make your choice to get information.", BaseTransientBottomBar.LENGTH_LONG).show();
        });
    }

    private void populateRecyclerViewOnRestaurantSelected(User user) {
        if (getArguments() != null) {
            mRestaurantViewModel.selectRestaurant(getArguments().getInt("position"));
            mRestaurantViewModel.getRestaurant().observe(getViewLifecycleOwner(), restaurant -> {
                mUserViewModel.getInterestedColleague().observe(getViewLifecycleOwner(), interestedUsers -> {
                    initFabClickListener(user, restaurant, interestedUsers);
                    getInterestedUsers(restaurant, interestedUsers);
                });
            });
        }
    }

    private void getInterestedUsers(Restaurant restaurant, List<User> users) {
        List<User> interestedUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getSelectedRestaurant().getName().equals(restaurant.getName())) {
                interestedUsers.add(user);
            }
        }
        mAdapter.updateUsers(interestedUsers);
    }

    private void initFabClickListener(User user, Restaurant restaurant, List<User> interestedUsers) {
        mBinding.checkIfSelectedDetailFab.setOnClickListener(v -> {

            if (interestedUsers.isEmpty()) {
                user.setSelectedRestaurant(restaurant);
                mUserViewModel.addInterestedUser(user);
                mUserViewModel.getInterestedColleague().observe(getViewLifecycleOwner(),
                        users -> mAdapter.updateUsers(users));
            }
            User userToAdd = user;
            for (User interestedUser : interestedUsers) {
                if (interestedUser.getEmail().equals(user.getEmail())) {
                    userToAdd = null;
                    break;
                }
            }
            if (userToAdd != null) {
                user.setSelectedRestaurant(restaurant);
                mUserViewModel.addInterestedUser(userToAdd);
            }
            mUserViewModel.getInterestedColleague().observe(getViewLifecycleOwner(),
                    interestedUsers1 -> mAdapter.updateUsers(interestedUsers1));

        });
    }


    private void initRecyclerView() {
        mAdapter = new RecyclerViewAdapter(new ArrayList<>(), TAG);
        mBinding.recyclerViewRestaurantDetail.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerViewRestaurantDetail.setAdapter(mAdapter);
    }
}