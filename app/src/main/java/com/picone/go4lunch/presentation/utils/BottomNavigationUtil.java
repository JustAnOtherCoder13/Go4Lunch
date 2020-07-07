package com.picone.go4lunch.presentation.utils;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.picone.go4lunch.R;
import com.picone.go4lunch.presentation.ui.MapsFragment;

public class BottomNavigationUtil {

    public static void getBottomNavigation(BottomNavigationView v, Fragment fragment){
        v.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.map_view_button_bottom_nav:
                    NavHostFragment.findNavController(fragment).navigate(R.id.action_listFragment_to_mapsFragment);
                    break;

                case R.id.list_view_button_bottom_nav:
                case R.id.workmates_view_button_bottom_nav:
                    if (fragment.getClass() == MapsFragment.class) {
                        NavHostFragment.findNavController(fragment).navigate(R.id.action_mapsFragment_to_listFragment);
                    }
                    break;

            }
            return false;
        });
    }

}
