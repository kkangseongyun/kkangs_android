package com.example.user.part5_mission;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class Mission2Activity extends AppCompatActivity implements View.OnClickListener{
    ImageView callIcon;
    ImageView locationIcon;
    ImageView internetIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission2);

        callIcon=(ImageView)findViewById(R.id.mission2_call);
        locationIcon=(ImageView)findViewById(R.id.mission2_location);
        internetIcon=(ImageView)findViewById(R.id.mission2_internet);

        callIcon.setOnClickListener(this);
        locationIcon.setOnClickListener(this);
        internetIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==callIcon){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:02-120"));
                startActivity(intent);
            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 100);
            }
        }else if(v==locationIcon){
            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("geo:37.5662952,126.9779451"));
            startActivity(intent);
        }else if(v==internetIcon){
            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.seoul.go.kr"));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100 && grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:02-120"));
                startActivity(intent);
            }else {
                Toast toast=Toast.makeText(this, "no permission", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}















