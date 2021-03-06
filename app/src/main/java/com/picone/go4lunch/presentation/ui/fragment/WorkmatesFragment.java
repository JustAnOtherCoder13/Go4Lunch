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
import com.picone.go4lunch.presentation.helpers.RecyclerViewItemClickUtil;
import com.picone.go4lunch.presentation.ui.fragment.adapters.ColleagueRecyclerViewAdapter;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.picone.core.utils.FindInListUtil.getUserDailyScheduleOnToday;

public class WorkmatesFragment extends BaseFragment {

    public static final String TAG = WorkmatesFragment.class.getName();
    private FragmentWorkmatesBinding mBinding;
    private List<User> mUsers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentWorkmatesBinding.inflate(getLayoutInflater());
        setAppBarVisibility(true);
        setStatusBarTransparency(false);
        setPageTitle(R.string.available_workmates);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        configureOnClickRecyclerView();
        mRestaurantViewModel.getSelectedRestaurant.observe(getViewLifecycleOwner(), restaurant -> {
            if (restaurant != null)
                Navigation.findNavController(view).navigate(R.id.restaurantDetailFragment);
        });
    }

    private void initRecyclerView() {
        ColleagueRecyclerViewAdapter adapter = new ColleagueRecyclerViewAdapter(mUsers, TAG);
        mBinding.recyclerViewWorkmatesFragment.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerViewWorkmatesFragment.setAdapter(adapter);
        mUserViewModel.getAllUsers().observe(getViewLifecycleOwner(), adapter::updateUsers);
    }

    private void configureOnClickRecyclerView() {
        RecyclerViewItemClickUtil.addTo(mBinding.recyclerViewWorkmatesFragment, R.layout.fragment_restaurant_list)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    if (!Objects.requireNonNull(mUserViewModel.getAllUsers().getValue()).isEmpty()
                            && mUserViewModel.getAllUsers().getValue().get(position).getUserDailySchedules() != null
                            && getUserDailyScheduleOnToday(mUserViewModel.getAllUsers().getValue().get(position).getUserDailySchedules()) != null) {

                            mRestaurantViewModel.setInterestedUsersForRestaurant
                                (getUserDailyScheduleOnToday
                                        (mUserViewModel.getAllUsers().getValue().get(position).getUserDailySchedules())
                                        .getRestaurantPlaceId(),mRestaurantViewModel.getAllRestaurants.getValue());
                    }
                });
    }
}