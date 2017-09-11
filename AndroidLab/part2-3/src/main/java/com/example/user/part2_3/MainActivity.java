package com.example.user.part2_3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linear=new LinearLayout(this);

        Button bt=new Button(this);
        bt.setText("Button1");
        linear.addView(bt);

        Button bt2=new Button(this);
        bt2.setText("Button2");
        linear.addView(bt2);

        setContentView(linear);
    }
}
