package com.example.admins.snaphotel.Activities.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.admins.snaphotel.fragment.DetailFragment;
import com.example.admins.snaphotel.fragment.RatingFragment;

/**
 * Created by Admins on 4/6/2018.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DetailFragment();
            case 1:
                return new RatingFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
