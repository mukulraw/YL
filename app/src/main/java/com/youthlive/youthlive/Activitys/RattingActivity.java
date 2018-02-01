package com.youthlive.youthlive.Activitys;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import com.youthlive.youthlive.Adapter.PagerAdapter;
import com.youthlive.youthlive.GetRankingPOJO.Datum;
import com.youthlive.youthlive.GetRankingPOJO.RankingBean;
import com.youthlive.youthlive.INTERFACE.AllAPIs;
import com.youthlive.youthlive.R;
import com.youthlive.youthlive.RankingAdapter;
import com.youthlive.youthlive.Rating1;
import com.youthlive.youthlive.Rating2;
import com.youthlive.youthlive.Rating3;
import com.youthlive.youthlive.bean;
import com.youthlive.youthlive.loginResponsePOJO.loginResponseBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RattingActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;


    TabLayout layout;
    ViewPager pager;
    ProgressBar bar;
    ViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratting);


        layout = (TabLayout)findViewById(R.id.tab_layout);
        pager = (ViewPager)findViewById(R.id.pager);

        layout.addTab(layout.newTab().setText("This hour"));
        layout.addTab(layout.newTab().setText("Last 24 hour"));
        layout.addTab(layout.newTab().setText("Last 7 days"));

        layout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new ViewAdapter(getSupportFragmentManager(), 3);

        layout.setupWithViewPager(pager);
        pager.setAdapter(adapter);

        layout.getTabAt(0).setText("This hour");
        layout.getTabAt(1).setText("Last 24 hour");
        layout.getTabAt(2).setText("Last 7 days");



       toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setTitle("Ranking");

    }

    public class ViewAdapter extends FragmentStatePagerAdapter{

        int tabs;

        public ViewAdapter(FragmentManager fm , int list) {
            super(fm);

            this.tabs = list;
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0){

                return new Rating1();
            }

            else if (position == 1){

                return new Rating2();
            }

           else if (position == 2){

                return new Rating3();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }





}
