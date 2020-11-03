package com.picone.go4lunch.presentation.utils;

import android.content.Context;
import android.icu.util.ValueIterator;
import android.widget.Toast;

public enum ErrorHandler {

    NO_ERROR(""),
    ON_ERROR("An error have occurred, please check your parameters and try again");


    public final String label;

    ErrorHandler(String label) {
        this.label = label;
    }
}
