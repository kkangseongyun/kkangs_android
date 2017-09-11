package com.example.user.part6_mission;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=5;

    public DBHelper(Context context){
        super(context, "datadb", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableSql="create table tb_data ("+
                "_id integer primary key autoincrement," +
                "name not null," +
                "date)";

        db.execSQL(tableSql);

        db.execSQL("insert into tb_data (name,date) values ('안영주','2017-07-01')");
        db.execSQL("insert into tb_data (name,date) values ('최은경','2017-07-01')");
        db.execSQL("insert into tb_data (name,date) values ('최호성','2017-07-01')");
        db.execSQL("insert into tb_data (name,date) values ('정성택','2017-06-30')");
        db.execSQL("insert into tb_data (name,date) values ('정길용','2017-06-30')");
        db.execSQL("insert into tb_data (name,date) values ('임윤정','2017-06-29')");
        db.execSQL("insert into tb_data (name,date) values ('김종덕','2017-06-29')");
        db.execSQL("insert into tb_data (name,date) values ('채규태','2017-06-28')");
        db.execSQL("insert into tb_data (name,date) values ('원형섭','2017-06-28')");
        db.execSQL("insert into tb_data (name,date) values ('표선영','2017-06-28')");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == DATABASE_VERSION){
            db.execSQL("drop table tb_data");
            onCreate(db);
        }
    }
}
