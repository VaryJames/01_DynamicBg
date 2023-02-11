package com.hong.dynamicbg.app.snow;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.TextureView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.hong.dynamicbg.app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SnowActivity extends Activity {

    private static final String TAG = "SnowFlake";

    TextureView snowTextureView;

    AtomicBoolean isAvailable;

    SnowDrawThread snowDrawThread;

    SnowFactory snowFactory;

    Handler mHandler;

    ListView listView;

    List<String> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.snow_activity);

        snowFactory = new SnowFactory(this);

        FrameLayout rootView = (FrameLayout) findViewById(R.id.root_view);
        listView = findViewById(R.id.listView);

        snowTextureView = new TextureView(this);
        snowTextureView.setOpaque(false);
        snowTextureView.setSurfaceTextureListener(mListener);

        isAvailable = new AtomicBoolean(false);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        rootView.addView(snowTextureView, layoutParams);

        mHandler = new Handler();

        dataList = new ArrayList<>();
        for (int i = 1; i <= 80; i++) {
            String itemStr = "第" + i + "项";
            dataList.add(itemStr);
        }

        ListAdapter listAdapter = new ArrayAdapter<>(this, R.layout.simple_list_item_1,dataList);
        listView.setAdapter(listAdapter);

    }


    TextureView.SurfaceTextureListener mListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            isAvailable.set(true);
            Log.d(TAG, "onSurfaceTextureAvailable() -->> ");

            snowDrawThread = new SnowDrawThread(snowFactory, snowTextureView);
            snowDrawThread.start();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            isAvailable.set(false);
            snowDrawThread.stopThread();
            snowFactory.clear();
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    @Override
    protected void onDestroy() {
        snowDrawThread.stopThread();
        snowFactory.clear();
        super.onDestroy();
    }
}
