package com.picone.go4lunch.presentation.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.ActivityMainBinding;

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
        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.authenticationFragment,R.id.mapsFragment,R.id.listFragment).build();
        NavigationUI.setupWithNavController(mBinding.bottomNavigation, navController);
        NavigationUI.setupWithNavController(mBinding.toolbar, navController, appBarConfiguration);
    }

    void setBottomNavAndToolbarVisibility(Boolean bool) {

            if (bool) {
                mBinding.toolbar.setVisibility(View.VISIBLE);
                mBinding.bottomNavigation.setVisibility(View.VISIBLE);
            }else {
                mBinding.toolbar.setVisibility(View.GONE);
                mBinding.bottomNavigation.setVisibility(View.GONE);
            }

    }
}