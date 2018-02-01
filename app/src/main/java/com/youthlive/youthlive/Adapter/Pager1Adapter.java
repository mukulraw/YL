package com.youthlive.youthlive.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.youthlive.youthlive.Fragments.AddressFragment;
import com.youthlive.youthlive.Fragments.CareerFragment;
import com.youthlive.youthlive.Fragments.EducationFragment;
import com.youthlive.youthlive.Fragments.Last24Hour;
import com.youthlive.youthlive.Fragments.Last7Days;
import com.youthlive.youthlive.Fragments.ThisHour;

/**
 * Created by Designer 3 on 26-10-2017.
 */

public class Pager1Adapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public Pager1Adapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                AddressFragment tab1 = new AddressFragment();
                return tab1;
            case 1:
                EducationFragment tab2 = new EducationFragment();
                return tab2;
            case 2:
                CareerFragment tab3 = new CareerFragment();
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
