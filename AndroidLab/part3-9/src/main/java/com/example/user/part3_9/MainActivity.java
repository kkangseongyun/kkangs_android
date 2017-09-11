package com.example.user.part3_9;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText contentView;
    Button btn;

    boolean fileReadPermission;
    boolean fileWritePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentView=(EditText)findViewById(R.id.content);
        btn=(Button)findViewById(R.id.btn);

        btn.setOnClickListener(this);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED){
            fileReadPermission=true;
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED){
            fileWritePermission=true;
        }

        if(!fileReadPermission || !fileWritePermission){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==200 && grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                fileReadPermission=true;
            if(grantResults[1]==PackageManager.PERMISSION_GRANTED)
                fileWritePermission=true;
        }
    }

    @Override
    public void onClick(View v) {
        String content=contentView.getText().toString();
        if(fileReadPermission && fileWritePermission){
            FileWriter writer;
            try{
                String dirPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/myApp";
                File dir=new File(dirPath);
                if(!dir.exists()){
                    dir.mkdir();
                }
                File file=new File(dir+"/myfile.txt");
                if(!file.exists()){
                    file.createNewFile();
                }

                writer=new FileWriter(file, true);
                writer.write(content);
                writer.flush();
                writer.close();


                Intent intent=new Intent(this, ReadFileActivity.class);
                startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            showToast("permission 이 부여되지 않아서 기능을 수행할수 없습니다.");
        }
    }

    private void showToast(String message){
        Toast toast=Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}

