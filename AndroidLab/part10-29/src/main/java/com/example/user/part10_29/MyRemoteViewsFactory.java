package com.example.user.part10_29;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */

public class MyRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{
    private Context context=null;
    private ArrayList<ItemVO> arrayList;
    public MyRemoteViewsFactory(Context context){
        this.context=context;
    }

    class ItemVO {
        int _id;
        String content;
    }
    private void selectDB(){
        arrayList=new ArrayList<>();
        DBHelper helper=new DBHelper(context);
        SQLiteDatabase db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select _id, content from tb_data order by _id desc limit 5", null);
        while (cursor.moveToNext()){
            ItemVO vo=new ItemVO();
            vo._id=cursor.getInt(0);
            vo.content=cursor.getString(1);
            arrayList.add(vo);
        }
    }

    @Override
    public void onCreate() {
        selectDB();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row=new RemoteViews(context.getPackageName(), R.layout.item_collection);
        row.setTextViewText(R.id.text1, arrayList.get(position).content);

        Intent i=new Intent();
        i.putExtra("item_id", arrayList.get(position)._id);
        i.putExtra("item_data", arrayList.get(position).content);
        row.setOnClickFillInIntent(R.id.text1, i);

        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void onDataSetChanged() {
        selectDB();
    }
}
