package com.example.user.part4_mission;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class Mission3Activity extends AppCompatActivity implements AdapterView.OnItemClickListener, TextWatcher{

    EditText editText;
    ListView listView;


    ArrayList<String> base;
    ArrayList<SpannableStringBuilder> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission3);

        editText=(EditText)findViewById(R.id.mission3_edit);
        listView=(ListView)findViewById(R.id.mission3_list);

        listView.setOnItemClickListener(this);
        editText.addTextChangedListener(this);

        base=new ArrayList<>();
        base.add("우편번호 검색");
        base.add("지도검색");
        base.add("대법원 나의 사건 검색");
        base.add("나의 사진검색");
        base.add("주소검색");
        base.add("다음지도 검색");
        base.add("검색등록");

        datas=new ArrayList<>();

        ArrayAdapter<SpannableStringBuilder> adapter=new ArrayAdapter<SpannableStringBuilder>(this, android.R.layout.simple_list_item_1, datas);
        listView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        editText.setText(datas.get(position).toString());
        datas=new ArrayList<>();
        ArrayAdapter<SpannableStringBuilder> adapter=new ArrayAdapter<SpannableStringBuilder>(this, android.R.layout.simple_list_item_1, datas);
        listView.setAdapter(adapter);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        datas=new ArrayList<>();
        for(String txt:base){
            if(txt.contains(s)){
                int startPos=txt.indexOf(s.toString());
                int endPos=startPos+s.length();
                SpannableStringBuilder builder=new SpannableStringBuilder(txt);
                builder.setSpan(new ForegroundColorSpan(Color.BLUE), startPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new StyleSpan(Typeface.BOLD), startPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                datas.add(builder);
            }
        }

        ArrayAdapter<SpannableStringBuilder> adapter=new ArrayAdapter<SpannableStringBuilder>(this, android.R.layout.simple_list_item_1, datas);
        listView.setAdapter(adapter);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
