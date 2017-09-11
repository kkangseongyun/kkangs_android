package com.example.user.part5_14;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, "mydb", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String memoSQL= "create table tb_data " +
                "(_id integer primary key autoincrement,"
                + "category,"
                + "location)";

        db.execSQL(memoSQL);

        db.execSQL("insert into tb_data (category, location) values ('0', '서울특별시')");
        db.execSQL("insert into tb_data (category, location) values ('0', '경기도')");

        db.execSQL("insert into tb_data (category, location) values ('서울특별시', '종로구')");
        db.execSQL("insert into tb_data (category, location) values ('서울특별시', '강남구')");
        db.execSQL("insert into tb_data (category, location) values ('서울특별시', '송파구')");

        db.execSQL("insert into tb_data (category, location) values ('경기도', '성남시')");
        db.execSQL("insert into tb_data (category, location) values ('경기도', '수원시')");
        db.execSQL("insert into tb_data (category, location) values ('경기도', '용인시')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion==DATABASE_VERSION){
            db.execSQL("drop table tb_data");
            onCreate(db);
        }

    }

}