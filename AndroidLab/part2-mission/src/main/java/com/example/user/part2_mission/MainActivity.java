package com.example.user.part2_mission;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn=(Button)findViewById(R.id.messenger_btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast toast=Toast.makeText(this, "ok button click~~~~", Toast.LENGTH_SHORT);
        toast.show();
    }
}

