package com.picone.go4lunch.presentation.ui.fragment;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.picone.go4lunch.databinding.FragmentRestaurantDetailBinding;
import com.picone.go4lunch.presentation.ui.fragment.adapters.ColleagueRecyclerViewAdapter;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;

import java.util.ArrayList;

import static com.picone.go4lunch.presentation.utils.ManageStarUtil.manageStar;

public class RestaurantDetailFragment extends BaseFragment {

    public static final String TAG = RestaurantDetailFragment.class.getName();
    private FragmentRestaurantDetailBinding mBinding;
    private ColleagueRecyclerViewAdapter mAdapter;

    //TODO make status bar transparent
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
        initView();
        initButtons();

        return mBinding.getRoot();
    }

    private void initButtons() {
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

        mBinding.callNumberDetailImageButton.setOnClickListener(v -> {
            //TODO call on click?
        });

        mBinding.webSiteDetailImageButton.setOnClickListener(v -> {
            //TODO link to website
        });
    }

    private void initView() {
        mRestaurantViewModel.isDataLoading.observe(getViewLifecycleOwner(), isDataLoading ->
                playLoadingAnimation(isDataLoading, mBinding.animationViewInclude.animationView));

        mRestaurantViewModel.getInterestedUsersForRestaurant.observe(getViewLifecycleOwner(), users ->
                mAdapter.updateUsers(users));

        mRestaurantViewModel.getSelectedRestaurant.observe(getViewLifecycleOwner(), restaurant -> {
            mBinding.restaurantNameDetailTextView.setText(restaurant.getName());
            mBinding.foodStyleAndAddressDetailTextView.setText(restaurant.getAddress());
            manageStar(mBinding.opinionStarDetailImageView, (int) restaurant.getAverageSatisfaction());
            mBinding.foodStyleAndAddressDetailTextView.setText(restaurant.getAddress());
            int numberOfLike = 0;
            if (restaurant.getFanList() != null && !restaurant.getFanList().isEmpty())
                numberOfLike = restaurant.getFanList().size();
            manageStar(mBinding.opinionStarDetailImageView, numberOfLike);
            Glide.with(mBinding.restaurantPhotoDetailImageView.getContext())
                    .load(restaurant.getRestaurantPhoto())
                    .centerCrop()
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super
                                Drawable> transition) {
                            mBinding.restaurantPhotoDetailImageView.setImageDrawable(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }

                    });
        });
    }

    private void initRecyclerView() {
        mAdapter = new ColleagueRecyclerViewAdapter(new ArrayList<>(), TAG);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerViewRestaurantDetail.setLayoutManager(linearLayoutManager);
        mBinding.recyclerViewRestaurantDetail.setAdapter(mAdapter);
    }
}