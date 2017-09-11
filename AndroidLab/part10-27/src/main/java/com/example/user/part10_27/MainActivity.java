package com.example.user.part10_27;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button sendBtn;
    EditText numberView;
    EditText messageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendBtn = (Button) findViewById(R.id.lab1_btn_send);
        numberView = (EditText) findViewById(R.id.lab1_phoneNumber);
        messageView = (EditText) findViewById(R.id.lab1_message);

        sendBtn.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 200);

        }
    }

    private void showToast(String message){
        Toast toast=Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onClick(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            //add~~~~~~~~~~~~~~
            TelephonyManager telephony=(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
            String myNumber=telephony.getLine1Number();

            String phoneNumber=numberView.getText().toString();
            String message=messageView.getText().toString();

            Intent intent=new Intent("ACTION_SENT");
            PendingIntent sentPIntent=PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, myNumber, message, sentPIntent, null);

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.SEND_SMS}, 100);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(sentReceiver, new IntentFilter("ACTION_SENT"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(sentReceiver);
    }

    BroadcastReceiver sentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //add~~~~~~~~~~~~~~~~
            String msg="";
            switch (getResultCode()){
                case Activity.RESULT_OK:
                    msg="sms 전송 성공";
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    msg="sms 전송 실패";
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    msg="무선 꺼짐";
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    msg="pdu 오류";
                    break;
            }
            showToast(msg);
            
        }
    };



}

