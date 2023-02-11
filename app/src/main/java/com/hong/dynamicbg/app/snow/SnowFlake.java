package com.hong.dynamicbg.app.snow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.hong.dynamicbg.app.R;
import com.hong.dynamicbg.app.util.DensityUtil;

/**
 * 雪花
 */
public class SnowFlake {

    private static final String TAG = "SnowFlake";

    public static final int SMALL = 1; //小雪
    public static final int MEDIUM = 2; //中雪
    public static final int BIG = 3;//大雪


    public int startX;
    public int startY;
    public int endX;
    public int endY;
    public float alpha;
    public float scale;
    public int speed;

    public int offsetX, offsetY;
    public int x, y;

    private Context context;

    private Bitmap snowFlakeBmp;

    public int scope;

    private boolean isReachBottom;
    private boolean isDead;

    private boolean isToolbar;

    private float parentAlpha = 1.0f;

    static Drawable drawable;

    public SnowFlake(Context context) {
        setData(context);
    }

    public SnowFlake(Context context, boolean isToolbar) {
        if (isToolbar) {
            this.context = context;
            this.isToolbar = isToolbar;
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            int screenHeight = DensityUtil.dip2px(76,context);

            alpha = (float) Math.floor(Math.random() * 8 + 2) / 10; //随机alpha值,取0.2~1之间
            scale = (float) Math.floor(Math.random() * 5 + 6) / 10; //随机scale值,取0.6~1之间

            startX = dp2px(5) + (int) (Math.random() * (screenWidth - dp2px(10)));
            startY = -dp2px(20);

            offsetX = (int) (Math.random() * dp2px(100)) - dp2px(50);
            offsetY = (int) (screenHeight * 0.7f) + (int) (Math.random() * dp2px(20));

            if (drawable == null){
                drawable = context.getResources().getDrawable(R.drawable.snow);
            }

            int drawableWidth = (int) (drawable.getIntrinsicWidth() * scale);
            int drawableHeight = (int) (drawable.getIntrinsicHeight() * scale);
            drawable.setBounds(0, 0, drawableWidth, drawableHeight);

            Bitmap bitmap = Bitmap.createBitmap(drawableWidth, drawableHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.draw(canvas);

            snowFlakeBmp = bitmap;

            x = startX;
            y = startY;
        } else {
            setData(context);
        }
    }

    private void setData(Context context) {
        this.context = context;
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;

        alpha = (float) Math.floor(Math.random() * 8 + 2) / 10; //随机alpha值,取0.2~1之间
        scale = (float) Math.floor(Math.random() * 5 + 6) / 10; //随机scale值,取0.6~1之间

        startX = dp2px(5) + (int) (Math.random() * (screenWidth - dp2px(10)));
        startY = -dp2px(20);

        offsetX = (int) (Math.random() * dp2px(100)) - dp2px(50);
        offsetY = (int) (screenHeight * 0.7f) + (int) (Math.random() * dp2px(150));

        if (drawable == null){
            drawable = context.getResources().getDrawable(R.drawable.snow);
        }

        int drawableWidth = (int) (drawable.getIntrinsicWidth() * scale);
        int drawableHeight = (int) (drawable.getIntrinsicHeight() * scale);
        drawable.setBounds(0, 0, drawableWidth, drawableHeight);

        Bitmap bitmap = Bitmap.createBitmap(drawableWidth, drawableHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);

        snowFlakeBmp = bitmap;

        x = startX;
        y = startY;
    }

    /**
     * 根据下雪规模，设置雪花速度
     *
     * @param scope
     */
    public void setScope(int scope) {
        switch (scope) {
            case BIG:
                speed = 3;
                break;
            case MEDIUM:
                speed = 5;
                break;
            default:
                speed = 8;
                break;
        }

        init();
    }

    /**
     * 根据下雪规模获取间隔
     *
     * @param scope
     * @return
     */
    public static int getPeroid(int scope) {
        switch (scope) {
            case BIG:
                return 20;
            case MEDIUM:
                return 80;
            default:
                return 330;
        }
    }

    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getDisplayMetrics());
    }

    public DisplayMetrics getDisplayMetrics() {
        return context.getResources().getDisplayMetrics();
    }

    private void init() {
        if (alpha < 0.8) {
            if (alpha < 0.5) {
                speed = 2 * speed;
                endX = startX + offsetX;
                endY = offsetY;
            } else {
                speed = (int) (1.5f * speed);
                endX = startX + offsetX;
                endY = offsetY;
            }

        } else {
            endX = startX + offsetX;
            endY = offsetY;
            if (scope == BIG) {
                endX = startX + offsetX + (int) (endY * Math.tan(15 * Math.PI / 180));
            }

        }

        Log.d(TAG, "init() -->> alpha = " + alpha + " speed = " + speed + " startX = " + startX + " endX = " + endX + " endY = " + endY);

    }

    public void updatePos(long deltaTime) {
        if (deltaTime <= 0) {
            return;
        }

        if (isDead) {
            return;
        }

        int factor = 45;
        if (isToolbar) {
            factor = 60;
        }

        double deltaY = ((double) (deltaTime * speed)) / (double) factor;
        double deltaX = deltaY * (endX - startX) / (double) endY;


        y += (int) deltaY;
        if (y > 0) {
            x = startX + (int) (y * (endX - startX) / (double) endY);
        }

        checkReachBottom();
        checkDead();

        if (isReachBottom) {
            updateBottomAlpha();
        }

        Log.d(TAG, "updatePos() -->> deltaTime = " + deltaTime + " deltaY = " + deltaY + " y = " + y + " x = " + x + " isReachBottom = " + isReachBottom + " alpha = " + alpha + " isDead = " + isDead);

    }

    public void draw(Canvas canvas) {
        if (isDead) {
            return;
        }
        if (snowFlakeBmp != null) {
            Paint paint = new Paint();
            paint.setAlpha((int) (255 * alpha * parentAlpha));
            canvas.drawBitmap(snowFlakeBmp, x, y, paint);
        }
    }

    /**
     * 当前雪花是否触底
     *
     * @return
     */
    private void checkReachBottom() {
        if (y >= (int) (endY * 0.8f)) {
            isReachBottom = true;
        }
    }

    private void checkDead() {
        if (y >= endY) {
            isDead = true;
        }
    }


    private void updateBottomAlpha() {
        int tmpY = (int) (endY * 0.2f);

        int disY = y - (int) (endY * 0.8f);

        float ratio = ((float) disY) / tmpY;

        alpha = alpha - alpha * ratio;
    }

    public boolean isDead() {
        return isDead;
    }


    public void setAlpha(float alpha) {
        this.parentAlpha = alpha;
    }
}
