package com.hong.dynamicbg.app.snow;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.TextureView;

public class SnowDrawThread extends Thread {

    private static final String TAG = "SnowFlake";
    private boolean isRunning = false;

    private static final int DRAW_INTERVAL = 30;

    private Canvas canvas;

    private SnowFactory factory;

    TextureView textureView;

    public SnowDrawThread(SnowFactory factory, TextureView textureView) {
        setRunning(true);
        this.factory = factory;
        this.textureView = textureView;
    }

    public void stopThread() {
        if (!isRunning()) {
            return;
        }
        setRunning(false);
    }

    private synchronized void setRunning(boolean isRunning){
        this.isRunning = isRunning;
    }

    private synchronized boolean isRunning(){
        return this.isRunning;
    }

    @Override
    public void run() {
        long deltaTime = 0;
        long tickTime = System.currentTimeMillis();

        Log.d(TAG,"SnowDrawThread run() -->> abcd");

        while (isRunning()) {

            try {
                synchronized (textureView) {
                    canvas = textureView.lockCanvas();
                    //canvas.drawColor(Color.BLACK);
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    factory.updatePos(DRAW_INTERVAL);
                    factory.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (textureView != null && canvas != null) {
                    textureView.unlockCanvasAndPost(canvas);
                }
            }

            deltaTime = System.currentTimeMillis() - tickTime;

            if (deltaTime < DRAW_INTERVAL) {
                try {
                    Thread.sleep(DRAW_INTERVAL - deltaTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            tickTime = System.currentTimeMillis();
        }

        try {
            synchronized (textureView) {
                canvas = textureView.lockCanvas();
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (textureView != null && canvas != null) {
                textureView.unlockCanvasAndPost(canvas);
            }
        }


    }
}
