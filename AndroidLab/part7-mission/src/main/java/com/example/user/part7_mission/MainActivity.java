package com.example.user.part7_mission;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    FloatingActionButton fab;
    TextView timeView;
    Switch aSwitch;

    SharedPreferences prefs;

    AlarmManager alarm;
    PendingIntent preIntent;
    Intent aIntent;
    PendingIntent alarmIntent;

    boolean isClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab=(FloatingActionButton)findViewById(R.id.mission1_fab);
        timeView=(TextView)findViewById(R.id.mission1_time);
        aSwitch=(Switch)findViewById(R.id.mission1_switch);

        alarm=(AlarmManager)getSystemService(ALARM_SERVICE);

        aIntent=new Intent(this, NotiReceiver.class);
        preIntent=PendingIntent.getBroadcast(this, 50, aIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent bIntent=new Intent("com.example.part7_mission.ACTION_ALARM");
        alarmIntent=PendingIntent.getActivity(this, 100, bIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        prefs= PreferenceManager.getDefaultSharedPreferences(this);

        int hour=prefs.getInt("hour",-1);
        int minute=prefs.getInt("minute",-1);
        boolean enable=prefs.getBoolean("enable", false);

        if(hour>-1 && minute>-1){
            Calendar calendar=Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            SimpleDateFormat sd=new SimpleDateFormat("HH:mm");
            timeView.setText(sd.format(calendar.getTime()));
        }

        if(enable){
            aSwitch.setChecked(true);
        }
        fab.setOnClickListener(this);
        aSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        final Calendar c=Calendar.getInstance();
        int currentHour=c.get(Calendar.HOUR_OF_DAY);
        int currentMinute=c.get(Calendar.MINUTE);

        final TimePickerDialog timeDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                isClick=true;

                Calendar calendar=Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SharedPreferences.Editor editor=prefs.edit();
                editor.putInt("hour", hourOfDay);
                editor.putInt("minute", minute);
                editor.putBoolean("enable", true);
                editor.commit();

                SimpleDateFormat sd=new SimpleDateFormat("HH:mm");
                timeView.setText(sd.format(calendar.getTime()));
                aSwitch.setChecked(true);

                aIntent.putExtra("time", sd.format(calendar.getTime()));
                alarm.set(AlarmManager.RTC, calendar.getTimeInMillis()-120000, preIntent);
                alarm.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmIntent);
                isClick=false;
            }
        }, currentHour, currentMinute, false);
        timeDialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor=prefs.edit();
        editor.putBoolean("enable", isChecked);
        editor.commit();

        if(isChecked){
            if(!isClick){
                int hour=prefs.getInt("hour",-1);
                int minute=prefs.getInt("minute",-1);
                if(hour > -1 && minute >-1){
                    Calendar calendar=Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);

                    alarm.set(AlarmManager.RTC, calendar.getTimeInMillis() - 60000, preIntent);
                    alarm.set(AlarmManager.RTC, calendar.getTimeInMillis(), alarmIntent);
                }
            }
        }else {
            alarm.cancel(preIntent);
            alarm.cancel(alarmIntent);
            preIntent.cancel();
            alarmIntent.cancel();
            editor.putBoolean("enable", false);

        }
    }
}
















