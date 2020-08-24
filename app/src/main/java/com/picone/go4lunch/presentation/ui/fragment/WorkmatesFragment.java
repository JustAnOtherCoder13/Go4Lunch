package com.picone.go4lunch.presentation.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.picone.core.domain.entity.User;
import com.picone.go4lunch.databinding.FragmentWorkmatesBinding;
import com.picone.go4lunch.presentation.ui.main.BaseFragment;
import com.picone.go4lunch.presentation.utils.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesFragment extends BaseFragment {

    private FragmentWorkmatesBinding mBinding;
    private RecyclerViewAdapter mAdapter;
    private List<User> mUsers = new ArrayList<>();
    public static final String TAG = WorkmatesFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentWorkmatesBinding.inflate(getLayoutInflater());
        initRecyclerView();
        showAppBars(true);
        return mBinding.getRoot();
    }

    private void initRecyclerView() {
        mAdapter = new RecyclerViewAdapter(mUsers, TAG);
        mBinding.recyclerViewWorkmatesFragment.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.recyclerViewWorkmatesFragment.setAdapter(mAdapter);
        mUserViewModel.getAllUsers().observe(getViewLifecycleOwner(), users -> {
            mAdapter.updateUsers(users);
        });

    }
}