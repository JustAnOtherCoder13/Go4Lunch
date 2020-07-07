package com.picone.go4lunch.presentation.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.picone.go4lunch.databinding.FragmentMapsBinding;

import static com.picone.go4lunch.presentation.utils.BottomNavigationUtil.getBottomNavigation;


public class MapsFragment extends Fragment {

    private FragmentMapsBinding mBinding;

    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentMapsBinding.inflate(inflater, container, false);
        getBottomNavigation(mBinding.bottomNavigation, container);
        return mBinding.getRoot();
    }
}