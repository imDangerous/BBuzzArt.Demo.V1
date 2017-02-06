package com.bbuzzart.demo;

import android.content.res.Configuration;
import android.os.SystemClock;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by imDangerous on 03/02/2017.
 */

public class BuzzApplication extends MultiDexApplication {

    /**
     * Curator Picks 데이터를 가지고 있는지 여부 확인
     */
    @Getter @Setter
    private boolean keepCuratorPicks;

    @Override
    public void onCreate() {
        super.onCreate();

        // Setup XML Image
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        setKeepCuratorPicks(false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }



    /** 일시적 사용자 클릭 시간 저장 */
    private long lastClickTime = 0;

    /**
     * Prevent Duplicate Click
     *
     * @return true: 가능, false: 이미 실행중
     */
    public boolean isClickEnabled() {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            Log.d("APP", "preventClick - " + (SystemClock.elapsedRealtime() - lastClickTime));
            return false;
        } else {
            Log.d("APP", "preventClick -> 1000");
        }

        lastClickTime = SystemClock.elapsedRealtime();
        return true;
    }
}
