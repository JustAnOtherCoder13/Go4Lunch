package com.picone.go4lunch.presentation.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.FragmentRestaurantListBinding;
import com.picone.go4lunch.presentation.ui.fragment.adapters.RestaurantListRecyclerViewAdapter;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;
import com.picone.go4lunch.presentation.utils.RecyclerViewItemClickUtil;

import java.util.ArrayList;


public class RestaurantListFragment extends BaseFragment {

    private FragmentRestaurantListBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentRestaurantListBinding.inflate(inflater, container, false);
        showAppBars(true);
        setStatusBarTransparent(false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        configureOnClickRecyclerView();
        mRestaurantViewModel.resetSelectedRestaurant();
        mRestaurantViewModel.getSelectedRestaurant.observe(getViewLifecycleOwner(), restaurant -> {
            if (restaurant != null)
                Navigation.findNavController(view).navigate(R.id.restaurantDetailFragment);
        });
    }

    private void initRecyclerView() {
        RestaurantListRecyclerViewAdapter adapter = new RestaurantListRecyclerViewAdapter(new ArrayList<>());
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerViewListFragment.setLayoutManager(linearLayoutManager);
        mBinding.recyclerViewListFragment.setAdapter(adapter);
        mRestaurantViewModel.getAllRestaurants.observe(getViewLifecycleOwner(), adapter::updateRestaurants);
    }

    public void configureOnClickRecyclerView() {
        RecyclerViewItemClickUtil.addTo(mBinding.recyclerViewListFragment, R.layout.fragment_restaurant_list)
                .setOnItemClickListener((recyclerView, position, v) ->
                        mRestaurantViewModel.initSelectedRestaurant(position));
    }
}