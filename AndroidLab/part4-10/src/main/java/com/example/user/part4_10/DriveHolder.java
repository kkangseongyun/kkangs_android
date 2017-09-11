package com.example.user.part4_10;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */

public class DriveHolder {
    public ImageView typeImageView;
    public TextView titleView;
    public TextView dateView;
    public ImageView menuImageView;

    public DriveHolder(View root){
        typeImageView=(ImageView)root.findViewById(R.id.custom_item_type_image);
        titleView=(TextView)root.findViewById(R.id.custom_item_title);
        dateView=(TextView)root.findViewById(R.id.custom_item_date);
        menuImageView=(ImageView)root.findViewById(R.id.custom_item_menu);
    }
}
