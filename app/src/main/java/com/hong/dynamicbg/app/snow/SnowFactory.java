package com.hong.dynamicbg.app.snow;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SnowFactory {

    private static final String TAG = "SnowFlake";

    private ArrayList<SnowFlake> snowFlakes;

    private int scope = SnowFlake.SMALL;

    private static final int SNOW_NUM = 200;

    private Context context;

    private Object lockObject;

    int perroid;

    private Timer timer;

    private boolean isToolbar;

    int num;

    private float alpha = 1.0f;

    public SnowFactory(Context context, boolean isToolbar) {
        this(context);
        this.isToolbar = isToolbar;
    }

    public SnowFactory(Context context) {
        this.context = context;
        lockObject = new Object();
        perroid = SnowFlake.getPeroid(scope);

        snowFlakes = new ArrayList<>();

        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                addSnowFlake();
                num++;
            }
        };
        timer.schedule(timerTask, 1000, perroid);
    }

    private void removeSnowFlake(SnowFlake snowFlake) {
        if (snowFlake == null) {
            return;
        }
        if (!snowFlakes.contains(snowFlake)) {
            return;
        }

        synchronized (lockObject) {
            snowFlakes.remove(snowFlake);
        }
    }

    private void addSnowFlake() {
        Log.d(TAG, "addSnowFlake() -->> size = " + snowFlakes.size());
        if (snowFlakes.size() > SNOW_NUM) {
            return;
        }

        SnowFlake snowFlake = new SnowFlake(context, isToolbar);
        snowFlake.setScope(scope);
        synchronized (lockObject) {
            snowFlakes.add(snowFlake);
        }
    }

    private void checkDead() {
        if (snowFlakes.size() > 0) {
            synchronized (lockObject) {
                for (int i = snowFlakes.size() - 1; i >= 0; i--) {
                    if (snowFlakes.get(i).isDead()) {
                        snowFlakes.remove(i);
                    }
                }
            }
        }
    }

    public void updatePos(long delayTime) {
        Log.d(TAG, "SnowFactory  updatePos() -->> delayTime = " + delayTime);

        checkDead();

        synchronized (lockObject) {
            for (SnowFlake snowFlake : snowFlakes) {
                snowFlake.updatePos(delayTime);
                snowFlake.setAlpha(alpha);
            }
        }
    }

    public void draw(Canvas canvas) {
        synchronized (lockObject) {
            for (SnowFlake snowFlake : snowFlakes) {
                snowFlake.draw(canvas);
            }
        }
    }

    public void clear() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (snowFlakes.size() > 0) {
            snowFlakes.clear();
        }
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}
