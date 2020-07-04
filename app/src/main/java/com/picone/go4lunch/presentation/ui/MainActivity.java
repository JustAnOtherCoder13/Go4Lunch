package com.picone.go4lunch.presentation.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.picone.go4lunch.R;
import com.picone.go4lunch.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }


}