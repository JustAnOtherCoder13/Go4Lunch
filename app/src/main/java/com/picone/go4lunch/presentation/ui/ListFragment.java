package com.picone.go4lunch.presentation.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.FragmentListBinding;

public class ListFragment extends Fragment {

    private FragmentListBinding mBinding;

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentListBinding.inflate(inflater, container, false);
        mBinding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.map_view_button_bottom_nav:
                    goToMap();
                    break;
                case R.id.list_view_button_bottom_nav:
                case R.id.workmates_view_button_bottom_nav:
                    break;
            }
            return false;
        });
        return mBinding.getRoot();
    }
    private void goToMap(){
        NavHostFragment.findNavController(this).navigate(R.id.action_listFragment_to_mapsFragment);
    }

}