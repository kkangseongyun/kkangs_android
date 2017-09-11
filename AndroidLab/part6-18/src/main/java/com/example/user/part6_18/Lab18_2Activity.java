package com.example.user.part6_18;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class Lab18_2Activity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener{

    ViewPager viewPager;
    FloatingActionButton fab;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab18_2);

        relativeLayout=(RelativeLayout)findViewById(R.id.lab2_container);
        viewPager=(ViewPager)findViewById(R.id.lab2_viewpager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout=(TabLayout)findViewById(R.id.lab2_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);

        fab=(FloatingActionButton)findViewById(R.id.lab2_fab);
        fab.setOnClickListener(this);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments=new ArrayList<>();
        String title[]=new String[]{"Tab1", "Tab2", "Tab3"};
        public MyPagerAdapter(FragmentManager fm){
            super(fm);
            fragments.add(new OneFragment());
            fragments.add(new TwoFragment());
            fragments.add(new ThreeFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        Snackbar.make(relativeLayout, "I am SnackBar", Snackbar.LENGTH_LONG)
                .setAction("MoreAction", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //...........
                    }
                }).show();
    }
}
