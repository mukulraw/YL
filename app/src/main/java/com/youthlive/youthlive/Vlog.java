package com.youthlive.youthlive;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;


public class Vlog extends Fragment{



    TabLayout tabs;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vlog , container , false);

        tabs = (TabLayout)view.findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        tabs.addTab(tabs.newTab().setText("HOT"));
        tabs.addTab(tabs.newTab().setText("NEARBY"));

        /*FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getChildFragmentManager(), FragmentPagerItems.with(getContext())
                .add("Hot", HotVolg.class)
                .add("Nearby", HotVolg.class)
                .create());*/

        ViewAdapter adapter = new ViewAdapter(getChildFragmentManager());

        viewPager.setAdapter(adapter);

        tabs.setupWithViewPager(viewPager);

        tabs.getTabAt(0).setText("HOT");
        tabs.getTabAt(1).setText("NEARBY");

        return view;
    }

    class ViewAdapter extends FragmentStatePagerAdapter
    {

        public ViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new HotVolg();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
