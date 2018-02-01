package com.youthlive.youthlive.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.youthlive.youthlive.Fragments.Last24Hour;
import com.youthlive.youthlive.Fragments.Last7Days;
import com.youthlive.youthlive.Fragments.MessageFragment;
import com.youthlive.youthlive.Fragments.ThisHour;
import com.youthlive.youthlive.Fragments.VlogFragment;

/**
 * Created by Designer 3 on 26-10-2017.
 */

public class Pager2Adapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public Pager2Adapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                VlogFragment tab1 = new VlogFragment();
                return tab1;
            case 1:
                MessageFragment tab2 = new MessageFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}