package com.zbar.lib.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.List;


public class FragmentListAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public FragmentListAdapter(FragmentManager fragmentManager,
                               List<Fragment> arrayList) {
        super(fragmentManager);
        this.fragmentList = arrayList;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragmentList.get(arg0);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


}
