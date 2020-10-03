package com.picone.go4lunch.presentation.ui.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.picone.go4lunch.databinding.FragmentRestaurantDetailBinding;
import com.picone.go4lunch.presentation.ui.fragment.adapters.ColleagueRecyclerViewAdapter;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;

import java.util.ArrayList;

import static com.picone.go4lunch.presentation.ui.utils.ManageStarUtil.manageStar;

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
        initRecyclerView();
        showAppBars(false);

        mRestaurantViewModel.isDataLoading.observe(getViewLifecycleOwner(), isDataLoading ->
                playLoadingAnimation(isDataLoading, mBinding.animationViewInclude.animationView));

        mRestaurantViewModel.getInterestedUsersForRestaurant.observe(getViewLifecycleOwner(), users ->
                mAdapter.updateUsers(users));

        mRestaurantViewModel.getSelectedRestaurant.observe(getViewLifecycleOwner(), restaurant -> {
            mBinding.restaurantNameDetailTextView.setText(restaurant.getName());
            mBinding.foodStyleAndAddressDetailTextView.setText(restaurant.getAddress());
            manageStar(mBinding.opinionStarDetailImageView, (int) restaurant.getAverageSatisfaction());
            mBinding.foodStyleAndAddressDetailTextView.setText(restaurant.getFoodType()
                    .concat(" restaurant")
                    .concat(" - ").concat(restaurant.getAddress()));
            int numberOfLike = 0;
            if (restaurant.getFanList() != null && !restaurant.getFanList().isEmpty())
                numberOfLike = restaurant.getFanList().size();
            manageStar(mBinding.opinionStarDetailImageView, numberOfLike);
        });

        mBinding.checkIfSelectedDetailFab.setOnClickListener(v ->
                mRestaurantViewModel.addUserToRestaurant());

        mBinding.likeDetailImageButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Like this restaurant ?")
                    .setNegativeButton("No",null)
                    .setPositiveButton("Yes", (dialog, which) -> mRestaurantViewModel.updateFanList())
                    .create()
                    .show();
        });

        return mBinding.getRoot();
    }

    private void initRecyclerView() {
        mAdapter = new ColleagueRecyclerViewAdapter(new ArrayList<>(), TAG);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerViewRestaurantDetail.setLayoutManager(linearLayoutManager);
        mBinding.recyclerViewRestaurantDetail.setAdapter(mAdapter);
    }
}