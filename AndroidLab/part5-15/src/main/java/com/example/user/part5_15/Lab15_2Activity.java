package com.example.user.part5_15;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class Lab15_2Activity extends AppCompatActivity implements View.OnClickListener{
    Button keyboardBtn;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab15_2);
        keyboardBtn=(Button)findViewById(R.id.lab2_toggleBtn);
        editText=(EditText)findViewById(R.id.lab2_edit);

        keyboardBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        InputMethodManager manager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void showToast(String message){
        Toast toast=Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showToast("onResume.....");
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.N){
            if(isInMultiWindowMode()){
                showToast("onResume....isInMultiWindowMode.. yes ");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        showToast("onPause......");
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        showToast("onMultiWindowModeChanged....."+isInMultiWindowMode);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            showToast("portrait.....");
        }else {
            showToast("landscape.....");
        }
    }
}

