package com.example.user.part7_20;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class Lab20_3Activity extends AppCompatActivity implements TabHost.OnTabChangeListener, View.OnClickListener{

    TabHost host;
    ListView serviceListView;
    ListView appListView;
    ListView pickListView;

    ArrayList<HashMap<String, String>> appsDatas;
    ArrayList<HashMap<String, String>> serviceDatas;

    SimpleAdapter appsAdapter;
    SimpleAdapter serviceAdapter;
    SimpleAdapter pickAdapter;

    Button alarmBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab20_3);

        host=(TabHost)findViewById(R.id.lab3_host);
        serviceListView=(ListView)findViewById(R.id.lab3_service);
        appListView=(ListView)findViewById(R.id.lab3_apps);
        pickListView=(ListView)findViewById(R.id.lab3_pick);
        alarmBtn=(Button)findViewById(R.id.lab3_btn);

        host.setup();
        host.setOnTabChangedListener(this);
        alarmBtn.setOnClickListener(this);

        TabHost.TabSpec spec=host.newTabSpec("tab1");
        spec.setIndicator("service");
        spec.setContent(R.id.lab3_service);
        host.addTab(spec);

        spec=host.newTabSpec("tab2");
        spec.setIndicator("apps");
        spec.setContent(R.id.lab3_apps);
        host.addTab(spec);

        spec=host.newTabSpec("tab3");
        spec.setIndicator("action : PICK");
        spec.setContent(R.id.lab3_pick);
        host.addTab(spec);

        registerReceiver(receiver, new IntentFilter("com.example.ACTION_TO_ACTIVITY"));
    }

    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast toast=Toast.makeText(Lab20_3Activity.this, intent.getStringExtra("message"), Toast.LENGTH_SHORT);
            toast.show();

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View v) {
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent=new Intent(this, Lab4Service.class);
        PendingIntent pIntent=PendingIntent.getService(this, 50, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.set(AlarmManager.RTC, System.currentTimeMillis()+10000, pIntent);
    }

    @Override
    public void onTabChanged(String tabId) {
        if(tabId.equals("tab1")){
            ActivityManager am=(ActivityManager)getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> list=am.getRunningServices(100);
            serviceDatas=new ArrayList<>();
            for(ActivityManager.RunningServiceInfo info : list){
                HashMap<String,String> map=new HashMap<>();
                map.put("name", info.service.getClassName());
                map.put("package", info.service.getPackageName());
                serviceDatas.add(map);
            }
            serviceAdapter=new SimpleAdapter(this, serviceDatas, android.R.layout.simple_list_item_2,
                    new String[]{"name","package"}, new int[]{android.R.id.text1, android.R.id.text2});
            serviceListView.setAdapter(serviceAdapter);
        }else if(tabId.equals("tab2")){
            PackageManager pm=getPackageManager();
            List<ApplicationInfo> list=pm.getInstalledApplications(PackageManager.GET_META_DATA);
            appsDatas=new ArrayList<>();
            for(ApplicationInfo info : list){
                HashMap<String,String> map=new HashMap<>();
                map.put("label", info.loadLabel(pm).toString());
                map.put("package", info.packageName);
                appsDatas.add(map);
            }
            appsAdapter=new SimpleAdapter(this, appsDatas, android.R.layout.simple_list_item_2,
                    new String[]{"label","package"}, new int[]{android.R.id.text1, android.R.id.text2});
            appListView.setAdapter(appsAdapter);
        }else if(tabId.equals("tab3")){
            PackageManager pm=getPackageManager();
            List<ResolveInfo> activities=pm.queryIntentActivities(new Intent(Intent.ACTION_PICK), 0);
            ArrayList<HashMap<String, String>> list=new ArrayList<>();
            for(ResolveInfo info : activities){
                HashMap<String, String> map=new HashMap<>();
                map.put("label", info.loadLabel(pm).toString());
                map.put("package", info.activityInfo.applicationInfo.packageName);
                list.add(map);
            }
            pickAdapter=new SimpleAdapter(this, list, android.R.layout.simple_list_item_2,
                    new String[]{"label","package"}, new int[]{android.R.id.text1, android.R.id.text2});
            pickListView.setAdapter(pickAdapter);
        }
    }

}

