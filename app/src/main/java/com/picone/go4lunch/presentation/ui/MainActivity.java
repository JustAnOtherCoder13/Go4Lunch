package com.picone.go4lunch.presentation.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setUpNavigation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); }
        return super.onOptionsItemSelected(item);
    }

    public void setUpNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(mBinding.bottomNavigation, navController);
        NavigationUI.setupWithNavController(mBinding.toolbar, navController);
        setUpBottomNavAndToolbar(navController);
    }

    private void setUpBottomNavAndToolbar(NavController navController) {
        mBinding.toolbar.setTitle(" ");
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() != R.id.authenticationFragment) {
                mBinding.toolbar.setVisibility(View.VISIBLE);
                mBinding.bottomNavigation.setVisibility(View.VISIBLE);
                setSupportActionBar(mBinding.toolbar);
                Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

            } else {
                mBinding.toolbar.setVisibility(View.GONE);
                mBinding.bottomNavigation.setVisibility(View.GONE);
            }
        });
    }
}