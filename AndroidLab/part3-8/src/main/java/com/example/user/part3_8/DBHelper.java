package com.example.user.part3_8;

/**
 * Created by kkang on 2017-08-16.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=1;
    public DBHelper(Context context){
        super(context, "memodb", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String memoSQL="create table tb_memo (" +
                "_id integer primary key autoincrement," +
                "title," +
                "content)";
        db.execSQL(memoSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion==DATABASE_VERSION){
            db.execSQL("drop table tb_memo");
            onCreate(db);
        }
    }
}
