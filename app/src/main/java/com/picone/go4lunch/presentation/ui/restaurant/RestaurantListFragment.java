package com.picone.go4lunch.presentation.ui.restaurant;

import android.os.Bundle;
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

import com.picone.core.domain.entity.Restaurant;
import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.FragmentListBinding;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;
import com.picone.go4lunch.presentation.utils.RecyclerViewItemClickUtil;

import java.util.ArrayList;
import java.util.List;


public class RestaurantListFragment extends BaseFragment {

    private FragmentListBinding mBinding;
    private RestaurantListRecyclerViewAdapter mAdapter;

    private List<Restaurant> mRestaurants = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentListBinding.inflate(inflater, container, false);
        initRecyclerView();
        showAppBars(true);
        configureOnClickRecyclerView();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initRecyclerView() {
        mAdapter = new RestaurantListRecyclerViewAdapter(mRestaurants);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerViewListFragment.setLayoutManager(linearLayoutManager);
        mBinding.recyclerViewListFragment.setAdapter(mAdapter);
        mRestaurantViewModel.getAllRestaurants().observe(getViewLifecycleOwner(),
                restaurants -> mAdapter.updateRestaurants(restaurants));
    }
    public void configureOnClickRecyclerView(){
        RecyclerViewItemClickUtil.addTo(mBinding.recyclerViewListFragment, R.layout.fragment_list)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    NavController navController = Navigation.findNavController(v);
                    mRestaurantViewModel.getAllRestaurants().observe(getViewLifecycleOwner(),
                            restaurants ->RestaurantDetailFragment.newInstance(restaurants.get(position)));
                    navController.navigate(R.id.restaurantDetailFragment);
                });
    }
}