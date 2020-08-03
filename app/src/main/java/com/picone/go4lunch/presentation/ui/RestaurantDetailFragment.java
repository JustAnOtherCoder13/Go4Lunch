package com.picone.go4lunch.presentation.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.picone.core_.domain.entity.User;
import com.picone.go4lunch.databinding.FragmentRestaurantDetailBinding;

public class RestaurantDetailFragment extends BaseFragment {

    private FragmentRestaurantDetailBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModels();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentRestaurantDetailBinding.inflate(inflater, container, false);
        setUserRestaurant();
        initRecyclerView();
        showAppBars(false);
        return mBinding.getRoot();
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerViewRestaurantDetail.setLayoutManager(linearLayoutManager);
        mBinding.recyclerViewRestaurantDetail.setAdapter(mAdapter);
    }

    private void setUserRestaurant() {
        for (int i = 0; i < mUsers.size(); i++) {
            User user = mUsers.get(i);
            user.setSelectedRestaurant(mRestaurants.get(i));
        }
    }
}