package com.picone.go4lunch.presentation.utils.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.picone.go4lunch.R;

import static com.picone.core.data.ConstantParameter.NOTIFICATION_ID;
import static com.picone.core.data.ConstantParameter.NOTIFICATION_TAG;

class NotificationHelper {

    static void createNotification(Context context, String message) {
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, context.getResources().getString(R.string.chanel_id))
                        .setSmallIcon(R.drawable.ic_logo_go4lunch)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(context.getString(R.string.app_name))
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(context.getResources().getString(R.string.chanel_id),
                    context.getResources().getString(R.string.chanel_name), importance);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }
        assert notificationManager != null;
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }
}