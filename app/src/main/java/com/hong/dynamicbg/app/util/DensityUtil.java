package com.hong.dynamicbg.app.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;

public class DensityUtil {

    private DensityUtil() {
        throw new AssertionError();
    }


    /**
     * dp转px
     *
     * @param dpVal
     * @return
     */
    public static int dp2px(float dpVal,Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getDisplayMetrics(context));
    }

    /**
     * dip转px
     *
     * @param dpVal
     * @return
     */
    public static int dip2px(float dpVal,Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getDisplayMetrics(context));
    }

    /**
     * sp转px
     *
     * @param spVal
     * @return
     */
    public static int sp2px(float spVal,Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getDisplayMetrics(context));
    }

    /**
     * px转dp
     *
     * @param pxVal
     * @return
     */
    public static int px2dp(float pxVal,Context context) {
        //Log.d("DensityUtil","density=" + App.sContext.getResources().getDisplayMetrics().density);
        return (int) (pxVal / getDisplayMetrics(context).density + 0.5f);

    }

    /**
     * px转sp
     *
     * @param pxVal
     * @return
     */
    public static int px2sp(float pxVal,Context context) {
        return (int) (pxVal / getDisplayMetrics(context).scaledDensity + 0.5f);
    }


    /**
     * 获取DisplayMetrics
     *
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }


    public static int dp2sp(float dipValue,Context context) {
        return px2sp(dp2px(dipValue,context),context);
    }

    public static void getAndroiodScreenProperty(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)


        Log.d("DensityUtil", "屏幕宽度（像素）：" + width);
        Log.d("DensityUtil", "屏幕高度（像素）：" + height);
        Log.d("DensityUtil", "屏幕密度（0.75 / 1.0 / 1.5）：" + density);
        Log.d("DensityUtil", "屏幕密度dpi（120 / 160 / 240）：" + densityDpi);
        Log.d("DensityUtil", "屏幕宽度（dp）：" + screenWidth);
        Log.d("DensityUtil", "屏幕高度（dp）：" + screenHeight);
    }
}
