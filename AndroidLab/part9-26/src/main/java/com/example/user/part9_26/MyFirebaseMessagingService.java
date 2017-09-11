package com.example.user.part9_26;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String from=remoteMessage.getFrom();
        Map<String, String> data=remoteMessage.getData();
        String msg=data.get("msg");

        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        android.support.v4.app.NotificationCompat.Builder builder=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "one-channel";
            String channelName = "My Channel One";
            String channelDescription = "My Channel One Description";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDescription);

            manager.createNotificationChannel(channel);
            builder = new android.support.v4.app.NotificationCompat.Builder(this, channelId);

        } else {
            builder = new android.support.v4.app.NotificationCompat.Builder(this);
        }

        builder.setSmallIcon(android.R.drawable.ic_notification_overlay);
        builder.setContentTitle("Server Message");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentText(msg);
        builder.setAutoCancel(true);

        manager.notify(222, builder.build());


    }
}
