package com.nadinegb.free;

import android.app.Application;

import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.onesignal.OneSignal;

public class MyApplication extends Application {
   // public static MyApplication intance = null;
   // public static MyApplication getInstance() {
   //     return intance;
   //}
    //private InterstitialAd interstitialAd;




    @Override
    public void onCreate() {
        super.onCreate();
        //intance = this;
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        AdSettings.addTestDevice("c8d68f9c-6e4e-4529-8883-a9fa81515752");
        AudienceNetworkAds.initialize(this);

        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(AppLovinSdkConfiguration config) {

            }
        });


    }
}
