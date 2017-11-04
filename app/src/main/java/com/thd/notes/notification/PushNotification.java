package com.thd.notes.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Tran Hai Dang on 11/4/2017.
 * Email : tranhaidang2320@gmail.com
 */

public class PushNotification extends BroadcastReceiver {
    public static String NOTIFICATION = "notification";
    public static String NOTIFICATION_ID = "notification-id";
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        manager.notify(id, notification);
    }
}
