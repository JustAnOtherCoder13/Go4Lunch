package com.picone.go4lunch.presentation.utils;

import android.view.View;

import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.picone.go4lunch.R;

import java.util.Objects;

import static androidx.fragment.app.FragmentManager.findFragment;

public class BottomNavigationUtil {

    public static void getBottomNavigation(BottomNavigationView v, View view) {

        int currentFragmentId = Objects.requireNonNull(NavHostFragment.findNavController(findFragment(view).getChildFragmentManager().getFragments().get(0)).getCurrentDestination()).getId();

        v.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.map_view_button_bottom_nav: {
                    if (currentFragmentId != R.id.mapsFragment)
                        NavHostFragment.findNavController(findFragment(view)).navigate(R.id.action_listFragment_to_mapsFragment);
                }
                break;

                case R.id.list_view_button_bottom_nav:
                case R.id.workmates_view_button_bottom_nav:
                    if (currentFragmentId != R.id.listFragment)
                        NavHostFragment.findNavController(findFragment(view)).navigate(R.id.action_mapsFragment_to_listFragment);
                    break;
            }
            return false;
        });
    }
}
