package com.bbuzzart.demo.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bbuzzart.demo.BaseActivity;
import com.bbuzzart.demo.BuzzApplication;
import com.bbuzzart.demo.R;
import com.bbuzzart.demo.model.CuratorPicks;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import lombok.Getter;

/**
 * Created by imDangerous on 03/02/2017.
 */

public class CuratorDetailActivity extends BaseActivity {

    private static final String TAG = CuratorDetailActivity.class.getSimpleName();


    private class PagerAdapter extends FragmentStatePagerAdapter {
        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return CuratorDetailActivityFragment.create(data.get(position));
        }

        @Override
        public int getCount() {
            return data.size();
        }
    }



    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.initProgressLayout) FrameLayout initProgressLayout;
    @BindView(R.id.navView) FrameLayout navView;


    private BuzzApplication application;
    private List<CuratorPicks.Response.Datum> data;
    @Getter
    private int curatorPicksIndex;


    @OnClick(R.id.rippleLike) void rippleLike() {
        CuratorPicks.Response.Datum datum = data.get(viewPager.getCurrentItem());

        Toast.makeText(application, "Like Touched [" + datum.getWork().getTitle() + "]", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.rippleBookmark) void rippleBookmark() {
        CuratorPicks.Response.Datum datum = data.get(viewPager.getCurrentItem());

        Toast.makeText(application, "Bookmark Touched [" + datum.getWork().getTitle() + "]", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.rippleFullscreen) void rippleFullscreen() {
        CuratorPicks.Response.Datum datum = data.get(viewPager.getCurrentItem());

        Toast.makeText(application, "Fullscreen Touched [" + datum.getWork().getTitle() + "]", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Log.d(TAG, "onConfigurationChanged...");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "back button click");

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy");

        super.onDestroy();
    }

    @Override
    protected int getLayoutResource() {
        this.application = (BuzzApplication) getApplication();

        return R.layout.activity_detail;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (getIntent() != null) {
            toggleProgress(true);
            data = Parcels.unwrap(getIntent().getParcelableExtra("curatorPicks"));
            curatorPicksIndex = getIntent().getIntExtra("index", 0);

            final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
            viewPager.setOffscreenPageLimit(data.size());
            viewPager.setAdapter(pagerAdapter);


            if (curatorPicksIndex == -1) {
                toggleProgress(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        navView.setVisibility(View.GONE);
                    }
                }, 2500);
            } else {
                viewPager.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (curatorPicksIndex != -1)
                            viewPager.setCurrentItem(curatorPicksIndex, true);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toggleProgress(false);
                            }
                        }, 1500);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                navView.setVisibility(View.GONE);
                            }
                        }, 3000);
                    }
                }, 500);
            }

        } else {
            Toast.makeText(application, "[There's no data]", Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    private void toggleProgress(boolean show) {
        if (show) {
            initProgressLayout.setVisibility(View.VISIBLE);
        } else {
            initProgressLayout.setVisibility(View.GONE);
        }
    }


}
