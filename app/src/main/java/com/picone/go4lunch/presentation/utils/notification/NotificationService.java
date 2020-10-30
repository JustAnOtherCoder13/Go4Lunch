package com.picone.go4lunch.presentation.utils.notification;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.picone.go4lunch.R;

import static com.picone.go4lunch.presentation.utils.notification.NotificationReceiver.setAlarm;

public class NotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0)
            setAlarm(this, remoteMessage.getData().get(getString(R.string.intent_extra_name)));
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("TAG", "Refreshed token: " + token);
    }
}
