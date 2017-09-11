package com.example.user.part9_24;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */


public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> datas;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.lab1_listview);

        datas = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas);
        listView.setAdapter(adapter);

        //add~~~~~~~~~~~~~
        TelephonyManager telManager=(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        telManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE|PhoneStateListener.LISTEN_SERVICE_STATE);

        datas.add("countryIos:"+telManager.getNetworkCountryIso());
        datas.add("operatorName:"+telManager.getNetworkOperatorName());
        if(telManager.getNetworkType()==TelephonyManager.NETWORK_TYPE_LTE){
            datas.add("networkType:LTE");
        }else if(telManager.getNetworkType()==TelephonyManager.NETWORK_TYPE_HSDPA){
            datas.add("networkType:3G");
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)== PackageManager.PERMISSION_GRANTED){
            datas.add("PhoneNumber:"+telManager.getLine1Number());
        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 100);
        }

        checkNetwork();

        checkWifi();

        IntentFilter wifiFilter=new IntentFilter();
        wifiFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        wifiFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        wifiFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        registerReceiver(wifiReceiver, wifiFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReceiver);
    }

    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            //add~~~~~~~~~~~~~~~
            switch (serviceState.getState()){
                case ServiceState.STATE_EMERGENCY_ONLY:
                    datas.add("onServiceStateChanged STATE_EMERGENCY_ONLY");
                    break;
                case ServiceState.STATE_IN_SERVICE:
                    datas.add("onServiceStateChanged STATE_IN_SERVICE");
                    break;
                case ServiceState.STATE_OUT_OF_SERVICE:
                    datas.add("onServiceStateChanged STATE_OUT_OF_SERVICE");
                    break;
                case ServiceState.STATE_POWER_OFF:
                    datas.add("onServiceStateChanged STATE_POWER_OFF");
                    break;
                default:
                    datas.add("onServiceStateChanged Unknown");
                    break;
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            //add~~~~~~~~~~~~~~
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    datas.add("onCallStateChanged : CALL_STATE_IDLE : "+incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    datas.add("onCallStateChanged : CALL_STATE_RINGING : "+incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    datas.add("onCallStateChanged : CALL_STATE_OFFHOOK : "+incomingNumber);
                    break;
            }
            adapter.notifyDataSetChanged();
        }

    };

    private void checkNetwork() {
        //add~~~~~~~~~~~~~~~
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null){
            if(networkInfo.getType()==ConnectivityManager.TYPE_WIFI){
                datas.add("Network Info : Online - "+networkInfo.getTypeName());
            }else if (networkInfo.getType()==ConnectivityManager.TYPE_MOBILE){
                datas.add("Network Info : Online - "+networkInfo.getTypeName());
            }
        }else {
            datas.add("Network Info : Offline");
        }
        adapter.notifyDataSetChanged();
    }

    private void checkWifi(){
        //add~~~~~~~~~~~~~~~~
        WifiManager wifiManager=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            datas.add("WifiManager : wifi disabled");
            if(wifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLED){
                wifiManager.setWifiEnabled(true);
            }
        }else {
            datas.add("WifiManager : wifi enabled");
        }
        adapter.notifyDataSetChanged();
    }

    BroadcastReceiver wifiReceiver=new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //add~~~~~~~~~~~~~~~~~~~~
            if(intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
                int state=intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
                if(state==WifiManager.WIFI_STATE_ENABLED){
                    datas.add("WIFI_STATE_CHANGED_ACTION : enable");
                }else {
                    datas.add("WIFI_STATE_CHANGED_ACTION : disable");
                }
            }else if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
                NetworkInfo networkInfo=intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                WifiManager wifiManager=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo=wifiManager.getConnectionInfo();
                String ssid=wifiInfo.getSSID();
                if(networkInfo.getState()==NetworkInfo.State.CONNECTED){
                    datas.add("NETWORK_STATE_CHANGED_ACTION : connected..."+ssid);
                }else if(networkInfo.getState()==NetworkInfo.State.DISCONNECTED){
                    datas.add("NETWORK_STATE_CHANGED_ACTION : disconnected..."+ssid);
                }
            }else if(intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)){
                datas.add("RSSI_CHANGED_ACTION");
            }
            adapter.notifyDataSetChanged();
        }
    };


}
