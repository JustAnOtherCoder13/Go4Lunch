package com.picone.go4lunch.presentation.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.picone.go4lunch.databinding.FragmentAuthenticationBinding;

public class AuthenticationFragment extends Fragment {

    private FragmentAuthenticationBinding mBinding;

    public static AuthenticationFragment newInstance() {
        return new AuthenticationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAuthenticationBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }
}