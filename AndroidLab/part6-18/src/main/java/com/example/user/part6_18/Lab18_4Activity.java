package com.example.user.part6_18;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class Lab18_4Activity extends AppCompatActivity implements View.OnClickListener {

    Button btn;
    CoordinatorLayout coordinatorLayout;
    BottomSheetBehavior<View> persistentBottomSheet;
    BottomSheetDialog modalBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab18_4);

        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.lab4_coordinator);
        btn=(Button)findViewById(R.id.lab4_button);
        btn.setOnClickListener(this);

        initPeristentBottomSheet();
    }

    @Override
    public void onClick(View v) {
        createDialog();
    }

    private void createDialog(){
        List<DataVO> list=new ArrayList<>();
        DataVO vo=new DataVO();
        vo.title="Keep";
        vo.image= ResourcesCompat.getDrawable(getResources(), R.drawable.ic_lab4_1, null);
        list.add(vo);

        vo=new DataVO();
        vo.title="Inbox";
        vo.image= ResourcesCompat.getDrawable(getResources(), R.drawable.ic_lab4_2, null);
        list.add(vo);

        vo=new DataVO();
        vo.title="Messanger";
        vo.image= ResourcesCompat.getDrawable(getResources(), R.drawable.ic_lab4_3, null);
        list.add(vo);

        vo=new DataVO();
        vo.title="Google+";
        vo.image= ResourcesCompat.getDrawable(getResources(), R.drawable.ic_lab4_4, null);
        list.add(vo);

        Lab4RecyclerViewAdapter adapter=new Lab4RecyclerViewAdapter(list);
        View view=getLayoutInflater().inflate(R.layout.lab4_modal_sheet, null);
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.lab4_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        modalBottomSheet=new BottomSheetDialog(this);
        modalBottomSheet.setContentView(view);
        modalBottomSheet.show();
    }

    private void initPeristentBottomSheet(){
        View bottomSheet=coordinatorLayout.findViewById(R.id.lab4_bottom_sheet);
        persistentBottomSheet=BottomSheetBehavior.from(bottomSheet);
    }
}


















