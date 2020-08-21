package com.picone.go4lunch.presentation.ui.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.go4lunch.databinding.FragmentRestaurantDetailBinding;
import com.picone.go4lunch.presentation.ui.colleague.ColleagueRecyclerViewAdapter;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;

import java.util.ArrayList;

public class RestaurantDetailFragment extends BaseFragment {

    public static final String TAG = RestaurantDetailFragment.class.getName();

    private FragmentRestaurantDetailBinding mBinding;
    private ColleagueRecyclerViewAdapter mAdapter;

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

    private void initView() {
        mRestaurantViewModel.selectRestaurant(getArguments().getInt("position"));
        mRestaurantViewModel.getRestaurant().observe(getViewLifecycleOwner(), restaurant -> {
            mBinding.restaurantNameDetailTextView.setText(restaurant.getName());
            mBinding.foodStyleAndAddressDetailTextView.setText(restaurant.getFoodType()
                    .concat(" - ").concat(restaurant.getAddress()));
            initFabClickListener(restaurant);
        });
    }

    private void initFabClickListener(Restaurant restaurant) {
        mBinding.checkIfSelectedDetailFab.setOnClickListener(v ->
                mUserViewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> {
                    for (User user : users) {
                        if (user.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                            mRestaurantViewModel.addInterestedUser(user);
                            mRestaurantViewModel.getInterestedColleague(restaurant).observe(getViewLifecycleOwner(),
                                    users1 -> mAdapter.updateUsers(users1));
                        }
                    }
                }));
    }

    private void initRecyclerView() {
        mAdapter = new ColleagueRecyclerViewAdapter(new ArrayList<>(), TAG);
        mBinding.recyclerViewRestaurantDetail.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerViewRestaurantDetail.setAdapter(mAdapter);
    }
}