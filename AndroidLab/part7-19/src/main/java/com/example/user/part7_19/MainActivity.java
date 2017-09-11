package com.example.user.part7_19;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MainActivity extends AppCompatActivity{
    ListView listView;
    ArrayList<String> datas;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=(ListView)findViewById(R.id.lab1_list);
        datas=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);
        listView.setAdapter(adapter);

        IntentFilter ifilter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus=registerReceiver(null, ifilter);

        int status=batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging=status==BatteryManager.BATTERY_STATUS_CHARGING;

        if(isCharging){
            int chargePlug=batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge=chargePlug==BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge=chargePlug==BatteryManager.BATTERY_PLUGGED_AC;
            if(usbCharge){
                addListItem("Battery is USB Charging");
            }else if(acCharge){
                addListItem("Battery is AC Charging");
            }
        }else {
            addListItem("Battery State is not Charging");
        }

        int level=batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale=batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = (level / (float)scale) *100;
        addListItem("Current Battery : "+batteryPct+"%");

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 100);
        }

        registerReceiver(brOn, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(brOff, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_POWER_CONNECTED));
        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));

        
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                Toast toast = Toast.makeText(this, "no permission", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
    

    private void addListItem(String message){
        datas.add(message);
        adapter.notifyDataSetChanged();
    }

    BroadcastReceiver brOn=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            addListItem("screen on");
        }
    };

    BroadcastReceiver brOff=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            addListItem("screen off....");
        }
    };

    BroadcastReceiver batteryReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals(Intent.ACTION_POWER_CONNECTED)){
                addListItem("ON Connected....");
            }else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)){
                addListItem("ON DISCONNECED....");
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(brOn);
        unregisterReceiver(brOff);
        unregisterReceiver(batteryReceiver);
    }
}
