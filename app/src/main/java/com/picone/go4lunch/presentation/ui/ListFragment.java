package com.picone.go4lunch.presentation.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.picone.go4lunch.databinding.FragmentListBinding;
import com.picone.go4lunch.presentation.viewModels.UserViewModel;


public class ListFragment extends BaseFragment {

    private FragmentListBinding mBinding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("test", "onCreate: "+mUsers.size());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentListBinding.inflate(inflater, container, false);
        initRecyclerView();
        showAppBars(true);
        return mBinding.getRoot();
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.recyclerViewListFragment.setLayoutManager(linearLayoutManager);
        mBinding.recyclerViewListFragment.setAdapter(mAdapter);
    }
}