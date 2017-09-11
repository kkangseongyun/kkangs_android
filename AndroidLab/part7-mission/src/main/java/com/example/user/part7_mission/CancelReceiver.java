package com.example.user.part7_mission;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class CancelReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent bIntent=new Intent("com.example.part7_mission.ACTION_ALARM");
        PendingIntent alarmIntent=PendingIntent.getActivity(context, 100, bIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(alarmIntent);
        alarmIntent.cancel();

        NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(222);
    }
}
