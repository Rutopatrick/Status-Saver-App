package com.nadinegb.free;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.nadinegb.free.util.SharedPrefs;

public class SplashActivity extends AppCompatActivity {

    ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppCompatDelegate.setDefaultNightMode(SharedPrefs.getAppNightDayMode(this));

        icon = findViewById(R.id.icon);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, StartActivity.class));
                finish();
//                MyApplication.getInstance().showFacebookInterstitial();
            }
        }, 4000);
    }
}
