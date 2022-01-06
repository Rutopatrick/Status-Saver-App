package com.nadinegb.free.util;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.nadinegb.free.R;


public class AdsManager {

    static MaxAdView adView;
    private static MaxInterstitialAd interstitialAdAPL;

    private static boolean isAdsAvailable =  false;

    public static void showBanner(Activity activity){

        if (activity.getResources().getString(R.string.bannerID_applovin) != null) {

            final LinearLayout adContainer = activity.findViewById(R.id.banner_container);
            adContainer.setVisibility(View.GONE);
            adView = new MaxAdView(activity.getResources().getString(R.string.bannerID_applovin), activity);
            adView.setListener(new MaxAdViewAdListener() {
                @Override
                public void onAdExpanded(MaxAd ad) {

                }

                @Override
                public void onAdCollapsed(MaxAd ad) {

                }

                @Override
                public void onAdLoaded(MaxAd ad) {
                    adContainer.setVisibility(View.VISIBLE);

                }

                @Override
                public void onAdDisplayed(MaxAd ad) {

                }

                @Override
                public void onAdHidden(MaxAd ad) {

                }

                @Override
                public void onAdClicked(MaxAd ad) {

                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {

                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                }
            });
            adContainer.addView(adView);
            adView.loadAd();

        }

    }


    public static void load(final Activity activity) {

        if(!isAdsAvailable){
            if (activity.getResources().getString(R.string.intersID_applovin) != null) {
                interstitialAdAPL = new MaxInterstitialAd(activity.getResources().getString(R.string.intersID_applovin)
                        , activity);
                interstitialAdAPL.setListener(new MaxAdViewAdListener() {
                    @Override
                    public void onAdExpanded(MaxAd ad) {

                    }

                    @Override
                    public void onAdCollapsed(MaxAd ad) {


                    }


                    @Override
                    public void onAdLoaded(MaxAd ad) {
                        isAdsAvailable =  true;
                    }

                    @Override
                    public void onAdDisplayed(MaxAd ad) {

                    }

                    @Override
                    public void onAdHidden(MaxAd ad) {


                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, MaxError error) {

                        isAdsAvailable =  false;
                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                    }
                });
                interstitialAdAPL.loadAd();
            }

        }

    }

    public static void showNext(final Activity activity, final Intent mClass) {

        if (interstitialAdAPL.isReady() && isAdsAvailable) {
            interstitialAdAPL.showAd();
            interstitialAdAPL.setListener(new MaxAdViewAdListener() {
                @Override
                public void onAdExpanded(MaxAd ad) {

                }

                @Override
                public void onAdCollapsed(MaxAd ad) {


                }


                @Override
                public void onAdLoaded(MaxAd ad) {

                }

                @Override
                public void onAdDisplayed(MaxAd ad) {

                }

                @Override
                public void onAdHidden(MaxAd ad) {
                    activity.startActivity(mClass);
                    isAdsAvailable =  false;
                    load(activity);
                }

                @Override
                public void onAdClicked(MaxAd ad) {

                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {

                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                }
            });
        }
        else {
            activity.startActivity(mClass);
            isAdsAvailable =  false;
            load(activity);
        }
    }

    public static void showNextFinish(final Activity activity, final Intent mClass) {

        if (interstitialAdAPL.isReady() && isAdsAvailable) {
            interstitialAdAPL.showAd();
            interstitialAdAPL.setListener(new MaxAdViewAdListener() {
                @Override
                public void onAdExpanded(MaxAd ad) {

                }

                @Override
                public void onAdCollapsed(MaxAd ad) {


                }


                @Override
                public void onAdLoaded(MaxAd ad) {

                }

                @Override
                public void onAdDisplayed(MaxAd ad) {

                }

                @Override
                public void onAdHidden(MaxAd ad) {
                    activity.startActivity(mClass);
                    isAdsAvailable =  false;
                    load(activity);
                    activity.finish();
                }

                @Override
                public void onAdClicked(MaxAd ad) {

                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {

                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                }
            });
        }
        else {
            activity.startActivity(mClass);
            isAdsAvailable =  false;
            load(activity);
            activity.finish();
        }
    }

    public static void showONLY(final Activity activity) {

        if (interstitialAdAPL.isReady() && isAdsAvailable) {
            interstitialAdAPL.showAd();
            interstitialAdAPL.setListener(new MaxAdViewAdListener() {
                @Override
                public void onAdExpanded(MaxAd ad) {

                }

                @Override
                public void onAdCollapsed(MaxAd ad) {


                }


                @Override
                public void onAdLoaded(MaxAd ad) {

                }

                @Override
                public void onAdDisplayed(MaxAd ad) {

                }

                @Override
                public void onAdHidden(MaxAd ad) {

                    isAdsAvailable =  false;
                    load(activity);

                }

                @Override
                public void onAdClicked(MaxAd ad) {

                }

                @Override
                public void onAdLoadFailed(String adUnitId, MaxError error) {



                }

                @Override
                public void onAdDisplayFailed(MaxAd ad, MaxError error) {



                }
            });
        }
        else {

            isAdsAvailable =  false;
            load(activity);
        }
    }


    public static void load_and_Show_interstitialAD(final Activity activity, final Intent mClass) {

        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Please wait...");
        dialog.show();

        if (activity.getResources().getString(R.string.intersID_applovin) != null) {
            interstitialAdAPL = new MaxInterstitialAd(activity.getResources().getString(R.string.intersID_applovin)
                    , activity);
            interstitialAdAPL.loadAd();
        }

        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                dialog.dismiss();
                if (interstitialAdAPL.isReady()) {
                    interstitialAdAPL.showAd();
                    interstitialAdAPL.setListener(new MaxAdViewAdListener() {
                        @Override
                        public void onAdExpanded(MaxAd ad) {

                        }

                        @Override
                        public void onAdCollapsed(MaxAd ad) {


                        }


                        @Override
                        public void onAdLoaded(MaxAd ad) {

                        }

                        @Override
                        public void onAdDisplayed(MaxAd ad) {

                        }

                        @Override
                        public void onAdHidden(MaxAd ad) {
                            activity.startActivity(mClass);

                        }

                        @Override
                        public void onAdClicked(MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(String adUnitId, MaxError error) {

                            activity.startActivity(mClass);


                        }

                        @Override
                        public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                            activity.startActivity(mClass);

                        }
                    });
                }
                else {
                    activity.startActivity(mClass);
                }


            }
        }.start();

    }


}

