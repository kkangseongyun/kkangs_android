package com.example.part11_34

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
class DBHelper(context: Context): SQLiteOpenHelper(context, "datadb", null, 1) {
    override fun onCreate(p0: SQLiteDatabase?) {
        val tableSql="create table tb_data ("+
                "_id integer primary key autoincrement," +
                "name not null," +
                "date)"

        p0!!.execSQL(tableSql)

        p0!!.execSQL("insert into tb_data (name,date) values ('안영주','2017-07-01')")
        p0!!.execSQL("insert into tb_data (name,date) values ('최은경','2017-07-01')")
        p0!!.execSQL("insert into tb_data (name,date) values ('최호성','2017-07-01')")
        p0!!.execSQL("insert into tb_data (name,date) values ('정성택','2017-06-30')")
        p0!!.execSQL("insert into tb_data (name,date) values ('정길용','2017-06-30')")
        p0!!.execSQL("insert into tb_data (name,date) values ('임윤정','2017-06-29')")
        p0!!.execSQL("insert into tb_data (name,date) values ('김종덕','2017-06-29')")
        p0!!.execSQL("insert into tb_data (name,date) values ('채규태','2017-06-28')")
        p0!!.execSQL("insert into tb_data (name,date) values ('원형섭','2017-06-28')")
        p0!!.execSQL("insert into tb_data (name,date) values ('표선영','2017-06-28')")

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("drop table tb_data")
        onCreate(p0)
    }
}