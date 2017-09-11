package com.example.user.part4_mission;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */

public class CallLogWrapper {
    public ImageView personImageView;
    public TextView nameView;
    public TextView dateView;
    public ImageView dialerImageView;
    public CallLogWrapper(View root){
        personImageView=(ImageView)root.findViewById(R.id.main_item_person);
        nameView=(TextView)root.findViewById(R.id.main_item_name);
        dateView=(TextView)root.findViewById(R.id.main_item_date);
        dialerImageView=(ImageView)root.findViewById(R.id.main_item_dialer);
    }
}
