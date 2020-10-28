package com.picone.go4lunch.presentation.utils;

import android.content.Context;

import android.content.SharedPreferences;

import android.content.res.Configuration;

import android.content.res.Resources;

import android.preference.PreferenceManager;
import android.util.Log;


import java.util.Locale;

import static com.picone.go4lunch.presentation.utils.ConstantParameter.SELECTED_LANGUAGE;


public class LocaleHelper {

    public static Context onAttach(Context context) {

        String lang = getPersistedData(context, Locale.getDefault().getLanguage());

        return setNewLocale(context, lang);

    }

    public static Context onAttach(Context context, String defaultLanguage) {

        String lang = getPersistedData(context, defaultLanguage);

        return setNewLocale(context, lang);
    }


    public static String getLanguage(Context context) {

        return getPersistedData(context, Locale.getDefault().getLanguage());

    }

    public static Context setNewLocale(Context context, String language) {
        Log.i("TAG", "setNewLocale: "+language);
        persist(context, language);

        return updateResourcesLegacy(context, language);

    }

    public static Context setLocale(Context context){
        return
        setNewLocale(context,getLanguage(context));
    }

    private static String getPersistedData(Context context, String defaultLanguage) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage);

    }

    private static void persist(Context context, String language) {

        Log.i("TAG", "persist: "+language);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(SELECTED_LANGUAGE, language);

        editor.apply();

    }

    private static Context updateResourcesLegacy(Context context, String language) {

        Locale locale = new Locale(language);

        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();

        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;

    }

}
