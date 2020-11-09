package com.picone.go4lunch.presentation.helpers;

import com.picone.go4lunch.R;

public enum ErrorHandler {

    NO_ERROR(0),
    ON_ERROR(R.string.on_error);

    public final int label;

    ErrorHandler(int label) {
        this.label = label;
    }
}
