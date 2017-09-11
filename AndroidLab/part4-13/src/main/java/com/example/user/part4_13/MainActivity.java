package com.example.user.part4_13;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MainActivity extends AppCompatActivity implements OnMyChangeListener{

    View barView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyPlusMinusView plusMinusView=(MyPlusMinusView)findViewById(R.id.customView);
        barView=(View)findViewById(R.id.barView);

        plusMinusView.setOnMyChangeListener(this);
    }

    @Override
    public void onChange(int value) {
        if(value<0){
            barView.setBackgroundColor(Color.RED);
        }else if(value<30){
            barView.setBackgroundColor(Color.YELLOW);
        }else if(value<60){
            barView.setBackgroundColor(Color.BLUE);
        }else {
            barView.setBackgroundColor(Color.GREEN);
        }
    }
}
