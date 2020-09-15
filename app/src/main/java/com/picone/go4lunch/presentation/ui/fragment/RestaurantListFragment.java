package com.picone.go4lunch.presentation.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.picone.core.domain.entity.User;
import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.FragmentListBinding;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;
import com.picone.go4lunch.presentation.ui.fragment.adapters.RestaurantListRecyclerViewAdapter;
import com.picone.go4lunch.presentation.ui.utils.RecyclerViewItemClickUtil;

import java.util.ArrayList;
import java.util.List;


public class RestaurantListFragment extends BaseFragment {

    private FragmentListBinding mBinding;
    private RestaurantListRecyclerViewAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentListBinding.inflate(inflater, container, false);
        initRecyclerView();
        showAppBars(true);
        configureOnClickRecyclerView();
        return mBinding.getRoot();
    }

    @SuppressWarnings("ConstantConditions")
    //mAuth.getCurrentUser().getEmail() can't be null cause needed to enter the app
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRestaurantViewModel.getCurrentUserForEmail(mAuth.getCurrentUser().getEmail());
    }

    private void initRecyclerView() {
        mAdapter = new RestaurantListRecyclerViewAdapter(new ArrayList<>());
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerViewListFragment.setLayoutManager(linearLayoutManager);
        mBinding.recyclerViewListFragment.setAdapter(mAdapter);
        mAdapter.updateRestaurants(mRestaurantViewModel.getAllRestaurants());
    }
    public void configureOnClickRecyclerView() {
        RecyclerViewItemClickUtil.addTo(mBinding.recyclerViewListFragment, R.layout.fragment_list)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    mRestaurantViewModel.setSelectedRestaurant(position);
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.restaurantDetailFragment);
                });
    }
}