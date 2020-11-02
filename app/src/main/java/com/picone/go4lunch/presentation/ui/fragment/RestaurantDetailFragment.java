package com.picone.go4lunch.presentation.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.picone.core.domain.entity.restaurant.Restaurant;
import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.FragmentRestaurantDetailBinding;
import com.picone.go4lunch.presentation.ui.fragment.adapters.ColleagueRecyclerViewAdapter;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;

import java.util.ArrayList;

import static android.Manifest.permission.CALL_PHONE;
import static com.picone.go4lunch.presentation.utils.ConstantParameter.CURRENT_HOUR;
import static com.picone.go4lunch.presentation.utils.ConstantParameter.MAX_RESERVATION_HOUR;
import static com.picone.go4lunch.presentation.utils.ConstantParameter.REQUEST_CODE;
import static com.picone.go4lunch.presentation.utils.DailyScheduleHelper.getUserDailyScheduleOnToday;
import static com.picone.go4lunch.presentation.utils.ManageStarUtil.manageStar;

public class RestaurantDetailFragment extends BaseFragment {

    public static final String TAG = RestaurantDetailFragment.class.getName();
    private FragmentRestaurantDetailBinding mBinding;
    private ColleagueRecyclerViewAdapter mAdapter;
    private boolean mCallPermission;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentRestaurantDetailBinding.inflate(inflater, container, false);
        setAppBarVisibility(false);
        setStatusBarTransparency(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        initView();
    }

    private void initButtons(Restaurant selectedRestaurant) {
        setChooseRestaurantFabVisibility(selectedRestaurant);

        mBinding.chooseRestaurantFab.setOnClickListener(v ->
                mRestaurantViewModel.addUserToRestaurant());

        mBinding.likeDetailImageButton.setOnClickListener(v ->
                initLikeAlertDialog());

        mBinding.callNumberDetailImageButton.setOnClickListener(v ->
                initCallIntent(selectedRestaurant));

        mBinding.webSiteDetailImageButton.setOnClickListener(v ->
                initWebSiteIntent(selectedRestaurant));
    }

    private void getCallPermission() {
        if (ContextCompat.checkSelfPermission(this.requireContext(),
                CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            mCallPermission = true;
        } else {
            ActivityCompat.requestPermissions(this.requireActivity(),
                    new String[]{CALL_PHONE},
                    REQUEST_CODE);
        }
    }

    private void initRecyclerView() {
        mAdapter = new ColleagueRecyclerViewAdapter(new ArrayList<>(), TAG);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerViewRestaurantDetail.setLayoutManager(linearLayoutManager);
        mBinding.recyclerViewRestaurantDetail.setAdapter(mAdapter);
    }

    private void initView() {
        mRestaurantViewModel.isDataLoading.observe(getViewLifecycleOwner(), this::playLoadingAnimation);

        mRestaurantViewModel.getInterestedUsersForRestaurant.observe(getViewLifecycleOwner(), users ->
                mAdapter.updateUsers(users));

        mRestaurantViewModel.getLikeCounter.observe(getViewLifecycleOwner(), fanListSize ->
                manageStar(mBinding.opinionStarDetailImageView, fanListSize));

        mRestaurantViewModel.getSelectedRestaurant.observe(getViewLifecycleOwner(), restaurant -> {
            if (restaurant != null) {
                setButtonColor(restaurant);
                initButtons(restaurant);
                if (restaurant.getFanList() != null)
                    mRestaurantViewModel.setLikeCounter(restaurant.getFanList().size());
                else mRestaurantViewModel.setLikeCounter(0);
                mBinding.restaurantNameDetailTextView.setText(restaurant.getName());
                mBinding.addressDetailTextView.setText(restaurant.getAddress());
                initLike(restaurant);
                setPhoto(restaurant);
            }
        });
    }

    private void setButtonColor(Restaurant restaurant) {
        if (restaurant.getPhoneNumber() == null) {
            mBinding.callNumberDetailImageButton.setBackgroundColor(Color.LTGRAY);
            mBinding.webSiteDetailImageButton.setEnabled(false);
        }
        if (restaurant.getWebsite() == null) {
            mBinding.webSiteDetailImageButton.setBackgroundColor(Color.LTGRAY);
            mBinding.webSiteDetailImageButton.setEnabled(false);
        }
        mRestaurantViewModel.getCurrentUser.observe(getViewLifecycleOwner(), user -> {
            if (restaurant.getFanList() != null && restaurant.getFanList().contains(user.getUid())) {
                mBinding.likeDetailImageButton.setBackgroundColor(Color.LTGRAY);
                mBinding.likeDetailImageButton.setEnabled(false);
            }

        });
    }

    private void setPhoto(Restaurant restaurant) {
        Glide.with(mBinding.restaurantPhotoDetailImageView.getContext())
                .load(restaurant.getRestaurantPhoto())
                .centerCrop()
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mBinding.restaurantPhotoDetailImageView.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    private void initLike(Restaurant restaurant) {
        int numberOfLike = 0;
        if (restaurant.getFanList() != null && !restaurant.getFanList().isEmpty())
            numberOfLike = restaurant.getFanList().size();
        manageStar(mBinding.opinionStarDetailImageView, numberOfLike);
    }

    private void initWebSiteIntent(Restaurant selectedRestaurant) {
        if (selectedRestaurant.getWebsite() != null) {
            Intent myWebLink = new Intent(Intent.ACTION_VIEW);
            myWebLink.setData(Uri.parse(selectedRestaurant.getWebsite()));
            startActivity(myWebLink);
        }
    }

    private void initCallIntent(Restaurant selectedRestaurant) {
        if (selectedRestaurant.getPhoneNumber() != null) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse(getString(R.string.tel).concat(selectedRestaurant.getPhoneNumber().trim())));
            if (mCallPermission) {
                startActivity(callIntent);
            } else getCallPermission();
        }
    }

    private void initLikeAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.like_restaurant_question)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, (dialog, which) -> mRestaurantViewModel.updateFanList())
                .create()
                .show();
    }

    private void setChooseRestaurantFabVisibility(Restaurant selectedRestaurant) {
        if (mRestaurantViewModel.getCurrentUser.getValue() != null
                && getUserDailyScheduleOnToday(mRestaurantViewModel.getCurrentUser.getValue().getUserDailySchedules()) != null
                && getUserDailyScheduleOnToday(mRestaurantViewModel.getCurrentUser.getValue().getUserDailySchedules()).getRestaurantPlaceId()
                .equals(selectedRestaurant.getPlaceId())
                || CURRENT_HOUR >= MAX_RESERVATION_HOUR
                || selectedRestaurant.getOpeningHours().equals(getResources().getString(R.string.closed)))
            mBinding.chooseRestaurantFab.setVisibility(View.GONE);
    }
}