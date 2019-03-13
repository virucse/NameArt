package com.imagezoom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.ViewConfiguration;
import android.widget.ImageView;

public class ImageViewTouch extends ImageViewTouchBase {
    private static final String TAG = ImageViewTouch.class.getSimpleName();
    private static final int NONE = 0;
    private static final int ZOOM = 2;
    public float MIN_ZOOM;
    public float mCurrentScaleFactor;
    public int mDoubleTapDirection;
    public boolean mDoubleTapToZoomEnabled;
    public ScaleGestureDetector mScaleDetector;
    public boolean mScaleEnabled;
    public boolean mScrollEnabled;
    protected GestureDetector mGestureDetector;
    protected OnGestureListener mGestureListener;
    protected float mScaleFactor;
    protected OnScaleGestureListener mScaleListener;
    protected int mTouchSlop;
    private float f86d;
    private ImageDoubleTapListener doubleTapListener;
    private float[] lastEvent;
    private ImageLongPressListener longPressListener;
    private boolean mTouchStart;
    private PointF mid;
    private int mode;
    private ImageMoveListener movingLisener;
    private float newRot;
    private float oldDist;
    private Matrix savedMatrix;
    private ImageSigleTapListener singleTapListener;
    private float tempScaleFactor;

    public ImageViewTouch(Context context) {
        super(context);
        this.MIN_ZOOM = 0.9f;
        this.mDoubleTapToZoomEnabled = true;
        this.mScaleEnabled = true;
        this.mScrollEnabled = true;
        this.mid = new PointF();
        this.oldDist = 1.0f;
        this.f86d = 0.0f;
        this.newRot = 0.0f;
        this.lastEvent = null;
        this.savedMatrix = new Matrix();
        this.mode = NONE;
        this.tempScaleFactor = -1f;
    }

    public ImageViewTouch(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.MIN_ZOOM = 0.9f;
        this.mDoubleTapToZoomEnabled = true;
        this.mScaleEnabled = true;
        this.mScrollEnabled = true;
        this.mid = new PointF();
        this.oldDist = 1.0f;
        this.f86d = 0.0f;
        this.newRot = 0.0f;
        this.lastEvent = null;
        this.savedMatrix = new Matrix();
        this.mode = NONE;
        this.tempScaleFactor = -1f;
    }

    public static ImageSigleTapListener access$000(ImageViewTouch imageViewTouch) {
        return imageViewTouch.singleTapListener;
    }

    public static ImageDoubleTapListener access$100(ImageViewTouch imageViewTouch) {
        return imageViewTouch.doubleTapListener;
    }

    public static ImageLongPressListener access$200(ImageViewTouch imageViewTouch) {
        return imageViewTouch.longPressListener;
    }

    public static boolean access$300(ImageViewTouch imageViewTouch) {
        return imageViewTouch.mTouchStart;
    }

    public static boolean access$302(ImageViewTouch imageViewTouch, boolean bl2) {
        imageViewTouch.mTouchStart = bl2;
        return bl2;
    }

    protected void _setImageDrawable(Drawable drawable2, boolean bl2, Matrix matrix, float f2) {
        super._setImageDrawable(drawable2, bl2, matrix, f2);
        this.mScaleFactor = getMaxZoom() / 3.0f;
    }

    public Bitmap getDispalyImage(int n2, int n3) {
        try {
            float f2 = ((float) n2) / ((float) getWidth());
            Bitmap bitmap = Bitmap.createBitmap(n2, n3, Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Matrix matrix = getImageMatrix();
            matrix.postScale(f2, f2);
            Bitmap bitmap2 = ((FastDrawable) getDrawable()).getBitmap();
            Paint paint = new Paint();
            paint.setFilterBitmap(true);
            canvas.drawBitmap(bitmap2, matrix, paint);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean getDoubleTapEnabled() {
        return this.mDoubleTapToZoomEnabled;
    }

    protected OnGestureListener getGestureListener() {
        return new ImageGestureListener(this);
    }

    protected OnScaleGestureListener getScaleListener() {
        return new ImageScaleListener(this);
    }

    protected void init() {
        super.init();
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.mGestureListener = getGestureListener();
        this.mScaleListener = getScaleListener();
        this.mScaleDetector = new ScaleGestureDetector(getContext(), this.mScaleListener);
        this.mGestureDetector = new GestureDetector(getContext(), this.mGestureListener, null, true);
        this.mCurrentScaleFactor = 10.f;
        this.mDoubleTapDirection = 1;
    }

    protected void onBitmapChanged(Drawable object) {
        super.onBitmapChanged(object);
        float[] values = new float[9];
        this.mSuppMatrix.getValues(values);
        this.mCurrentScaleFactor = values[NONE];
    }

    public float onDoubleTapPost(float f2, float f3) {
        if (this.mDoubleTapDirection != 1) {
            this.mDoubleTapDirection = 1;
            return 1.0f;
        } else if ((this.mScaleFactor * 2.0f) + f2 <= f3) {
            return f2 + this.mScaleFactor;
        } else {
            this.mDoubleTapDirection = -1;
            return f3;
        }
    }

    public boolean onTouchEvent1(MotionEvent motionEvent) {
        int n2 = motionEvent.getAction();
        if (n2 == 0) {
            this.mTouchStart = true;
        }
        mScaleDetector.onTouchEvent(motionEvent);
        if (!mScaleDetector.isInProgress()) {
            mGestureDetector.onTouchEvent(motionEvent);
        }
        switch (n2 & 255) {
            case 1 /*1*/:
                if (getScale() < 1.0f) {
                    zoomTo(1.0f, 50.0f);
                    break;
                }
                break;
        }
        if (this.movingLisener != null) {
            this.movingLisener.ImageMovingTouchEvent(motionEvent);
        }
        return true;
    }

    protected void onZoom(float f2) {
        super.onZoom(f2);
        if (!this.mScaleDetector.isInProgress()) {
            this.mCurrentScaleFactor = f2;
        }
    }

    public void setDoubleTapListener(ImageDoubleTapListener air2) {
        this.doubleTapListener = air2;
    }

    public void setDoubleTapToZoomEnabled(boolean bl2) {
        this.mDoubleTapToZoomEnabled = bl2;
    }

    public void setLongPressListener(ImageLongPressListener ais2) {
        this.longPressListener = ais2;
    }

    public void setMinScale(float f2) {
        this.MIN_ZOOM = f2;
    }

    public void setMovingListener(ImageMoveListener re2) {
        this.movingLisener = re2;
    }

    public void setScaleEnabled(boolean bl2) {
        this.mScaleEnabled = bl2;
    }

    public void setScrollEnabled(boolean bl2) {
        this.mScrollEnabled = bl2;
    }

    public void setSingleTapListener(ImageSigleTapListener ait2) {
        this.singleTapListener = ait2;
    }

    public void zoomScaleToFitView(float f2, float f3, float f4) {
        this.mCurrentScaleFactor = Math.min(getMaxZoom(), Math.max(f2, this.MIN_ZOOM));
        zoomTo(f2, f3, f4, 5.0f);
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 0) {
            this.mTouchStart = true;
        }
        mScaleDetector.onTouchEvent(event);
        if (!mScaleDetector.isInProgress()) {
            mGestureDetector.onTouchEvent(event);
        }
        ImageView view = this;
        switch (event.getAction() & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN /*0*/:
                this.savedMatrix.set(this.mSuppMatrix);
                this.tempScaleFactor = this.mCurrentScaleFactor;
                this.mode = NONE;
                this.lastEvent = null;
                break;
            case MotionEvent.ACTION_UP  /*1*/:
                break;
            case MotionEvent.ACTION_POINTER_UP /*6*/:
                this.tempScaleFactor = -1f;
                this.mode = NONE;
                this.lastEvent = null;
                if (getScale() < 1.0f) {
                    zoomTo(1.0f, 50.0f);
                    break;
                }
                break;
            case MotionEvent.ACTION_MOVE /*2*/:
                if (this.mode == ZOOM && this.tempScaleFactor == this.mCurrentScaleFactor) {
                    if (spacing(event) > 10.0f) {
                        this.mSuppMatrix.set(this.savedMatrix);
                    }
                    if (this.lastEvent != null && event.getPointerCount() == ZOOM) {
                        this.newRot = rotation(event);
                        float r = this.newRot - this.f86d;
                        float[] values = new float[9];
                        this.mSuppMatrix.getValues(values);
                        float tx = values[ZOOM];
                        float ty = values[5];
                        float sx = values[NONE];
                        this.mSuppMatrix.postRotate(r, tx + (((float) (getWidth() / ZOOM)) * sx), ty + (((float) (getHeight() / ZOOM)) * sx));
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN /*5*/:
                this.oldDist = spacing(event);
                if (this.oldDist > 10.0f) {
                    this.savedMatrix.set(this.mSuppMatrix);
                    midPoint(this.mid, event);
                    this.mode = ZOOM;
                }
                this.lastEvent = new float[4];
                this.lastEvent[NONE] = event.getX(NONE);
                this.lastEvent[1] = event.getX(1);
                this.lastEvent[ZOOM] = event.getY(NONE);
                this.lastEvent[3] = event.getY(1);
                this.f86d = rotation(event);
                break;
        }
        setImageMatrix(getImageViewMatrix());
        if (this.movingLisener != null) {
            this.movingLisener.ImageMovingTouchEvent(event);
        }
        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    private void midPoint(PointF point, MotionEvent event) {
        point.set((event.getX(0) + event.getX(1)) / 2.0f, (event.getY(0) + event.getY(1)) / 2.0f);
    }

    private float rotation(MotionEvent event) {
        return (float) Math.toDegrees(Math.atan2((double) (event.getY(0) - event.getY(1)), (double) (event.getX(0) - event.getX(1))));
    }
}
