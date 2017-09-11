package com.example.user.part2_3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class Lab3_3Activity extends AppCompatActivity implements View.OnClickListener{

    Button trueBtn;
    TextView targetTextView;
    Button falseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3_3);

        trueBtn=(Button)findViewById(R.id.btn_visible_true);
        targetTextView=(TextView)findViewById(R.id.text_visible_target);
        falseBtn=(Button)findViewById(R.id.btn_visible_false);

        trueBtn.setOnClickListener(this);
        falseBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==trueBtn){
            targetTextView.setVisibility(View.VISIBLE);
        }else if(v==falseBtn){
            targetTextView.setVisibility(View.INVISIBLE);
        }
    }
}
