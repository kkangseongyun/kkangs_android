package com.example.user.part7_mission;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class NotiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("kkang","11111111111111111");
        NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent aIntent=new Intent(context, MainActivity.class);
        PendingIntent pIntent=PendingIntent.getActivity(context, 10, aIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent bIntent=new Intent(context, CancelReceiver.class);
        PendingIntent cancelIntent=PendingIntent.getBroadcast(context, 20, bIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "one-channel";
            String channelName = "My Channel One";
            String channelDescription = "My Channel One Description";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDescription);

            manager.createNotificationChannel(channel);
            builder = new android.support.v4.app.NotificationCompat.Builder(context, channelId);

        } else {
            builder = new NotificationCompat.Builder(context);
        }
        builder.setSmallIcon(R.drawable.ic_alarm);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("예정된 알람");
        builder.setContentText(intent.getStringExtra("time"));
        builder.setContentIntent(pIntent);
        builder.addAction(R.drawable.ic_alarm, "Cancel", cancelIntent);
        builder.setAutoCancel(true);
        builder.setOngoing(false);

        manager.notify(222, builder.build());
    }
}
