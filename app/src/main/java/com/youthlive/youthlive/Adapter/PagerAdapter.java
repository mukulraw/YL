package com.youthlive.youthlive.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.youthlive.youthlive.Fragments.Last24Hour;
import com.youthlive.youthlive.Fragments.Last7Days;
import com.youthlive.youthlive.Fragments.ThisHour;

/**
 * Created by Designer 3 on 14-10-2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ThisHour tab1 = new ThisHour();
                return tab1;
            case 1:
                Last24Hour tab2 = new Last24Hour();
                return tab2;
            case 2:
                Last7Days tab3 = new Last7Days();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
