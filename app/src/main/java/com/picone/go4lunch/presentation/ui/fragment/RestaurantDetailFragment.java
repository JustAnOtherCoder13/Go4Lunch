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

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.picone.core.domain.entity.Restaurant;
import com.picone.core.domain.entity.User;
import com.picone.go4lunch.databinding.FragmentRestaurantDetailBinding;
import com.picone.go4lunch.presentation.utils.RecyclerViewAdapter;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;

import java.util.ArrayList;

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

    private void initView(ViewGroup container) {
        User user = mUserViewModel.getCurrentUser().getValue();

        if (getArguments() != null) {
            mRestaurantViewModel.selectRestaurant(getArguments().getInt("position"));
        } else if (user != null && user.getSelectedRestaurant() != null) {
            mRestaurantViewModel.selectRestaurant(user);
        } else {
            Snackbar.make(container, "You haven't chose a restaurant, make your choice to get information.", BaseTransientBottomBar.LENGTH_LONG).show();
        }
        mRestaurantViewModel.getRestaurant().observe(getViewLifecycleOwner(), restaurant -> {
            mBinding.restaurantNameDetailTextView.setText(restaurant.getName());
            mBinding.foodStyleAndAddressDetailTextView.setText(restaurant.getFoodType()
                    .concat(" - ").concat(restaurant.getAddress()));
            initFabClickListener(restaurant);
        });
    }

    private void initFabClickListener(Restaurant restaurant) {
        mBinding.checkIfSelectedDetailFab.setOnClickListener(v -> {
            Log.i(TAG, "initFabClickListener: ");
                }
        );
    }

    private void initRecyclerView() {
        mAdapter = new RecyclerViewAdapter(new ArrayList<>(), TAG);
        mBinding.recyclerViewRestaurantDetail.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerViewRestaurantDetail.setAdapter(mAdapter);
    }
}