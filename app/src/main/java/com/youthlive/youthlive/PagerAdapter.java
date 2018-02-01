package com.youthlive.youthlive;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int tab;


    public PagerAdapter(FragmentManager fm) {
        super(fm);
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
