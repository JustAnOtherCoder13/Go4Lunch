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
import com.picone.go4lunch.databinding.FragmentMapsBinding;


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
        mBinding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.map_view_button_bottom_nav :
                case R.id.workmates_view_button_bottom_nav:
                    break;
                case R.id.list_view_button_bottom_nav :
                    goToList();
                    break;
            }
            return false;
        });
        return mBinding.getRoot();
    }
    private void goToList (){
        NavHostFragment.findNavController(this).navigate(R.id.action_mapsFragment_to_listFragment);
    }
}