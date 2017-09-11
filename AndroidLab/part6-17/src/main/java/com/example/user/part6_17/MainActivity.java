package com.example.user.part6_17;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn1;
    Button btn2;
    Button btn3;

    FragmentManager manager;
    OneFragment oneFragment;
    TwoFragment twoFragment;
    ThreeFragment threeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1=(Button)findViewById(R.id.main_btn1);
        btn2=(Button)findViewById(R.id.main_btn2);
        btn3=(Button)findViewById(R.id.main_btn3);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);

        manager=getSupportFragmentManager();
        oneFragment=new OneFragment();
        twoFragment=new TwoFragment();
        threeFragment=new ThreeFragment();

        FragmentTransaction tf=manager.beginTransaction();
        tf.addToBackStack(null);
        tf.add(R.id.main_container, oneFragment);
        tf.commit();

    }

    @Override
    public void onClick(View v) {
        if(v==btn1){
            if(!oneFragment.isVisible()){
                FragmentTransaction ft=manager.beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.main_container, oneFragment);
                ft.commit();
            }
        }else if(v==btn2){
            if(!twoFragment.isVisible()){
                twoFragment.show(manager, null);
            }
        }else if(v==btn3){
            if(!threeFragment.isVisible()){
                FragmentTransaction ft=manager.beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.main_container, threeFragment);
                ft.commit();
            }
        }
    }
}







