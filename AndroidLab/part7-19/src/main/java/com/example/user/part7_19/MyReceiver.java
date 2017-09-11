package com.example.user.part7_19;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if(action.equals("android.intent.action.NEW_OUTGOING_CALL")){
            String phoneNumber=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Intent intent1=new Intent(context, DialogActivity.class);
            intent1.putExtra("number", phoneNumber);
            context.startActivity(intent1);
        }else if(action.equals("android.intent.action.PHONE_STATE")){
            Bundle bundle=intent.getExtras();
            String state=bundle.getString(TelephonyManager.EXTRA_STATE);
            String phoneNumber=bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                Intent intent1=new Intent(context, DialogActivity.class);
                intent1.putExtra("number", phoneNumber);
                context.startActivity(intent1);
            }
        }
    }
}
