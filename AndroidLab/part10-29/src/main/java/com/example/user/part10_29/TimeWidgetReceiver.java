package com.example.user.part10_29;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Calendar;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class TimeWidgetReceiver extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getStringExtra("mode") != null){
            Calendar current=Calendar.getInstance();
            RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.mywidget);
            remoteViews.setTextViewText(R.id.textView, current.get(Calendar.HOUR_OF_DAY)+":"+current.get(Calendar.MINUTE));
            AppWidgetManager manager=AppWidgetManager.getInstance(context);
            manager.updateAppWidget(new ComponentName(context, TimeWidgetReceiver.class), remoteViews);
        }else {
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent aIntent=new Intent(context, TimeWidgetReceiver.class);

        PendingIntent pIntent=PendingIntent.getBroadcast(context, 0, aIntent, 0);

        am.cancel(pIntent);
        pIntent.cancel();
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Intent aIntent=new Intent(context, TimeWidgetReceiver.class);
        aIntent.putExtra("mode","time");
        PendingIntent pIntent=PendingIntent.getBroadcast(context, 0, aIntent, 0);

        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pIntent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.mywidget);
        Calendar cal=Calendar.getInstance();
        remoteViews.setTextViewText(R.id.textView, cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE));

        Intent intent=new Intent(context, MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.textView, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
