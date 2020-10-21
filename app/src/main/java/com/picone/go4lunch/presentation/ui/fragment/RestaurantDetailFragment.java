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
import static com.picone.go4lunch.presentation.utils.ConstantParameter.REQUEST_CODE;
import static com.picone.go4lunch.presentation.utils.ManageStarUtil.manageStar;

public class RestaurantDetailFragment extends BaseFragment {

    public static final String TAG = RestaurantDetailFragment.class.getName();
    private FragmentRestaurantDetailBinding mBinding;
    private ColleagueRecyclerViewAdapter mAdapter;
    private boolean mCallPermission;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentRestaurantDetailBinding.inflate(inflater, container, false);
        showAppBars(false);
        setStatusBarTransparent(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        initView();
    }

    private void initButtons(Restaurant selectedRestaurant) {
        mBinding.checkIfSelectedDetailFab.setOnClickListener(v ->
                mRestaurantViewModel.addUserToRestaurant());

        mBinding.likeDetailImageButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(R.string.like_restaurant_question)
                    .setNegativeButton(R.string.no, null)
                    .setPositiveButton(R.string.yes, (dialog, which) -> mRestaurantViewModel.updateFanList())
                    .create()
                    .show();
        });

        mBinding.callNumberDetailImageButton.setOnClickListener(v -> {
            if (selectedRestaurant.getPhoneNumber() != null) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(getString(R.string.tel).concat(selectedRestaurant.getPhoneNumber().trim())));
                if (mCallPermission) {
                    startActivity(callIntent);
                } else getCallPermission();
            }
        });

        mBinding.webSiteDetailImageButton.setOnClickListener(v -> {
            if (selectedRestaurant.getWebsite() != null) {
                Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
                myWebLink.setData(Uri.parse(selectedRestaurant.getWebsite()));
                startActivity(myWebLink);
            }
        });
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

    //TODO setFabInactive if on userChosenRestaurant
    private void initView() {
        mRestaurantViewModel.isDataLoading.observe(getViewLifecycleOwner(), isDataLoading ->
                playLoadingAnimation(isDataLoading, mBinding.animationViewInclude.animationView));

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
                mBinding.foodStyleAndAddressDetailTextView.setText(restaurant.getAddress());
                mBinding.foodStyleAndAddressDetailTextView.setText(restaurant.getAddress());
                setLikeView(restaurant);
                setPhoto(restaurant);
            }
        });
    }

    private void setButtonColor(Restaurant restaurant) {
        if (restaurant.getPhoneNumber() == null)
            mBinding.callNumberDetailImageButton.setBackgroundColor(Color.LTGRAY);
        if (restaurant.getWebsite() == null)
            mBinding.webSiteDetailImageButton.setBackgroundColor(Color.LTGRAY);
        mRestaurantViewModel.getCurrentUser.observe(getViewLifecycleOwner(), user -> {
            if (restaurant.getFanList() != null && restaurant.getFanList().contains(user.getUid())) {
                mBinding.likeDetailImageButton.setBackgroundColor(Color.LTGRAY);
                mBinding.likeDetailImageButton.setOnClickListener(null);
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

    private void setLikeView(Restaurant restaurant) {
        int numberOfLike = 0;
        if (restaurant.getFanList() != null && !restaurant.getFanList().isEmpty())
            numberOfLike = restaurant.getFanList().size();
        manageStar(mBinding.opinionStarDetailImageView, numberOfLike);
    }

    private void initRecyclerView() {
        mAdapter = new ColleagueRecyclerViewAdapter(new ArrayList<>(), TAG);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerViewRestaurantDetail.setLayoutManager(linearLayoutManager);
        mBinding.recyclerViewRestaurantDetail.setAdapter(mAdapter);
    }
}