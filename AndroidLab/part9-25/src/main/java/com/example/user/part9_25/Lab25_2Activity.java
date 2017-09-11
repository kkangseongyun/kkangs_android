package com.example.user.part9_25;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class Lab25_2Activity extends AppCompatActivity {
    TextView titleView;
    TextView dateView;
    TextView contentView;
    NetworkImageView imageView;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab25_2);

        titleView = (TextView) findViewById(R.id.lab2_title);
        dateView = (TextView) findViewById(R.id.lab2_date);
        contentView = (TextView) findViewById(R.id.lab2_content);

        //add~~~~~~~~~~~~~~~
        imageView=(NetworkImageView)findViewById(R.id.lab2_image);

        queue= Volley.newRequestQueue(this);

        JsonObjectRequest jsonRequest=new JsonObjectRequest(Request.Method.GET, "http://70.12.205.94:8000/files/test.json", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    titleView.setText(response.getString("title"));
                    dateView.setText(response.getString("date"));
                    contentView.setText(response.getString("content"));

                    String imageFile = response.getString("file");
                    if (imageFile != null && !imageFile.equals("")) {
                        ImageLoader imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
                            @Override
                            public Bitmap getBitmap(String url) {
                                return null;
                            }

                            @Override
                            public void putBitmap(String url, Bitmap bitmap) {

                            }
                        });
                        imageView.setImageUrl("http://70.12.205.94:8000/files/" + imageFile, imageLoader);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonRequest);
        



    }
}
