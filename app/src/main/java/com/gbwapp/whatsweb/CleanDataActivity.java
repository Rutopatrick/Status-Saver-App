package com.nadinegb.free;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nadinegb.free.adapter.CleanerPagerAdapter;
import com.nadinegb.free.util.AdsManager;

public class CleanDataActivity extends AppCompatActivity {

    ImageView backIV;
    TabLayout tabLayout;
    ViewPager viewPager;
    String category;
    String receivePath;
    String sentPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_data);

        this.category = getIntent().getStringExtra("category");
        this.receivePath = getIntent().getStringExtra("receivePath");
        this.sentPath = getIntent().getStringExtra("sentPath");

        backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        viewPager = findViewById(R.id.pagerdiet);
        viewPager.setAdapter(new CleanerPagerAdapter(getSupportFragmentManager(), category, receivePath, sentPath));
        tabLayout = findViewById(R.id.tab_layoutdiet);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        LinearLayout adContainer = findViewById(R.id.banner_container);
        AdsManager.showBanner(this);
    }

    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Received File");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabtwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabtwo.setText("Sent File");
        tabLayout.getTabAt(1).setCustomView(tabtwo);
    }


}
