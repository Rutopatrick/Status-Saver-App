package com.nadinegb.free;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nadinegb.free.util.AdsManager;

public class SecondActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        AdsManager.load(this);
        AdsManager.showBanner(this);

        findViewById(R.id.btn_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, SecondActivity.class);
                AdsManager.showNextFinish(SecondActivity.this,intent);

            }
        });

        findViewById(R.id.btn_messenger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                if (appInstalledOrNot("com.whatsapp")) {
                    //MyApplication.getInstance().showAdmobInterstitial(SecondActivity.this);

                    try {
                        startActivity(launchIntentForPackage);


                    } catch (ActivityNotFoundException unused) {

                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")));
                    }
                } else {
                    Toast.makeText(SecondActivity.this, "Whatsapp not install in your device!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                AdsManager.showNextFinish(SecondActivity.this,intent);
            }
        });

        findViewById(R.id.btn_gb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, SecondActivity.class);
                AdsManager.showNextFinish(SecondActivity.this,intent);

            }
        });

        //NativeAdLayout native_ad_container = (NativeAdLayout) findViewById(R.id.native_ad_container);

    }

    @SuppressLint("WrongConstant")
    public boolean appInstalledOrNot(String str) {
        try {
            getPackageManager().getPackageInfo(str, 1);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            AdsManager.showONLY(this);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        AdsManager.load(this);
    }
}
