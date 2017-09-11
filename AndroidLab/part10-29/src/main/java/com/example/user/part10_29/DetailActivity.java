package com.example.user.part10_29;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int id = getIntent().getIntExtra("item_id",0);
        String content=getIntent().getStringExtra("item_data");

        TextView tv=(TextView)findViewById(R.id.detail_text);

        if (id != 0 && content != null) {
            tv.setText(id+":"+content);
        }
    }
}
