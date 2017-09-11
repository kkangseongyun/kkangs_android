package com.example.user.part10_27;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class Lab27_2Activity extends AppCompatActivity {
    TextView proximityView;
    TextView accelerometerView;

    SensorManager manager;
    //근접센서
    Sensor proximity;
    //가속도 센서
    Sensor accelerometer;

    //근접센서 max 값.
    float proximityMaximumRange;
    //가속도 센서 이전값
    float lastX;
    float lastY;
    float lastZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab27_2);

        proximityView = (TextView) findViewById(R.id.lab2_proximity);
        accelerometerView=(TextView)findViewById(R.id.lab2_accelerometer);

        //add~~~~~~~~~~~~~~~~~~~~~
        manager=(SensorManager)getSystemService(SENSOR_SERVICE);
        proximity=manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(proximity != null){
            proximityMaximumRange=proximity.getMaximumRange();
        }
        accelerometer=manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //add~~~~~~~~~~~~~~~
        manager.registerListener(listener, proximity, SensorManager.SENSOR_DELAY_UI);
        manager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //add~~~~~~~~~~~~~~~
        manager.unregisterListener(listener);
    }

    
    //add~~~~~~~~~~~~~~~~~~~~~
    SensorEventListener listener=new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType()==Sensor.TYPE_PROXIMITY){
                float distance=event.values[0];
                if(distance<proximityMaximumRange){
                    proximityView.setBackgroundColor(ResourcesCompat.getColor(getResources(),
                            android.R.color.holo_red_dark, null));
                }else {
                    proximityView.setBackgroundColor(ResourcesCompat.getColor(getResources(),
                            android.R.color.holo_blue_light, null));
                }
            }else if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                float xValue=Math.abs(event.values[0]-lastX);
                float yValue=Math.abs(event.values[1]-lastY);
                float zValue=Math.abs(event.values[2]-lastZ);

                if(xValue + yValue + zValue > 20){
                    accelerometerView.setBackgroundColor(ResourcesCompat.getColor(getResources(),
                            android.R.color.holo_orange_dark, null));
                }else {
                    accelerometerView.setBackgroundColor(ResourcesCompat.getColor(getResources(),
                            android.R.color.holo_blue_light, null));
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    
}