package com.nadinegb.free;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.applovin.sdk.AppLovinSdk;
import com.nadinegb.free.util.AdsManager;

public class StartActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        AdsManager.showBanner(this);
        AdsManager.load(this);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, SecondActivity.class);
                AdsManager.showNext(StartActivity.this,intent);
            }
        });
        AppLovinSdk.getInstance( this ).showMediationDebugger();
    }

//
//    private LinearLayout adView;
//    private NativeAd nativeAd;

//
//    public void loadNativeAd(final Activity context, final NativeAdLayout nativeAdLayout) {
//        nativeAd = new NativeAd(context, getResources().getString(R.string.facebook_native));
//
//        NativeAdListener nativeAdListener = new NativeAdListener() {
//            @Override
//            public void onMediaDownloaded(Ad ad) {
//
//            }
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
//
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                // Race condition, load() called again before last ad was displayed
//                if (nativeAd == null || nativeAd != ad) {
//                    return;
//                }
//                // Inflate Native Ad into Container
//                inflateAd(context, nativeAdLayout, nativeAd);
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//        };
//        nativeAd.loadAd(
//                nativeAd.buildLoadAdConfig()
//                        .withAdListener(nativeAdListener)
//                        .build());
//    }
//
//    private void inflateAd(Activity context, NativeAdLayout nativeAdLayout, NativeAd nativeAd) {
//
//        nativeAd.unregisterView();
//
//        // Add the Ad view into the ad container.
//        LayoutInflater inflater = LayoutInflater.from(context);
//        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
//        adView = (LinearLayout) inflater.inflate(R.layout.facebook_native, nativeAdLayout, false);
//        nativeAdLayout.addView(adView);
//
//        // Add the AdOptionsView
//        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
//        AdOptionsView adOptionsView = new AdOptionsView(context, nativeAd, nativeAdLayout);
//        adChoicesContainer.removeAllViews();
//        adChoicesContainer.addView(adOptionsView, 0);
//
//        // Create native UI using the ad metadata.
//        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
//        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
//        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
//        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
//        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
//        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
//        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);
//
//        // Set the Text.
//        nativeAdTitle.setText(nativeAd.getAdvertiserName());
//        nativeAdBody.setText(nativeAd.getAdBodyText());
//        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
//        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
//        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
//        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());
//
//        // Create a list of clickable views
//        List<View> clickableViews = new ArrayList<>();
//        clickableViews.add(nativeAdTitle);
//        clickableViews.add(nativeAdCallToAction);
//
//        // Register the Title and CTA button to listen for clicks.
//        nativeAd.registerViewForInteraction(
//                adView, nativeAdMedia, nativeAdIcon, clickableViews);
//    }

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
}
