package com.test.kingofclick;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

/**
 * Created by Staff2 on 18.05.2018.
 */

public class VideoAdvertising implements RewardedVideoAdListener {
    private Context context;
    private String ad_id;
    private RewardedVideoAd mRewardedVideoAd;
    private boolean isVideoOver=false;
    private OnVideoEndListener onVideoEndListener;


    public VideoAdvertising(Context context,String ad_id) {
        this.context = context;
        this.ad_id = ad_id;
        MobileAds.initialize(context, String.valueOf(R.string.ads_id));
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
    }
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());

    }
    public void show(OnVideoEndListener onVideoEndListener){
        if (mRewardedVideoAd.isLoaded()) {
            this.onVideoEndListener=onVideoEndListener;
            mRewardedVideoAd.show();
        }else{
            Toast.makeText(context,"Уже нужна помощь? Давай еще чутка сам",Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isLoaded(){
        return mRewardedVideoAd.isLoaded();

    }



    @Override
    public void onRewardedVideoAdLoaded() {
        Log.w("VIDEO","loaded Video");
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Log.w("VIDEO","open Video");
    }

    @Override
    public void onRewardedVideoStarted() {
        Log.w("VIDEO","start Video");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Log.w("VIDEO","close Video");
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        onVideoEndListener.onVideoEnd();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Log.w("VIDEO","left Video");
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Log.w("VIDEO","cod = "+i);
    }

    public boolean isVideoOver() {
        return isVideoOver;
    }

    public void setVideoOver(boolean videoOver) {
        isVideoOver = videoOver;
    }
}
