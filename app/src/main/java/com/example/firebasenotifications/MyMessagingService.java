package com.example.firebasenotifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class MyMessagingService  extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
//        showNotification(message.getNotification().getTitle(),
//                message.getNotification().getBody());

        Map<String,String> dataMap = new HashMap<>();
        String noteType="";
        if (message.getData().size() > 0) {
            noteType = message.getData().get("type");
            dataMap = message.getData();

        }

        switch (noteType)
        {
            case "BIGTEXT":
                bigTextNotification(dataMap);
                break;
            case "BIGPIC":
                bigPicNotification(dataMap);
                break;
            case "ACTIONS":
                notificationActions(dataMap);
                break;
//            case "DIRECTREPLY":
//                directReply(dataMap);
//                break;
//            case "INBOX":
//                inboxTypeNotification(dataMap);
//                break;
//            case "MESSAGE":
//                messageTypeNotification(dataMap);
//                break;
        }

    }

//    public void showNotification(String title, String message){
//
//        Intent intent = new Intent(MyMessagingService.this,MainActivity2.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(MyMessagingService.this,
//                1,intent,PendingIntent.FLAG_ONE_SHOT);
//
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
//                "MyNotifications")
//                .setContentTitle(title)
//                .setSmallIcon(R.drawable.ic_stat_ic_notification)
//                .setAutoCancel(true)
//                .setContentText(message)
//                .setContentIntent(pendingIntent);
//
//
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
//        managerCompat.notify(999,builder.build());
//
//    }

    public void bigTextNotification(Map<String,String> dataMap)
    {
        String title = dataMap.get("title");
        String message = dataMap.get("message");
        String channelId = "MyNotifications";
        String channelName = "MyNotifications";
        NotificationCompat.Builder builder1= new NotificationCompat.Builder(this, channelId);

       NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText(message);
        style.setSummaryText(title);

        builder1.setContentTitle(title)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setColor(Color.BLUE)
                .setStyle(style);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(999,builder1.build());

    }


    public void bigPicNotification(Map<String,String> dataMap)
    {
        String title = dataMap.get("title");
        String message = dataMap.get("message");
        String imageUrl = dataMap.get("imageUrl");
        try {
            String channelId = "MyNotifications";
            String channelName = "MyNotifications";
            NotificationCompat.Builder builder2= new NotificationCompat.Builder(this, channelId);

            NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
            style.setBigContentTitle(title);
            style.setSummaryText(message);
            style.bigPicture(Glide.with(MyMessagingService.this).asBitmap()
                    .load(imageUrl).submit().get());

            builder2.setContentTitle(title)
                    .setContentText(message)
                    .setColor(Color.GREEN)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_stat_ic_notification)
                    .setStyle(style);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(999,builder2.build());

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void notificationActions(Map<String,String> dataMap)
    {
        String title = dataMap.get("title");
        String message = dataMap.get("message");

        String channelId = "MyNotifications";
        String channelName = "MyNotifications";
        NotificationCompat.Builder builder3 = new NotificationCompat.Builder(this, channelId);


        Intent intent1 = new Intent(MyMessagingService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyMessagingService.this,
                0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent cancelIntent = new Intent(getBaseContext(), NotificationReceiver.class);
        cancelIntent.putExtra("ID",0);

        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(getBaseContext(),
                0, cancelIntent, 0);


        builder3.setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(Color.BLUE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(android.R.drawable.ic_menu_view, "VIEW", pendingIntent)
                .addAction(android.R.drawable.ic_delete, "DISMISS", cancelPendingIntent)
                .build();
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(999,builder3.build());

    }


}
