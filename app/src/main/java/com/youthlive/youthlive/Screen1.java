package com.youthlive.youthlive;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Screen1 extends AppCompatActivity {

    TabLayout layout;
    PagerAdapter adapter;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        layout = (TabLayout) findViewById(R.id.tab);

        pager = (ViewPager) findViewById(R.id.pager);

        layout.addTab(layout.newTab().setText("Vlog"));

        layout.addTab(layout.newTab().setText("Message"));

        layout.addTab(layout.newTab().setText("Wallet"));

        layout.addTab(layout.newTab().setText("Check In"));

        adapter = new PagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        layout.setupWithViewPager(pager);

        layout.getTabAt(0).setText("Vlog");

        layout.getTabAt(1).setText("Message");

        layout.getTabAt(2).setText("Wallet");

        layout.getTabAt(3).setText("Check In");

    }

}

