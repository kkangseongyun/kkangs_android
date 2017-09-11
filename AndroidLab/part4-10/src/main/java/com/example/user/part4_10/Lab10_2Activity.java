package com.example.user.part4_10;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class Lab10_2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab10_2);

        ListView listView=(ListView)findViewById(R.id.custom_listview);

        DBHelper helper=new DBHelper(this);
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from tb_drive", null);

        ArrayList<DriveVO> datas=new ArrayList<>();
        while (cursor.moveToNext()){
            DriveVO vo=new DriveVO();
            vo.type=cursor.getString(3);
            vo.title=cursor.getString(1);
            vo.date=cursor.getString(2);
            datas.add(vo);
        }
        db.close();

        DriveAdapter adapter=new DriveAdapter(this, R.layout.custom_item, datas);
        listView.setAdapter(adapter);
    }
}
