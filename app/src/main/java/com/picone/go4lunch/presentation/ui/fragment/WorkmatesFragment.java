package com.picone.go4lunch.presentation.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.picone.core.domain.entity.user.User;
import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.FragmentWorkmatesBinding;
import com.picone.go4lunch.presentation.ui.fragment.adapters.ColleagueRecyclerViewAdapter;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;
import com.picone.go4lunch.presentation.utils.RecyclerViewItemClickUtil;

import java.util.ArrayList;
import java.util.List;

import static com.picone.go4lunch.presentation.utils.DailyScheduleHelper.getUserDailyScheduleOnToday;

public class WorkmatesFragment extends BaseFragment {

    public static final String TAG = WorkmatesFragment.class.getName();
    private FragmentWorkmatesBinding mBinding;
    private List<User> mUsers = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentWorkmatesBinding.inflate(getLayoutInflater());
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
        ColleagueRecyclerViewAdapter adapter = new ColleagueRecyclerViewAdapter(mUsers, TAG);
        mBinding.recyclerViewWorkmatesFragment.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerViewWorkmatesFragment.setAdapter(adapter);
        mUserViewModel.getAllUsers.observe(getViewLifecycleOwner(), adapter::updateUsers);
    }

    //getAllUsers can't be null cause set in main
    @SuppressWarnings("ConstantConditions")
    public void configureOnClickRecyclerView() {
        RecyclerViewItemClickUtil.addTo(mBinding.recyclerViewWorkmatesFragment, R.layout.fragment_restaurant_list)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    if (!mUserViewModel.getAllUsers.getValue().isEmpty() && mUserViewModel.getAllUsers.getValue().get(position).getUserDailySchedules() != null && getUserDailyScheduleOnToday(mUserViewModel.getAllUsers.getValue().get(position).getUserDailySchedules())!=null) {
                        mRestaurantViewModel.setInterestedUsersForRestaurant(getUserDailyScheduleOnToday(mUserViewModel.getAllUsers.getValue().get(position).getUserDailySchedules()).getRestaurantPlaceId());
                    }
                });
    }
}