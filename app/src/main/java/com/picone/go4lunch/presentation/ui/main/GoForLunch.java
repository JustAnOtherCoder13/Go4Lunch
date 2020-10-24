package com.picone.go4lunch.presentation.ui.main;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.picone.go4lunch.presentation.utils.LocaleHelper;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class GoForLunch extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.setLocale(base));
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleHelper.setLocale(this);
    }
}
