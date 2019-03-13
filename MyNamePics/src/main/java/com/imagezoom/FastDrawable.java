package com.imagezoom;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class FastDrawable extends Drawable {
    protected Bitmap mBitmap;
    protected Paint mPaint;

    public FastDrawable(Bitmap bitmap) {
        this.mBitmap = bitmap;
        this.mPaint = new Paint();
        this.mPaint.setDither(true);
        this.mPaint.setFilterBitmap(true);
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void draw(Canvas canvas) {
        if (this.mBitmap != null && !this.mBitmap.isRecycled()) {
            canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, this.mPaint);
            Log.e("draw", this.mBitmap.toString());
        }
    }

    public int getIntrinsicHeight() {
        return this.mBitmap.getHeight();
    }

    public int getIntrinsicWidth() {
        return this.mBitmap.getWidth();
    }

    public int getMinimumHeight() {
        return this.mBitmap.getHeight();
    }

    public int getMinimumWidth() {
        return this.mBitmap.getWidth();
    }

    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setAlpha(int n2) {
        this.mPaint.setAlpha(n2);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }
}
