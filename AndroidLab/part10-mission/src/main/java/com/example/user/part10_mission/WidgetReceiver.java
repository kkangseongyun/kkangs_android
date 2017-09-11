package com.example.user.part10_mission;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class WidgetReceiver extends AppWidgetProvider{
    RequestQueue queue;
    @Override
    public void onReceive(final Context context, Intent intent) {
        if(intent.getStringExtra("mode") != null){

            Log.d("kkang","onReceive...............");
            final RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            StringRequest currentRequest=new StringRequest(Request.Method.POST, "http://api.openweathermap.org/data/2.5/weather?q=seoul&mode=xml&units=metric&appid=db441b5cd16577f716641f657592c172", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{

                        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder=factory.newDocumentBuilder();
                        Document doc= builder.parse(new InputSource(new StringReader(response)));

                        Element tempElement=(Element)(doc.getElementsByTagName("temperature").item(0));
                        String temperature=tempElement.getAttribute("value");

                        remoteViews.setTextViewText(R.id.lab2_text, temperature);

                        Element weatherElement=(Element)(doc.getElementsByTagName("weather").item(0));
                        String symbol=weatherElement.getAttribute("icon");

                        ImageRequest imageRequest=new ImageRequest("http://openweathermap.org/img/w/" + symbol + ".png", new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                remoteViews.setImageViewBitmap(R.id.lab2_image, response);
                                AppWidgetManager manager=AppWidgetManager.getInstance(context);
                                manager.updateAppWidget(new ComponentName(context, WidgetReceiver.class), remoteViews);
                                Log.d("kkang","update ok~~~~~~~~~~~~~~");
                            }
                        }, 0, 0, ImageView.ScaleType.CENTER_CROP,null, new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });

                        queue.add(imageRequest);
                    }catch (Exception e){

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue= Volley.newRequestQueue(context);
            queue.add(currentRequest);



        }
        super.onReceive(context, intent);
    }



    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Intent aIntent=new Intent(context, WidgetReceiver.class);
        aIntent.putExtra("mode","data");
        PendingIntent pIntent=PendingIntent.getBroadcast(context, 11, aIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pIntent);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Intent aIntent=new Intent(context, WidgetReceiver.class);
        PendingIntent pIntent=PendingIntent.getBroadcast(context, 11, aIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pIntent);
    }


}
