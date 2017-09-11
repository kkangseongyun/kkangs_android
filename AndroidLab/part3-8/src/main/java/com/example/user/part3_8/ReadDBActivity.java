package com.example.user.part3_8;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class ReadDBActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_db);

        TextView titleView=(TextView)findViewById(R.id.read_title);
        TextView contentView=(TextView)findViewById(R.id.read_content);

        DBHelper helper=new DBHelper(this);
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select title, content from tb_memo order by _id desc limit 1", null);

        while (cursor.moveToNext()){
            titleView.setText(cursor.getString(0));
            contentView.setText(cursor.getString(1));
        }
        db.close();
    }
}
