package com.bbuzzart.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.bbuzzart.demo.BuzzConstants;
import com.bbuzzart.demo.model.CuratorPicks;
import com.navercorp.volleyextensions.volleyer.Volleyer;
import com.navercorp.volleyextensions.volleyer.builder.GetBuilder;
import com.navercorp.volleyextensions.volleyer.builder.PostBuilder;
import com.navercorp.volleyextensions.volleyer.factory.DefaultRequestQueueFactory;
import com.navercorp.volleyextensions.volleyer.response.parser.Jackson2NetworkResponseParser;

import org.parceler.Parcels;

/**
 * Created by imDangerous on 03/02/2017.
 */

public final class RestApiService extends Service implements BuzzConstants {

    private static final String TAG = RestApiService.class.getSimpleName();

    public static final String SERVER_ROOT = "https://api4.bbuzzart.com";

    public static final String API_CURATOR_PICKS = "/picks";


    public static final String ACTION_INTENT_REST_API_REQUEST = "com.bbuzzart.intent.REST_API_REQUEST";
    public static final String ACTION_INTENT_REST_API_RESPONSE = "com.bbuzzart.intent.REST_API_RESPONSE";


    public static final String EXTRA_REQUEST_URI = "request_uri";


    // private BuzzApplication application;
    private RequestQueue rq;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // application = (BuzzApplication) getApplication();

        rq = DefaultRequestQueueFactory.create(getApplicationContext());
        rq.start();
        Volleyer.volleyer(rq).settings().setAsDefault().done();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (rq != null) {
            rq.stop();
            rq = null;
        }

        Log.e(TAG, "onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action;
        if (intent == null || (action = intent.getAction()) == null) {
            stopSelf();
            return START_NOT_STICKY;
        }

        if (action.equals(ACTION_INTENT_REST_API_REQUEST)) {
            switch (intent.getStringExtra(EXTRA_REQUEST_URI)) {
                case API_CURATOR_PICKS: {
                    getCuratorPicks((CuratorPicks) Parcels.unwrap(intent.getParcelableExtra(REQUEST)));
                    break;
                }

                default:
                    break;
            }
        }


        return START_NOT_STICKY;

    }



    private PostBuilder buildRequestMessage(String url) {
        return Volleyer.volleyer().post(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                // .addHeader("content-Type", "application/json; charset=utf-8")
                .addHeader("Accept-Encoding", "gzip")
                .addHeader("Content-Transfer-Encoding", "UTF-8")
                .addHeader("charset", "UTF-8");
    }

    private GetBuilder buildGetRequestMessage(String url) {
        return Volleyer.volleyer().get(url)
                //.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("content-Type", "application/json; charset=UTF-8")
                .addHeader("Accept-Encoding", "gzip")
                .addHeader("Content-Transfer-Encoding", "UTF-8")
                .addHeader("charset", "UTF-8");
    }




    private void getCuratorPicks(final CuratorPicks curatorPicks) {
        buildGetRequestMessage(SERVER_ROOT + API_CURATOR_PICKS)
                .withTargetClass(CuratorPicks.Response.class)
                .withListener(new Response.Listener<CuratorPicks.Response>() {
                    @Override
                    public void onResponse(CuratorPicks.Response response) {
                        curatorPicks.setResponse(response);
                        // Log.w(TAG, "CuratorPicks.Response == " + curatorPicks.getResponse().toString());

                        Intent intent = new Intent();
                        intent.setAction(ACTION_INTENT_REST_API_RESPONSE);
                        intent.putExtra(EXTRA_REQUEST_URI, API_CURATOR_PICKS);
                        intent.putExtra(RESPONSE, Parcels.wrap(curatorPicks));
                        sendBroadcast(intent);
                    }
                })
                .withErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null)
                            Log.e(TAG, error.toString());
                        else
                            Log.e(TAG, "Unknown Error");


                        CuratorPicks.Response response = new CuratorPicks.Response();
                        response.setSuccess(false);
                        if (error != null)
                            response.setMessage(error.toString());
                        else
                            response.setMessage(E9999);


                        Intent intent = new Intent();
                        intent.setAction(ACTION_INTENT_REST_API_RESPONSE);
                        intent.putExtra(EXTRA_REQUEST_URI, API_CURATOR_PICKS);
                        intent.putExtra(RESPONSE, Parcels.wrap(curatorPicks));
                        sendBroadcast(intent);
                    }
                })
                .withResponseParser(new Jackson2NetworkResponseParser())
                .execute().setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                Log.d(TAG, "10000");
                return 10000;
            }

            @Override
            public int getCurrentRetryCount() {
                Log.d(TAG, "MAX 2");
                return 2;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                Log.d(TAG, "retry");


                if (error != null)
                    Log.e(TAG, error.toString());
                else
                    Log.e(TAG, E9999);

                CuratorPicks.Response response = new CuratorPicks.Response();
                response.setSuccess(false);
                response.setMessage(E9001);

                Intent intent = new Intent();
                intent.setAction(ACTION_INTENT_REST_API_RESPONSE);
                intent.putExtra(EXTRA_REQUEST_URI, API_CURATOR_PICKS);
                intent.putExtra(RESPONSE, Parcels.wrap(curatorPicks));
                sendBroadcast(intent);

            }
        });
    }


}
