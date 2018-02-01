package com.youthlive.youthlive;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;


public class Screen3 extends Fragment {

    TabLayout layout;
    ViewPager pager;
    HotAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen3 , container , false);
        layout = (TabLayout) view.findViewById(R.id.tab);
        pager = (ViewPager)view .findViewById(R.id.pager);
        //adapter = new HotAdapter(getContext());
        layout.addTab(layout.newTab().setText("Hot Vlog"));
        layout.addTab(layout.newTab().setText("Nearby"));



        layout.getTabAt(0).setText("Hot Vlog");
        layout.getTabAt(1).setText("Nearby");
        layout.setupWithViewPager(pager);
        return view;
    }
    public class Viewpager extends FragmentStatePagerAdapter {
        int tab;


        public Viewpager(FragmentManager fm , int List) {
            super(fm);
            this.tab = List;
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0)
            {
                return new Vlog();
            }
            else if (position == 1){
                return new Message();
            }
            else  if (position == 2){
                return new Wallet();
            }
            else if (position == 3){
                return new CheckIn();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }


}
