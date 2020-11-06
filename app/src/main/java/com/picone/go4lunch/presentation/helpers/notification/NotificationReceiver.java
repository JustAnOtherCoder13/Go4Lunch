package com.picone.go4lunch.presentation.helpers.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.picone.go4lunch.R;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null)
            NotificationHelper.createNotification(context, intent.getExtras().getString(context.getResources().getString(R.string.intent_extra_name)));
    }
}
