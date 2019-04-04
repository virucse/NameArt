package com.removebg;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import android.support.annotation.Nullable;


public class MagnifyView extends AppCompatImageView {
    private Paint bPaint;
    private Bitmap bit2;
    int bitmappx;
    private int brushSize;
    int canvaspx;
    private Context context;
    private boolean isRectEnable = false;
    private boolean needToDraw = false;
    private boolean onLeft = false;
    private int screenWidth;
    private Paint shaderPaint = null;

    public MagnifyView(Context context) {
        super(context);
        initVariables(context);
    }

    public MagnifyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initVariables(context);
    }

    public MagnifyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVariables(context);
    }

    public void initVariables(Context context) {
        this.context = context;
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        this.screenWidth = displaymetrics.widthPixels;
        this.bitmappx = BgEraserImageUtils.dpToPx(context, 150);
        this.canvaspx = BgEraserImageUtils.dpToPx(context, 75);
        //this.bit2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.circle1);
        //this.bit2 = Bitmap.createScaledBitmap(this.bit2, this.bitmappx, this.bitmappx, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.needToDraw && this.shaderPaint != null) {
            if(bit2!=null){
                if (this.onLeft) {
                    //canvas.drawBitmap(BgRemoverActivity.bgCircleBit, 0.0f, 0.0f, null);
                    canvas.drawCircle((float) this.canvaspx, (float) this.canvaspx, (float) this.canvaspx, this.shaderPaint);
                    if (this.isRectEnable) {
                        canvas.drawRect((float) (this.canvaspx - this.brushSize), (float) (this.canvaspx - this.brushSize), (float) (this.canvaspx + this.brushSize), (float) (this.canvaspx + this.brushSize), this.bPaint);
                    } else {
                        canvas.drawCircle((float) this.canvaspx, (float) this.canvaspx, (float) this.brushSize, this.bPaint);
                    }

                    canvas.drawBitmap(this.bit2, 0.0f, 0.0f, null);
                    return;
                }
                //canvas.drawBitmap(BgRemoverActivity.bgCircleBit, (float) (this.screenWidth - this.bitmappx), 0.0f, null);
                canvas.drawCircle((float) (this.screenWidth - this.canvaspx), (float) this.canvaspx, (float) this.canvaspx, this.shaderPaint);
                if (this.isRectEnable) {
                    canvas.drawRect((float) ((this.screenWidth - this.canvaspx) - this.brushSize), (float) (this.canvaspx - this.brushSize), (float) ((this.screenWidth - this.canvaspx) + this.brushSize), (float) (this.canvaspx + this.brushSize), this.bPaint);
                } else {
                    canvas.drawCircle((float) (this.screenWidth - this.canvaspx), (float) this.canvaspx, (float) this.brushSize, this.bPaint);
                }
                canvas.drawBitmap(this.bit2, (float) (this.screenWidth - this.bitmappx), 0.0f, null);
            }
        }
    }

    public void updateShaderView(Paint sp, Paint bPaint, int brushSize, boolean needToDraw, boolean onLeft, boolean isRectBrushEnable) {
        /*this.needToDraw = needToDraw;
        this.onLeft = onLeft;
        this.isRectEnable = isRectBrushEnable;
        this.shaderPaint = sp;
        this.bPaint = bPaint;
        this.brushSize = brushSize;
        invalidate();*/
    }
}
