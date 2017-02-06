package com.bbuzzart.demo.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bbuzzart.demo.BaseActivity;
import com.bbuzzart.demo.BuzzApplication;
import com.bbuzzart.demo.R;
import com.bbuzzart.demo.service.RestApiService;

import butterknife.BindView;

/**
 * Created by imDangerous on 03/02/2017.
 */

public final class CuratorPicksActivity extends BaseActivity {

    private static final String TAG = CuratorPicksActivity.class.getSimpleName();


    @BindView(R.id.initProgressLayout)
    FrameLayout initProgressLayout;



    private BuzzApplication application;


    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");

        super.onDestroy();
    }


    @Override
    protected int getLayoutResource() {
        this.application = (BuzzApplication) getApplication();

        return R.layout.activity_default;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }


    private static final long FINISH_INTERVAL_TIME = 1000L;
    private long backPressedTime;
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            appFinish();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(application, getString(R.string.app_finish), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public void appFinish() {
        // TODO quit service
        stopService(new Intent(application, RestApiService.class));

        // TODO cached data release
        application.setKeepCuratorPicks(false);

        finish();
    }

}
