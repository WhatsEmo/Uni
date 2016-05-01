package com.example.whatsemo.classmates.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.whatsemo.classmates.HomePageFragment;
import com.example.whatsemo.classmates.SocialFragment;

/**
 * Created by WhatsEmo on 3/11/2016.
 */
public class BaseFragmentAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Home", "Social" };
    private Context context;

    public BaseFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return HomePageFragment.newInstance(position + 1);
        }
        /*
        if (position == 1){
            return SocialFragment.newInstance(position + 1);
        }*/
        else{
            return SocialFragment.newInstance(position + 1);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
