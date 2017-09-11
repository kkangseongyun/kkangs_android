package com.example.user.part5_16;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView startView;
    ImageView pauseView;
    TextView textView;

    boolean loopFlag = true;
    boolean isFirst = true;
    boolean isRun;

    MyThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startView = (ImageView) findViewById(R.id.main_startBtn);
        pauseView = (ImageView) findViewById(R.id.main_pauseBtn);
        textView = (TextView) findViewById(R.id.main_textView);

        startView.setOnClickListener(this);
        pauseView.setOnClickListener(this);

        thread = new MyThread();
    }

    @Override
    public void onClick(View v) {
        if (v == startView) {
            if (isFirst) {
                isFirst = false;
                isRun = true;
                thread.start();
            } else {
                isRun = true;
            }
        } else if (v == pauseView) {
            isRun = false;
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                textView.setText(String.valueOf(msg.arg1));
            }else if(msg.what==2){
                textView.setText((String)msg.obj);
            }
        }
    };

    class MyThread extends Thread {
        @Override
        public void run() {
            try{
                int count=10;
                while (loopFlag){
                    Thread.sleep(1000);
                    if(isRun){
                        count--;
                        Message message=new Message();
                        message.what=1;
                        message.arg1=count;
                        handler.sendMessage(message);
                        if(count==0){
                            message=new Message();
                            message.what=2;
                            message.obj="Finish!!";
                            handler.sendMessage(message);
                            loopFlag=false;
                        }
                    }
                }
            }catch (Exception e){

            }
        }
    }

    
}














