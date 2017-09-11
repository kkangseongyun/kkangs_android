package com.example.user.part10_29;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class Lab29_2Activity extends AppCompatActivity implements View.OnClickListener {
    EditText editText;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab29_2);
        editText=(EditText)findViewById(R.id.lab2_edit);
        btn=(Button)findViewById(R.id.lab2_btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String data=editText.getText().toString();
        if(data != null && !data.equals("")) {
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("insert into tb_data (content) values (?)",
                    new String[]{data});
            db.close();

            Intent intent = new Intent(this, MyCollectionWidgetReceiver.class);
            intent.putExtra("mode","data");
            sendBroadcast(intent);
        }
    }
}
