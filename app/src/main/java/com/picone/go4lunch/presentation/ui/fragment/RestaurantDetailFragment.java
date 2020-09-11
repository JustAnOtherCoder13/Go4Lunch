package com.picone.go4lunch.presentation.ui.fragment;

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
        mBinding.checkIfSelectedDetailFab.setOnClickListener(v ->
                mInterestedUserViewModel.updateCurrentUserChosenRestaurant(mRestaurantViewModel.selectedRestaurant.getValue()));
        return mBinding.getRoot();
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerViewRestaurantDetail.setLayoutManager(linearLayoutManager);
        mBinding.recyclerViewRestaurantDetail.setAdapter(mAdapter);
    }


}