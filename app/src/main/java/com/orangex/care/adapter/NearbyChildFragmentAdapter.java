package com.orangex.care.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.orangex.care.common.CommonKey;
import com.orangex.care.fragment.NearbyChildFragment;

/**
 * Created by orangex on 2017/4/12.
 */

public class NearbyChildFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{CommonKey.TYPE_HOSPITAL, CommonKey.TYPE_119, CommonKey.TYPE_POLICE, CommonKey.TYPE_WC};
    
    public NearbyChildFragmentAdapter(FragmentManager fm) {
        super(fm);
    }
    
    @Override
    public Fragment getItem(int position) {
        
        return NearbyChildFragment.newInstance(NearbyChildFragment.class, mTitles[position]);
    }
    
    @Override
    public int getCount() {
        return mTitles.length;
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
