package com.example.user.part10_29;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MyCollectionWidgetReceiver extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent svcIntent =new Intent(context, MyRemoteViewsService.class);
        RemoteViews widget=new RemoteViews(context.getPackageName(), R.layout.my_collection_widget);
        widget.setRemoteAdapter(R.id.list, svcIntent);

        Intent clickIntent=new Intent(context, DetailActivity.class);
        PendingIntent clickPI=PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        widget.setPendingIntentTemplate(R.id.list, clickPI);

        appWidgetManager.updateAppWidget(appWidgetIds, widget);

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getStringExtra("mode") != null){
            AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
            int appWidgetIds[]=appWidgetManager.getAppWidgetIds(new ComponentName(context, MyCollectionWidgetReceiver.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list);
        }
    }
}
