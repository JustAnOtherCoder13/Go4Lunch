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

    public final static String TAG = RestaurantListFragment.class.getName();

    private FragmentListBinding mBinding;
    private RestaurantListRecyclerViewAdapter mAdapter;

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
        mAdapter = new RestaurantListRecyclerViewAdapter(new ArrayList<>());
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerViewListFragment.setLayoutManager(linearLayoutManager);
        mBinding.recyclerViewListFragment.setAdapter(mAdapter);
        mRestaurantViewModel.getAllRestaurants().observe(getViewLifecycleOwner(),
                new Observer<List<Restaurant>>() {
                    @Override
                    public void onChanged(List<Restaurant> restaurants) {
                        mAdapter.updateRestaurants(restaurants);
                        mRestaurantViewModel.addRestaurant();

                    }
                });
    }

    public void configureOnClickRecyclerView() {
        RecyclerViewItemClickUtil.addTo(mBinding.recyclerViewListFragment, R.layout.fragment_list)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    NavController navController = Navigation.findNavController(v);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    navController.navigate(R.id.restaurantDetailFragment, bundle);
                });
    }
}