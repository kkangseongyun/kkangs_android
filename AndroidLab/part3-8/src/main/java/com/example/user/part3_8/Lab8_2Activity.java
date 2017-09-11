package com.example.user.part3_8;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.realm.Realm;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class Lab8_2Activity extends AppCompatActivity implements View.OnClickListener{

    EditText titleView;
    EditText contentView;
    Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab8_2);
        titleView=(EditText)findViewById(R.id.realm_add_title);
        contentView=(EditText)findViewById(R.id.realm_add_content);
        addBtn=(Button)findViewById(R.id.realm_add_btn);

        addBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final String title=titleView.getText().toString();
        final String content=contentView.getText().toString();

        Realm.init(this);
        Realm mRealm=Realm.getDefaultInstance();

        mRealm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                MemoVO vo=realm.createObject(MemoVO.class);
                vo.title=title;
                vo.content=content;
            }
        });

        Intent intent=new Intent(this, RealmReadActivity.class);
        intent.putExtra("title", title);
        startActivity(intent);
    }
}

