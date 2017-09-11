package com.example.user.part9_25;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class Lab25_3Activity extends AppCompatActivity {
    ImageView gifView;
    ImageView networkView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab25_3);
        gifView=(ImageView)findViewById(R.id.lab3_gif);
        networkView=(ImageView)findViewById(R.id.lab3_network);

        Glide.with(this)
                .load(R.raw.loading)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .override(200, 200)
                .into(gifView);

        String url="http://70.12.205.94:8000/files/1.jpg";
        Glide.with(this)
                .load(url)
                .override(400, 400)
                .error(R.drawable.error)
                .into(networkView);
    }
}
