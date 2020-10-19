package com.picone.go4lunch.presentation.utils;

import android.content.Context;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.picone.go4lunch.presentation.utils.SendNotificationUtil.sendVisualNotification;

public class NotificationService extends FirebaseMessagingService {

    private String message;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("TAG", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0)
            Log.d("TAG", "Message data payload: " + remoteMessage.getData());

        if (remoteMessage.getNotification() != null){
            Log.d("TAG", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            this.message=remoteMessage.getNotification().getBody();
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    public String getMessage (){return this.message;}

    @Override
    public void onNewToken(String token) {
        Log.d("TAG", "Refreshed token: " + token);
    }
}
