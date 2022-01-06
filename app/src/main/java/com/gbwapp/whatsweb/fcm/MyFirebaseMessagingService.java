package com.nadinegb.free.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nadinegb.free.MainActivity;
import com.nadinegb.free.R;

import java.util.Map;

/**
 * Created by Sagar on 14/04/20.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Map<String, String> params = remoteMessage.getData();

            if (AppPreference.getInstance(MyFirebaseMessagingService.this).isNotificationOn()) {
                sendNotification(params.get("title"), params.get("message"), params.get("url"));
            }
        }
    }

    private void sendNotification(String title, String messageBody, String url) {

        // insert data into database
        NotificationDbController notificationDbController = new NotificationDbController(MyFirebaseMessagingService.this);
        notificationDbController.insertData(title, messageBody, url);

        Intent intent;
        if (url != null && !url.isEmpty()) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
        } else {
            intent = new Intent(this, MainActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getResources().getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000})
                .setSound(defaultSoundUri)
                .setChannelId(getResources().getString(R.string.default_notification_channel_id))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.default_notification_channel_id), getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }

        assert notificationManager != null;
        notificationManager.notify(0, notificationBuilder.build());
    }
}
