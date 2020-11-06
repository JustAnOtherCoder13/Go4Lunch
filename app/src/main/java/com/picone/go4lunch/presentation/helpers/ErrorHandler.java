package com.picone.go4lunch.presentation.helpers;

//TODO do i have to translate this?
public enum ErrorHandler {

    NO_ERROR(""),
    ON_ERROR("An error have occurred, please check your parameters and try again");

    public final String label;

    ErrorHandler(String label) {
        this.label = label;
    }
}
