package com.nadinegb.free.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nadinegb.free.fragments.CleanListFragment;

public class CleanerPagerAdapter extends FragmentPagerAdapter {
    String category;
    String receivedPath;
    String sentPath;

    public CleanerPagerAdapter(FragmentManager fm, String category, String receivedPath, String sentPath) {
        super(fm);
        this.category = category;
        this.receivedPath = receivedPath;
        this.sentPath = sentPath;
    }

    public Fragment getItem(int position) {
        if (position != 1) {
            return CleanListFragment.newInstance(this.category, this.receivedPath);
        }
        return CleanListFragment.newInstance(this.category, this.sentPath);
    }

    public int getCount() {
        return 2;
    }
}
