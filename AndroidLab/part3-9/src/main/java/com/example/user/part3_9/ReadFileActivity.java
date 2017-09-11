package com.example.user.part3_9;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class ReadFileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_file);

        TextView textView=(TextView)findViewById(R.id.fileResult);

        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/myApp/myfile.txt");
        try {
            BufferedReader reader=new BufferedReader(new FileReader(file));
            StringBuffer buffer=new StringBuffer();
            String line;
            while ((line=reader.readLine()) != null){
                buffer.append(line);
            }
            textView.setText(buffer.toString());
            reader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

