package com.thd.notes.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.thd.notes.R;
import com.thd.notes.activity.MainActivity;
import com.thd.notes.model.Note;

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
        Note note = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        Intent intent1 = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
        Notification myNotification = new NotificationCompat.Builder(context)
                .setContentTitle(note.getNoteTitle())
                .setContentText(note.getBrief())
                .setTicker("Notification!")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_color_lens_24dp)
                .build();
        manager.notify(id, myNotification);
    }
}
