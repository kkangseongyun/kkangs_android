package com.example.user.part7_21;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    ListView listView;
    EditText nameView;
    EditText phoneView;
    Button btn;

    boolean isUpdate;
    String _id;

    ArrayList<HashMap<String, String>> datas;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=(ListView)findViewById(R.id.lab1_listview);
        nameView=(EditText) findViewById(R.id.lab1_name);
        phoneView=(EditText) findViewById(R.id.lab1_phone);
        btn=(Button)findViewById(R.id.lab1_btn);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        btn.setOnClickListener(this);


        uri=Uri.parse("content://com.example.part.Provider");
        setAdapter();

        
    }

    private void setAdapter(){
        datas=new ArrayList<>();
        Cursor cursor=getContentResolver().query(uri, null, null, null, null);
        while (cursor.moveToNext()){
            HashMap<String, String> map=new HashMap<>();
            map.put("id", cursor.getString(0));
            map.put("name", cursor.getString(1));
            map.put("phone", cursor.getString(2));
            datas.add(map);
        }
        SimpleAdapter adapter=new SimpleAdapter(this, datas, android.R.layout.simple_list_item_2,
                new String[]{"name", "phone"}, new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(isUpdate){
            //update.......
            String name=nameView.getText().toString();
            String phone=phoneView.getText().toString();
            if(!name.equals("") && !phone.equals("")){
                ContentValues values=new ContentValues();
                values.put("name", name);
                values.put("phone", phone);
                getContentResolver().update(uri, values, "_id=?", new String[]{_id});
                setAdapter();
            }
            nameView.setText("");
            phoneView.setText("");
            isUpdate=false;
            
        }else {
            //insert.......
            String name=nameView.getText().toString();
            String phone=phoneView.getText().toString();
            if(!name.equals("") && !phone.equals("")){
                ContentValues values=new ContentValues();
                values.put("name", name);
                values.put("phone", phone);
                getContentResolver().insert(uri, values);
                setAdapter();
            }
            nameView.setText("");
            phoneView.setText("");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> map=datas.get(position);
        nameView.setText(map.get("name"));
        phoneView.setText(map.get("phone"));
        _id=map.get("id");
        isUpdate=true;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> map=datas.get(position);
        getContentResolver().delete(uri, "_id=?", new String[]{map.get("id")});
        setAdapter();
        return false;
    }
}
