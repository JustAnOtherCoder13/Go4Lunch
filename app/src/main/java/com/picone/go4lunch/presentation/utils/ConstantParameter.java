package com.picone.go4lunch.presentation.utils;

import com.picone.core.domain.entity.user.SettingValues;
import com.picone.go4lunch.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ConstantParameter {

    public static final String TODAY = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(Calendar.getInstance().getTime());

    public static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    public static final int NOTIFICATION_ID = 7;
    public static final String NOTIFICATION_TAG = "GO4LUNCH";
    public static final String CLOSED = "Closed";
    public static final int RC_SIGN_IN = 13250;
    public static final int REQUEST_CODE = 13700;


    public static final int ALARM_HOUR=12;
    public static final int ALARM_MINUTE= 0;

    public static final int MAPS_CAMERA_ZOOM = 16;

    public static final SettingValues SETTING_START_VALUE = new SettingValues("En",true);
}
