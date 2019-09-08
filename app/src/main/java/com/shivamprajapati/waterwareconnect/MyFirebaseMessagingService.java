package com.shivamprajapati.waterwareconnect;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;

import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    int currentNotificationID=0;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if(remoteMessage.getData().size()>0){
            Map<String,String> payload=remoteMessage.getData();
            showNotification(payload);
        }
    }

    private void showNotification(Map<String, String> payload) {

        Intent intent=new Intent(this,MainActivity.class);

        Log.i("TAG","notification");


        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);



        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_notifications_black_24dp))
                .setContentTitle(payload.get("title"))
                .setContentText(payload.get("body"))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(payload.get("body")))
                .setLights(Notification.DEFAULT_LIGHTS,2000,2000);
        builder.setContentIntent(pendingIntent);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel=new NotificationChannel("id","name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.enableLights(true);
            builder.setChannelId("id");
            notificationManager.createNotificationChannel(channel);
            Notification notification=builder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND;
            currentNotificationID++;
            int notificationId = currentNotificationID;
            if (notificationId == Integer.MAX_VALUE - 1)
                notificationId = 0;

            notificationManager.notify(notificationId, notification);
        }else {
            Notification notification = builder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND;
            currentNotificationID++;
            int notificationId = currentNotificationID;
            if (notificationId == Integer.MAX_VALUE - 1)
                notificationId = 0;

            notificationManager.notify(notificationId, notification);
        }


    }


}
