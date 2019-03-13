package com.imagezoom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class ImageViewTouchBase extends AppCompatImageView {
    public static final String LOG_TAG = "image";
    protected final float MAX_ZOOM;
    protected final Matrix mDisplayMatrix;
    protected final float[] mMatrixValues;
    protected Matrix mBaseMatrix;
    protected RectF mBitmapRect;
    protected RectF mCenterRect;
    protected IEasing mEasing;
    protected boolean mFitToScreen;
    protected Handler mHandler;
    protected float mMaxZoom;
    protected Runnable mOnLayoutRunnable;
    protected RectF mScrollRect;
    protected Matrix mSuppMatrix;
    protected int mThisHeight;
    protected int mThisWidth;
    int mBmpHeight;
    int mBmpWidth;
    private PointF mImageCenterPointF;
    private DrawableChangeListener mListener;
    private boolean mNeedSetCenter;
    private PointF mNewImageCenter;

    public ImageViewTouchBase(Context context) {
        super(context);
        this.MAX_ZOOM = 10.0f;
        this.mBaseMatrix = new Matrix();
        this.mBitmapRect = new RectF();
        this.mBmpHeight = 0;
        this.mBmpWidth = 0;
        this.mCenterRect = new RectF();
        this.mDisplayMatrix = new Matrix();
        this.mEasing = new Easing();
        this.mFitToScreen = false;
        this.mHandler = new Handler();
        this.mImageCenterPointF = new PointF();
        this.mMatrixValues = new float[9];
        this.mNeedSetCenter = false;
        this.mNewImageCenter = new PointF();
        this.mOnLayoutRunnable = null;
        this.mScrollRect = new RectF();
        this.mSuppMatrix = new Matrix();
        this.mThisHeight = -1;
        this.mThisWidth = -1;
        init();
    }

    public ImageViewTouchBase(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.MAX_ZOOM = 10.0f;
        this.mBaseMatrix = new Matrix();
        this.mBitmapRect = new RectF();
        this.mBmpHeight = 0;
        this.mBmpWidth = 0;
        this.mCenterRect = new RectF();
        this.mDisplayMatrix = new Matrix();
        this.mEasing = new Easing();
        this.mFitToScreen = false;
        this.mHandler = new Handler();
        this.mImageCenterPointF = new PointF();
        this.mMatrixValues = new float[9];
        this.mNeedSetCenter = false;
        this.mNewImageCenter = new PointF();
        this.mOnLayoutRunnable = null;
        this.mScrollRect = new RectF();
        this.mSuppMatrix = new Matrix();
        this.mThisHeight = -1;
        this.mThisWidth = -1;
        init();
    }

    private float getCurScale() {
        float[] arrf = new float[9];
        getImageViewMatrix().getValues(arrf);
        return arrf[0];
    }

    private void resetCenter() {
        float[] arrf = new float[9];
        getImageViewMatrix().getValues(arrf);
        float f2 = arrf[0];
        float f3 = arrf[4];
        float f4 = arrf[2];
        float f5 = arrf[5];
        float f6 = (float) this.mThisWidth;
        PointF p1 = new PointF(f6 / 2.0f, ((float) this.mThisHeight) / 2.0f);
        PointF p2 = new PointF(p1.x - f4, p1.y - f5);
        PointF pointF = new PointF(p2.x / f2, p2.y / f3);
        pointF = new PointF(this.mNewImageCenter.x - pointF.x, this.mNewImageCenter.y - pointF.y);
        PointF p5 = new PointF(pointF.x * f2, pointF.y * f3);
        panBy((double) (-p5.x), (double) (-p5.y));
        this.mImageCenterPointF = this.mNewImageCenter;
    }

    protected void _setImageDrawable(Drawable drawable2, boolean bl2, Matrix matrix, float f2) {
        if (drawable2 != null) {
            if (this.mFitToScreen) {
                getProperBaseMatrix2(drawable2, this.mBaseMatrix);
            } else {
                getProperBaseMatrix(drawable2, this.mBaseMatrix);
            }
            super.setImageDrawable(drawable2);
        } else {
            this.mBaseMatrix.reset();
            super.setImageDrawable(null);
        }
        if (bl2) {
            this.mSuppMatrix.reset();
            if (matrix != null) {
                this.mSuppMatrix = new Matrix(matrix);
            }
        }
        setImageMatrix(getImageViewMatrix());
        if (f2 < 1.0f) {
            f2 = maxZoom();
        }
        this.mMaxZoom = f2;
        onBitmapChanged(drawable2);
    }

    protected void center(boolean bl2, boolean bl3) {
        if (getDrawable() != null) {
            RectF rectF = getCenter(bl2, bl3);
            if (rectF.left != 0.0f || rectF.top != 0.0f) {
                postTranslate(rectF.left, rectF.top);
            }
        }
    }

    public void clear() {
        setImageBitmap(null, true);
    }

    public void dispose() {
        clear();
    }

    protected RectF getBitmapRect() {
        Drawable drawable2 = getDrawable();
        if (drawable2 == null) {
            return null;
        }
        Matrix matrix = getImageViewMatrix();
        this.mBitmapRect.set(0.0f, 0.0f, (float) drawable2.getIntrinsicWidth(), (float) drawable2.getIntrinsicHeight());
        matrix.mapRect(this.mBitmapRect);
        return this.mBitmapRect;
    }

    protected RectF getCenter(boolean horizontal, boolean vertical) {
        if (getDrawable() == null) {
            return new RectF(0.0f, 0.0f, 0.0f, 0.0f);
        }
        this.mCenterRect.set(0.0f, 0.0f, 0.0f, 0.0f);
        RectF rect = getBitmapRect();
        float height = rect.height();
        float width = rect.width();
        float deltaX = 0.0f;
        float deltaY = 0.0f;
        if (vertical) {
            if (height < ((float) getHeight())) {
                deltaY = ((((float) getHeight()) - height) / 2.0f) - rect.top;
            } else if (rect.top > 0.0f) {
                deltaY = -(rect.top - 0.0f);
            } else if (rect.bottom < ((float) getHeight())) {
                deltaY = ((float) getHeight()) - rect.bottom;
            }
        }
        if (horizontal) {
            if (width < ((float) getWidth())) {
                deltaX = ((((float) getWidth()) - width) / 2.0f) - rect.left;
            } else if (rect.left > 0.0f) {
                deltaX = -(rect.left - 0.0f);
            } else if (rect.right < ((float) getWidth())) {
                deltaX = ((float) getWidth()) - rect.right;
            }
        }
        this.mCenterRect.set(deltaX, deltaY, 0.0f, 0.0f);
        return this.mCenterRect;
    }

    public RectF getCenterRectF() {
        return this.mCenterRect;
    }

    public Matrix getDisplayMatrix() {
        return new Matrix(this.mSuppMatrix);
    }

    public PointF getImageCenterPointF() {
        return this.mImageCenterPointF;
    }

    public Matrix getImageViewMatrix() {
        this.mDisplayMatrix.set(this.mBaseMatrix);
        this.mDisplayMatrix.postConcat(this.mSuppMatrix);
        return this.mDisplayMatrix;
    }

    public float getMaxZoom() {
        return this.mMaxZoom;
    }

    protected void getProperBaseMatrix(Drawable drawable2, Matrix matrix) {
        float f2 = (float) getWidth();
        float f3 = (float) getHeight();
        float f4 = (float) drawable2.getIntrinsicWidth();
        float f5 = (float) drawable2.getIntrinsicHeight();
        matrix.reset();
        if (f4 > f2 || f5 > f3) {
            float f6 = Math.min(Math.min(f2 / f4, 2.0f), Math.min(f3 / f5, 2.0f));
            matrix.postScale(f6, f6);
            matrix.postTranslate((f2 - (f4 * f6)) / 2.0f, (f3 - (f5 * f6)) / 2.0f);
            return;
        }
        matrix.postTranslate((f2 - f4) / 2.0f, (f3 - f5) / 2.0f);
    }

    protected void getProperBaseMatrix2(Drawable drawable2, Matrix matrix) {
        float f2 = (float) getWidth();
        float f3 = (float) getHeight();
        float f4 = (float) drawable2.getIntrinsicWidth();
        float f5 = (float) drawable2.getIntrinsicHeight();
        matrix.reset();
        float f6 = Math.max(Math.min(f2 / f4, 10.0f), Math.min(f3 / f5, 10.0f));
        matrix.postScale(f6, f6);
        matrix.postTranslate((f2 - (f4 * f6)) / 2.0f, (f3 - (f5 * f6)) / 2.0f);
    }

    public float getRotation() {
        return 0.0f;
    }

    public float getScale() {
        return getScale(this.mSuppMatrix);
    }

    protected float getScale(Matrix matrix) {
        return getValue(matrix, 0);
    }

    protected float getValue(Matrix matrix, int n2) {
        matrix.getValues(this.mMatrixValues);
        return this.mMatrixValues[n2];
    }

    protected void init() {
        try {
            if (VERSION.SDK_INT > 10) {
                setLayerType(1, null);
            }
        } catch (Exception e) {
        }
        setScaleType(ScaleType.MATRIX);
    }

    protected float maxZoom() {
        Drawable drawable2 = getDrawable();
        if (drawable2 == null) {
            return 1.0f;
        }
        return Math.max(((float) drawable2.getIntrinsicWidth()) / ((float) this.mThisWidth), ((float) drawable2.getIntrinsicHeight()) / ((float) this.mThisHeight)) * 10.0f;
    }

    protected void onBitmapChanged(Drawable drawable2) {
        if (this.mListener != null) {
            this.mListener.onDrawableChange(drawable2);
        }
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.mThisWidth = right - left;
        this.mThisHeight = bottom - top;
        Runnable runnable = this.mOnLayoutRunnable;
        if (runnable != null) {
            this.mOnLayoutRunnable = null;
            runnable.run();
        }
        if (getDrawable() != null) {
            if (this.mFitToScreen) {
                getProperBaseMatrix2(getDrawable(), this.mBaseMatrix);
            } else {
                getProperBaseMatrix(getDrawable(), this.mBaseMatrix);
            }
            setImageMatrix(getImageViewMatrix());
            if (this.mNeedSetCenter) {
                resetCenter();
                this.mNeedSetCenter = false;
            }
        }
    }

    protected void onZoom(float f2) {
    }

    protected void panBy(double d2, double d3) {
        if (!this.mNeedSetCenter) {
            float f2 = getCurScale();
            PointF p = this.mImageCenterPointF;
            p.x = (float) (((double) p.x) - (d2 / ((double) f2)));
            p = this.mImageCenterPointF;
            p.y = (float) (((double) p.y) - (d3 / ((double) f2)));
        }
        RectF rectF = getBitmapRect();
        this.mScrollRect.set((float) d2, (float) d3, 0.0f, 0.0f);
        updateRect(rectF, this.mScrollRect);
        postTranslate(this.mScrollRect.left, this.mScrollRect.top);
        center(true, true);
    }

    protected void postScale(float f2, float f3, float f4) {
        this.mSuppMatrix.postScale(f2, f2, f3, f4);
        setImageMatrix(getImageViewMatrix());
    }

    protected void postTranslate(float f2, float f3) {
        this.mSuppMatrix.postTranslate(f2, f3);
        setImageMatrix(getImageViewMatrix());
    }

    public void resetDisplayMatrix() {
        this.mSuppMatrix.reset();
    }

    public void scrollBy(float f2, float f3) {
        panBy((double) f2, (double) f3);
    }

    public void scrollBy(float f2, float f3, double d2) {
        double d3 = (double) f2;
        double d4 = (double) f3;
        this.mHandler.post(new C01711(d2, System.currentTimeMillis(), d3, d4));
    }

    public void setFitToScreen(boolean fitToScreen) {
        if (fitToScreen != this.mFitToScreen) {
            this.mFitToScreen = fitToScreen;
            requestLayout();
        }
    }

    public void setImageBitmap(Bitmap bitmap) {
        setImageBitmap(bitmap, true);
    }

    public void setImageBitmap(Bitmap bitmap, boolean reset) {
        setImageBitmap(bitmap, reset, null);
    }

    public void setImageBitmap(Bitmap bitmap, boolean reset, Matrix matrix) {
        setImageBitmap(bitmap, reset, matrix, -1f);
    }

    public void setImageBitmap(Bitmap bitmap, boolean reset, Matrix matrix, float f2) {
        if (bitmap != null) {
            setImageDrawable(new FastDrawable(bitmap), reset, matrix, f2);
        } else {
            setImageDrawable(null, reset, matrix, f2);
        }
    }

    public void setImageCenterPoint(PointF pointF, int n2, int n3) {
        this.mNeedSetCenter = true;
        this.mNewImageCenter = pointF;
        this.mBmpHeight = n3;
        this.mBmpWidth = n2;
        requestLayout();
    }

    public void setImageDrawable(Drawable drawable2) {
        setImageDrawable(drawable2, true, null, -1f);
    }

    public void setImageDrawable(Drawable drawable2, boolean reset, Matrix matrix, float f2) {
        if (getWidth() <= 0) {
            this.mOnLayoutRunnable = new C01722(drawable2, reset, matrix, f2);
        } else {
            _setImageDrawable(drawable2, reset, matrix, f2);
        }
    }

    public void setImageResource(int n2) {
        setImageDrawable(ContextCompat.getDrawable(getContext(), n2));
    }

    public void setOnBitmapChangedListener(DrawableChangeListener aiv2) {
        this.mListener = aiv2;
    }

    protected void updateRect(RectF rectF, RectF rectF2) {
        float f2 = (float) getWidth();
        float f3 = (float) getHeight();
        if (rectF != null && rectF2 != null) {
            if (rectF.top >= 0.0f && rectF.bottom <= f3) {
                rectF2.top = 0.0f;
            }
            if (rectF.left >= 0.0f && rectF.right <= f2) {
                rectF2.left = 0.0f;
            }
            if (rectF.top + rectF2.top >= 0.0f && rectF.bottom > f3) {
                rectF2.top = (float) ((int) (0.0f - rectF.top));
            }
            if (rectF.bottom + rectF2.top <= f3 - 0.0f && rectF.top < 0.0f) {
                rectF2.top = (float) ((int) ((f3 - 0.0f) - rectF.bottom));
            }
            if (rectF.left + rectF2.left >= 0.0f) {
                rectF2.left = (float) ((int) (0.0f - rectF.left));
            }
            if (rectF.right + rectF2.left <= f2 - 0.0f) {
                rectF2.left = (float) ((int) ((f2 - 0.0f) - rectF.right));
            }
        }
    }

    protected void zoomTo(float f2) {
        zoomTo(f2, ((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f);
    }

    public void zoomTo(float f2, float f3) {
        zoomTo(f2, ((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f, f3);
    }

    public void zoomTo(float f2, float f3, float f4) {
        float f5 = f2;
        if (f2 > this.mMaxZoom) {
            f5 = this.mMaxZoom;
        }
        postScale(f5 / getScale(), f3, f4);
        onZoom(getScale());
        center(true, true);
    }

    public void zoomTo(float f2, float f3, float f4, float f5) {
        float f = (f2 - getScale()) / f5;
        this.mHandler.post(new C01733(f5, System.currentTimeMillis(), getScale(), f, f3, f4));
    }

    /* renamed from: com.beautify.pip.imagezoom.ImageViewTouchBase.1 */
    class C01711 implements Runnable {
        private final /* synthetic */ double val$d2;
        private final /* synthetic */ double val$d3;
        private final /* synthetic */ double val$d4;
        private final /* synthetic */ long val$l2;
        double f68a;
        double f69b;

        C01711(double d, long j, double d2, double d3) {
            this.val$d2 = d;
            this.val$l2 = j;
            this.val$d3 = d2;
            this.val$d4 = d3;
        }

        public void run() {
            double d22 = Math.min(this.val$d2, (double) (System.currentTimeMillis() - this.val$l2));
            double d32 = ImageViewTouchBase.this.mEasing.m40a(d22, 0.0d, this.val$d3, this.val$d2);
            double d42 = ImageViewTouchBase.this.mEasing.m40a(d22, 0.0d, this.val$d4, this.val$d2);
            ImageViewTouchBase.this.panBy(d32 - this.f68a, d42 - this.f69b);
            this.f68a = d32;
            this.f69b = d42;
            if (d22 < this.val$d2) {
                ImageViewTouchBase.this.mHandler.post(this);
                return;
            }
            RectF rectF = ImageViewTouchBase.this.getCenter(true, true);
            if (rectF.left != 0.0f || rectF.top != 0.0f) {
                ImageViewTouchBase.this.scrollBy(rectF.left, rectF.top);
            }
        }
    }

    /* renamed from: com.beautify.pip.imagezoom.ImageViewTouchBase.2 */
    class C01722 implements Runnable {
        private final /* synthetic */ boolean val$bl2;
        private final /* synthetic */ Drawable val$drawable2;
        private final /* synthetic */ float val$f2;
        private final /* synthetic */ Matrix val$matrix;

        C01722(Drawable drawable, boolean z, Matrix matrix, float f) {
            this.val$drawable2 = drawable;
            this.val$bl2 = z;
            this.val$matrix = matrix;
            this.val$f2 = f;
        }

        public void run() {
            ImageViewTouchBase.this.setImageDrawable(this.val$drawable2, this.val$bl2, this.val$matrix, this.val$f2);
        }
    }

    /* renamed from: com.beautify.pip.imagezoom.ImageViewTouchBase.3 */
    class C01733 implements Runnable {
        private final /* synthetic */ float val$f;
        private final /* synthetic */ float val$f3;
        private final /* synthetic */ float val$f4;
        private final /* synthetic */ float val$f5;
        private final /* synthetic */ float val$f6;
        private final /* synthetic */ long val$l2;

        C01733(float f, long j, float f2, float f3, float f4, float f5) {
            this.val$f5 = f;
            this.val$l2 = j;
            this.val$f6 = f2;
            this.val$f = f3;
            this.val$f3 = f4;
            this.val$f4 = f5;
        }

        public void run() {
            float f22 = Math.min(this.val$f5, (float) (System.currentTimeMillis() - this.val$l2));
            ImageViewTouchBase.this.zoomTo((this.val$f * f22) + this.val$f6, this.val$f3, this.val$f4);
            if (f22 < this.val$f5) {
                ImageViewTouchBase.this.mHandler.post(this);
            }
        }
    }
}
