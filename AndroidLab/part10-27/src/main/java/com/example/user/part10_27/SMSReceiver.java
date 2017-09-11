package com.example.user.part10_27;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle=intent.getExtras();
        Object[] pdus=(Object[])bundle.get("pdus");
        SmsMessage[] messages=new SmsMessage[pdus.length];
        for(int i=0; i<pdus.length; i++){
            messages[i]=SmsMessage.createFromPdu((byte[])pdus[i]);
            try{
                String message=new String(messages[i].getMessageBody());
                String phoneNumber=messages[i].getOriginatingAddress();

                NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder=null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String channelId = "one-channel";
                    String channelName = "My Channel One";
                    String channelDescription = "My Channel One Description";
                    NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription(channelDescription);

                    manager.createNotificationChannel(channel);
                    builder = new NotificationCompat.Builder(context, channelId);

                } else {
                    builder = new NotificationCompat.Builder(context);
                }
                builder.setSmallIcon(android.R.drawable.ic_notification_overlay);
                builder.setContentTitle("New SMS Message");
                builder.setContentText(message);
                builder.setAutoCancel(true);
                Notification noti=builder.build();
                manager.notify(111, noti);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
