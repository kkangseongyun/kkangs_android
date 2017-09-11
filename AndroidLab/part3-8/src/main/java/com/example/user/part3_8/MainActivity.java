package com.example.user.part3_8;

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
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText titleView;
    EditText contentView;
    Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleView=(EditText)findViewById(R.id.add_title);
        contentView=(EditText)findViewById(R.id.add_content);
        addBtn=(Button)findViewById(R.id.add_btn);

        addBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String title=titleView.getText().toString();
        String content=contentView.getText().toString();

        DBHelper helper=new DBHelper(this);
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL("insert into tb_memo (title, content) values (?,?)",
                new String[]{title, content});
        db.close();

        Intent intent=new Intent(this, ReadDBActivity.class);
        startActivity(intent);
    }
}

