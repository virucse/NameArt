package com.webapi.model;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;

/**
 * Created by ROSHAN on 12/27/2017.
 */

public class NotificationIntentService extends IntentService {

    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";

    public NotificationIntentService() {

        super(NotificationIntentService.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {

                setUpNotification();
            }
            if (ACTION_DELETE.equals(action)) {
                processDeleteNotification(intent);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processDeleteNotification(Intent intent) {
        // Log something?
    }

    private void processStartNotification() {
        NotificationCompat.Builder builder;
        if (nd != null) {
            if (nd.notification.length > 0) {
                for (int i = 0; i < nd.notification.length; i++) {
                    String username = nd.notification[i].username;
                    String post = nd.notification[i].type;
                    if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                        builder = new NotificationCompat.Builder(this);
                    } else {
                        builder = new NotificationCompat.Builder(this, "notification_builder");
                    }
                    builder.setSmallIcon(R.mipmap.icon_72);
                    builder.setContentTitle(username);
                    if (post.equals("comment")) {
                        builder.setContentText("" + username + " commented on your post");
                    }
                    if (post.equals("post")) {
                        builder.setContentText("" + username + " posted on Name Art");
                    }
                    builder.setAutoCancel(true);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this,
                            NOTIFICATION_ID,
                            new Intent(this, NameArtMenu.class),
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this));

                    final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(NOTIFICATION_ID, builder.build());
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent("RestartBroadcast");
        sendBroadcast(broadcastIntent);
    }
    NotificationDetail nd = null;
    private void setUpNotification(){

    }


}
