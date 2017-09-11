package com.example.user.part5_16;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class Lab16_2Activity extends AppCompatActivity implements View.OnClickListener {
    ImageView startView;
    ImageView pauseView;
    TextView textView;

    boolean isFirst = true;

    MyAsyncTask asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab16_2);

        startView = (ImageView) findViewById(R.id.main_startBtn);
        pauseView = (ImageView) findViewById(R.id.main_pauseBtn);
        textView = (TextView) findViewById(R.id.main_textView);

        startView.setOnClickListener(this);
        pauseView.setOnClickListener(this);

        asyncTask = new MyAsyncTask();
    }

    @Override
    public void onClick(View v) {
        if(v==startView){
            if(isFirst){
                asyncTask.isRun=true;
                asyncTask.execute();
                isFirst=false;
            }else {
                asyncTask.isRun=true;
            }
        }else if(v==pauseView){
            asyncTask.isRun=false;
        }
    }

    class MyAsyncTask extends AsyncTask<Void, Integer, String>{
        boolean loopFlag=true;
        boolean isRun;

        @Override
        protected String doInBackground(Void... params) {
            int count=10;
            while (loopFlag){
                SystemClock.sleep(1000);
                if(isRun){
                    count--;
                    publishProgress(count);
                    if(count==0){
                        loopFlag=false;
                    }
                }
            }
            return "Finish!!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            textView.setText(String.valueOf(values[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            textView.setText(s);
        }
    }
}
