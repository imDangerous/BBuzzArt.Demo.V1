package com.bbuzzart.demo.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bbuzzart.demo.BaseBindFragment;
import com.bbuzzart.demo.BuzzApplication;
import com.bbuzzart.demo.BuzzConstants;
import com.bbuzzart.demo.R;
import com.bbuzzart.demo.model.CuratorPicks;
import com.bbuzzart.demo.service.RestApiService;
import com.bbuzzart.demo.ui.widget.CustomLinearLayoutManager;
import com.bbuzzart.demo.ui.widget.FixedHorizonMarginDecoration;
import com.bbuzzart.demo.ui.widget.ItemHorizonMarginDecoration;
import com.bbuzzart.demo.ui.widget.ItemVerticalMarginDecoration;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;

/**
 * A placeholder fragment containing a simple view.
 */
public class CuratorPicksActivityFragment extends BaseBindFragment implements BuzzConstants {

    private static final String TAG = CuratorPicksActivityFragment.class.getSimpleName();

    @BindView(R.id.recyclerViewCurator) RecyclerView recyclerViewCurator;
    @BindView(R.id.recyclerViewHorizontal) RecyclerView recyclerViewHorizontal;
    @BindView(R.id.recyclerViewVertical) RecyclerView recyclerViewVertical;
    @BindView(R.id.initProgressLayout) FrameLayout initProgressLayout;
    @BindView(R.id.nestedScrollView) NestedScrollView nestedScrollView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    private BuzzApplication application;
    private CuratorPicksActivity activity;
    private CuratorPicks curatorPicks;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        this.application = (BuzzApplication) getActivity().getApplication();
        this.activity = (CuratorPicksActivity) getActivity();

        addReceiverRestApiService();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        ViewGroup viewGroup = (ViewGroup) getView();
        if (viewGroup != null) {
            viewGroup.removeAllViewsInLayout();
            View view = onCreateView(getActivity().getLayoutInflater(), viewGroup, null);
            viewGroup.addView(view);
        }

        changeCuratorView();
        changeHorizontalView();
        changeVerticalView();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_default;
    }

    @Override
    protected void initVariables(Bundle savedInstanceState) {
        recyclerViewCurator.setFocusable(false);
        recyclerViewHorizontal.setFocusable(false);
        recyclerViewVertical.setFocusable(false);
        initProgressLayout.requestFocus();


        // init RecyclerView
        recyclerViewCurator.setAdapter(new ViewCuratorListAdapter(application, new ArrayList<CuratorPicks.Response.Datum.Curator>()));
        recyclerViewCurator.setLayoutManager(new GridLayoutManager(application, 1));


        recyclerViewHorizontal.setAdapter(new ViewCuratorPickHorizonAdapter(application, new ArrayList<CuratorPicks.Response.Datum>()));
        recyclerViewHorizontal.setLayoutManager(new CustomLinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewHorizontal.addItemDecoration(new FixedHorizonMarginDecoration());

        recyclerViewVertical.setAdapter(new ViewCuratorPickVerticalAdapter(application, new ArrayList<CuratorPicks.Response.Datum>()));
        recyclerViewVertical.setLayoutManager(new GridLayoutManager(application, 1));


        // init SwipeRefresh
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setColorSchemeResources(R.color.green_500, R.color.red_500, R.color.yellow_500, R.color.blue_500);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                    }
                }, 5000);
                requestCuratorPicks();
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (activity.isOnline() && !application.isKeepCuratorPicks()) {
            Log.w(TAG, "online");

            requestCuratorPicks();
        } else if (activity.isOnline() && application.isKeepCuratorPicks()) {
            Log.w(TAG, "online & kept");
        } else {
            Log.w(TAG, "offline");

            activity.appFinish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        removeReceiverRestApiService();
    }

    private void toggleProgress(boolean show) {
        if (show) {
            initProgressLayout.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.INVISIBLE);
        } else {
            initProgressLayout.setVisibility(View.GONE);
            nestedScrollView.setVisibility(View.VISIBLE);
        }
    }




    private void changeCuratorView() {
        // Curator 정제
        Set<Long> curatorIds = new HashSet<>();
        for (CuratorPicks.Response.Datum d : curatorPicks.getResponse().getData()) {
            curatorIds.add(d.getCurator().getId());
        }

        List<CuratorPicks.Response.Datum.Curator> curators = new ArrayList<>();
        for (Long id : curatorIds) {
            for (CuratorPicks.Response.Datum d : curatorPicks.getResponse().getData()) {
                if (id == d.getCurator().getId()) {
                    curators.add(d.getCurator());
                    break;
                }
            }
        }


        // VERTICAL
        ViewCuratorListAdapter recyclerAdapter = new ViewCuratorListAdapter(application, curators);
        recyclerAdapter.setHasStableIds(false);
        recyclerAdapter.setAdapterListener(new ViewCuratorListAdapter.AdapterListener() {
            @Override
            public void onSelected(long curatorId, int position) {
                // Curator 정제 후 새로운 CuratorPick Data를 만듬
                List<CuratorPicks.Response.Datum> dataByCurator = new ArrayList<>();
                for (CuratorPicks.Response.Datum d : curatorPicks.getResponse().getData()) {
                    if (curatorId == d.getCurator().getId()) {
                        dataByCurator.add(d);
                    }
                }

                Bundle bundle = new Bundle();
                bundle.putParcelable("curatorPicks", Parcels.wrap(dataByCurator));
                bundle.putInt("index", -1); // -1 인 경우는 ProgressView 생략
                startActivity(CuratorDetailActivity.class, bundle);
            }
        });

        if (application.getResources().getBoolean(R.bool.is_landscape)) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(application, 2);
            gridLayoutManager.setAutoMeasureEnabled(true);
            recyclerViewCurator.setLayoutManager(gridLayoutManager);
            recyclerViewCurator.addItemDecoration(new ItemHorizonMarginDecoration(activity, 2));
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(application, 2);
            gridLayoutManager.setAutoMeasureEnabled(true);
            recyclerViewCurator.setLayoutManager(gridLayoutManager);
            recyclerViewCurator.addItemDecoration(new ItemHorizonMarginDecoration(activity, 2));
        }

        recyclerViewCurator.setVisibility(View.VISIBLE);
        recyclerViewCurator.setFocusable(false);
        recyclerViewCurator.setHasFixedSize(false);
        recyclerViewCurator.setItemViewCacheSize(10);
        recyclerViewCurator.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCurator.setNestedScrollingEnabled(false);
        recyclerViewCurator.getItemAnimator().setChangeDuration(0);
        recyclerViewCurator.setAdapter(recyclerAdapter);
    }

    private void changeHorizontalView() {
        // HORIZONTAL
        ViewCuratorPickHorizonAdapter horizonAdapter = new ViewCuratorPickHorizonAdapter(application, curatorPicks.getResponse().getData());
        horizonAdapter.setAdapterListener(new ViewCuratorPickHorizonAdapter.AdapterListener() {
            @Override
            public void onSelected(CuratorPicks.Response.Datum datum, int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("curatorPicks", Parcels.wrap(curatorPicks.getResponse().getData()));
                bundle.putInt("index", position);
                startActivity(CuratorDetailActivity.class, bundle);
            }
        });
        horizonAdapter.setHasStableIds(false);
        recyclerViewHorizontal.setAdapter(horizonAdapter);
        recyclerViewHorizontal.setHasFixedSize(false);
        recyclerViewHorizontal.setLayoutManager(new CustomLinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        // recyclerViewHorizontal.addItemDecoration(new FixedHorizonMarginDecoration());
        recyclerViewHorizontal.setNestedScrollingEnabled(false);
    }

    private void changeVerticalView() {
        // VERTICAL
        ViewCuratorPickVerticalAdapter recyclerAdapter = new ViewCuratorPickVerticalAdapter(
                application, curatorPicks.getResponse().getData());
        recyclerAdapter.setHasStableIds(false);
        recyclerAdapter.setAdapterListener(new ViewCuratorPickVerticalAdapter.AdapterListener() {
            @Override
            public void onSelected(CuratorPicks.Response.Datum datum, int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("curatorPicks", Parcels.wrap(curatorPicks.getResponse().getData()));
                bundle.putInt("index", position);
                startActivity(CuratorDetailActivity.class, bundle);
            }
        });

        if (application.getResources().getBoolean(R.bool.is_landscape)) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(application, 2);
            gridLayoutManager.setAutoMeasureEnabled(true);
            recyclerViewVertical.setLayoutManager(gridLayoutManager);
            recyclerViewVertical.addItemDecoration(new ItemHorizonMarginDecoration(activity, 2));
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(application, 1);
            gridLayoutManager.setAutoMeasureEnabled(true);
            recyclerViewVertical.setLayoutManager(gridLayoutManager);
            recyclerViewVertical.addItemDecoration(new ItemVerticalMarginDecoration(activity));
        }

        recyclerViewVertical.setVisibility(View.VISIBLE);
        recyclerViewVertical.setFocusable(false);
        recyclerViewVertical.setHasFixedSize(false);
        recyclerViewVertical.setItemViewCacheSize(10);
        recyclerViewVertical.setItemAnimator(new DefaultItemAnimator());
        recyclerViewVertical.setNestedScrollingEnabled(false);
        recyclerViewVertical.getItemAnimator().setChangeDuration(0);
        recyclerViewVertical.setAdapter(recyclerAdapter);
    }


    /**
     * Request Rest API
     */
    private void requestCuratorPicks() {
        curatorPicks = new CuratorPicks();

        Intent intent = new Intent(application, RestApiService.class);
        intent.setAction(RestApiService.ACTION_INTENT_REST_API_REQUEST);
        intent.putExtra(RestApiService.EXTRA_REQUEST_URI, RestApiService.API_CURATOR_PICKS);
        intent.putExtra(REQUEST, Parcels.wrap(curatorPicks));
        application.startService(intent);

        // show progress bar
        toggleProgress(true);
    }




    private BroadcastReceiver broadcastReceiver;

    /**
     * remove Broadcast Receiver
     */
    private void removeReceiverRestApiService() {
        try {
            activity.unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
            // NOPE
        }
    }

    /**
     * init Broadcast Receiver
     */
    private void addReceiverRestApiService() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RestApiService.ACTION_INTENT_REST_API_RESPONSE);
        Log.w(TAG, TAG + " addAction: RestApiService.ACTION_INTENT_REST_API_RESPONSE");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                Log.i(TAG, "onReceive: [ACTION=" + action + " / URI=" + intent.getStringExtra(RestApiService.EXTRA_REQUEST_URI) + "]");

                switch (intent.getStringExtra(RestApiService.EXTRA_REQUEST_URI)) {
                    case RestApiService.API_CURATOR_PICKS: {

                        swipeRefreshLayout.setRefreshing(false);
                        toggleProgress(false);

                        curatorPicks = Parcels.unwrap(intent.getParcelableExtra(RESPONSE));
                        // Log.w(TAG, curatorPicks.toString());

                        if (curatorPicks.getResponse().isSuccess()) {
                            Log.w(TAG, "success");

                            application.setKeepCuratorPicks(true);

                            changeCuratorView();
                            changeHorizontalView();
                            changeVerticalView();
                        } else {
                            Log.w(TAG, "fail");

                            application.setKeepCuratorPicks(false);
                        }

                        // RestApi Service 종료
                        activity.stopService(new Intent(application, RestApiService.class));
                        break;
                    }
                    default:
                        break;
                }
            }
        };


        activity.registerReceiver(broadcastReceiver, intentFilter);
    }

}
