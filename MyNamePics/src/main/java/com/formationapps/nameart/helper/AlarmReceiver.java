package com.formationapps.nameart.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.formationapps.nameart.NameArtMenu;
import com.formationapps.nameart.R;
import com.formationapps.nameart.activity.TemplateActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            final int notificationid = 1;
            Intent notificationIntent = new Intent(context, NameArtMenu.class);
            notificationIntent.putExtra("notificationid", notificationid);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(NameArtMenu.class);
            stackBuilder.addNextIntent(notificationIntent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            String weekDay;
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

            Calendar calendar = Calendar.getInstance();
            weekDay = dayFormat.format(calendar.getTime());

            String title = context.getResources().getString(R.string.app_name);
            String msg = "GOOD MORNING....Enjoy " + weekDay + "!. You can wish to some one special using this app.";
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            String url = TemplateActivity.getLastTemplateUrl(context);
            String s = url.replace("/", "");
            final File file = new File(context.getFilesDir(), s);
            Bitmap bigBitmap = null;
            if (file.isFile()) {
                try {
                    bigBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                } catch (final Exception e) {
                    //Log.e(TAG, e.getMessage());
                    bigBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_72);
                }
            } else {
                bigBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_72);
            }
            Notification notification;

            RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
            contentView.setTextViewText(R.id.custom_noti_title, context.getString(R.string.app_name));
            contentView.setTextViewText(R.id.custom_noti_subtext, msg);
            contentView.setImageViewBitmap(R.id.custom_noti_bigpic, bigBitmap);
            contentView.setImageViewResource(R.id.custom_noti_icon, R.mipmap.icon_72);
            builder.setCustomBigContentView(contentView);
            builder.setSmallIcon(R.mipmap.icon_72);
            builder.setAutoCancel(true);
            contentView.setOnClickPendingIntent(R.id.cus_noti_id, pendingIntent);
        /*try {
            String url= TemplateEditorActivity.getLastTemplateUrl(context);
            String s=url.replace("/","");
            final File file=new File(context.getFilesDir(),s);
            Bitmap bigBitmap=null;
            if(file.isFile()){
                try {
                    bigBitmap =BitmapFactory.decodeFile(file.getAbsolutePath());
                } catch (final Exception e) {
                    //Log.e(TAG, e.getMessage());
                    bigBitmap=BitmapFactory.decodeResource(context.getResources(),R.mipmap.icon_72);
                }
            }else{
                bigBitmap=BitmapFactory.decodeResource(context.getResources(),R.mipmap.icon_72);
            }
            NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle()
                    .bigPicture(bigBitmap)
                    .setSummaryText(msg);
            notification = builder.setContentTitle(title)
                    .setContentText(msg)
                    .setTicker("Its "+weekDay+" !")
                    .setSmallIcon(R.mipmap.icon_72)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.icon_72))
                    .setSound(sound)
                    .setStyle(style)
                    .setContentIntent(pendingIntent).build();
        }catch (Exception e){
            if(BuildConfig.DEBUG){
                Toast.makeText(context,"ar:"+e.getMessage(),Toast.LENGTH_LONG).show();
            }
           Bitmap bigBitmap=BitmapFactory.decodeResource(context.getResources(),R.mipmap.icon_72);
            NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle()
                .bigPicture(bigBitmap)
                .setSummaryText(msg);
            notification = builder.setContentTitle(title)
                    .setContentText(msg)
                    .setTicker("Its "+weekDay+" !")
                    .setSmallIcon(R.mipmap.icon_72)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.icon_72))
                    .setSound(sound)
                    .setStyle(style)
                    .setContentIntent(pendingIntent).build();
        }*/
            notification = builder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
            notification.defaults |= Notification.DEFAULT_VIBRATE;//Vibration
            notification.defaults |= Notification.DEFAULT_SOUND;// Sound

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationid, notification);
        } catch (Exception e) {

        }
    }

}
