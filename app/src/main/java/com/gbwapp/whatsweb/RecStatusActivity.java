package com.nadinegb.free;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nadinegb.free.fragments.RecentWapp;
import com.nadinegb.free.fragments.RecentWappBus;
import com.nadinegb.free.util.AdsManager;

import java.util.ArrayList;
import java.util.List;

public class RecStatusActivity extends AppCompatActivity {

    ImageView backIV;
    TextView topTV;

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_status);

        backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        topTV = findViewById(R.id.topTV);


        viewPager = findViewById(R.id.pagerdiet);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tab_layoutdiet);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();


        AdsManager.showBanner(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Whatsapp");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabtwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabtwo.setText("WA Business");
        tabLayout.getTabAt(1).setCustomView(tabtwo);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(
                getSupportFragmentManager());

        adapter.addFragment(new RecentWapp(), "Whatsapp");
        adapter.addFragment(new RecentWappBus(), "WA Business");

        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return this.mFragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return this.mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.mFragmentTitleList.get(position);
        }
    }
}
