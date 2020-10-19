package com.picone.go4lunch.presentation.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.picone.go4lunch.R;
import com.picone.go4lunch.presentation.ui.main.MainActivity;

public class SendNotificationUtil extends Service {

    private static final int NOTIFICATION_ID = 007;
    private static final String NOTIFICATION_TAG = "FIREBASEOC";

    public static void sendVisualNotification(String messageBody, Context context) {

        // 1 - Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // 2 - Create a Style for the Notification
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(context.getResources().getString(R.string.app_name));
        inboxStyle.addLine(messageBody);

        // 3 - Create a Channel (Android 8)
        String channelId = "fcm_default_channel";

        // 4 - Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_logo_go4lunch)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentText(context.getResources().getString(R.string.app_name))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle);

        // 5 - Add the Notification to the Notification Manager and show it.
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // 6 - Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Message provenant de Firebase";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // 7 - Show notification
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
