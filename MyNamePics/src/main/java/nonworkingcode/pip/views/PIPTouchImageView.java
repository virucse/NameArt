package nonworkingcode.pip.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import com.imagezoom.FastDrawable;
import com.imagezoom.ImageViewTouch;

/**
 * Created by Caliber Fashion on 12/15/2016.
 */

public class PIPTouchImageView extends ImageViewTouch {
    private static final String TAG = PIPTouchImageView.class.getSimpleName();
    private Shader mBmpShader;
    private RectF mFrame;
    private Bitmap mMask;
    private Paint mPaint;

    public PIPTouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initI();
    }

    public PIPTouchImageView(Context context) {
        super(context);
        initI();
    }

    private void initI() {
        mFrame = new RectF();
    }

    private Shader createShader(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        return new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
    }

    public Bitmap getDispalyImage(int n2, int n3) {
        float f2 = ((float) n2) / ((float) getWidth());
        Bitmap bitmap = Bitmap.createBitmap(n2, n3, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Matrix matrix = new Matrix(getImageViewMatrix());
        matrix.postScale(f2, f2, getCenterRectF().left, getCenterRectF().top);
        canvas.drawARGB(0, 0, 0, 0);
        Matrix matrix2 = new Matrix();
        this.mBmpShader.getLocalMatrix(matrix2);
        if (this.mMask != null) {
            f2 = ((float) n2) / ((float) this.mMask.getWidth());
            matrix.preScale(1.0f / f2, 1.0f / f2);
            canvas.scale(f2, f2);
            if (!(this.mMask == null || this.mMask.isRecycled())) {
                canvas.drawBitmap(this.mMask, 0.0f, 0.0f, this.mPaint);
            }
        } else {
            Bitmap bitmap2 = ((FastDrawable) getDrawable()).getBitmap();
            Paint paint = new Paint();
            paint.setFilterBitmap(true);
            if (!(bitmap2 == null || bitmap2.isRecycled())) {
                canvas.drawBitmap(bitmap2, matrix, paint);
            }
        }
        this.mBmpShader.setLocalMatrix(matrix2);
        return bitmap;
    }

    public Bitmap getMask() {
        return this.mMask;
    }

    public void setMask(Bitmap bitmap) {
        if (!(this.mMask == null || this.mMask.isRecycled())) {
            this.mMask.recycle();
            this.mMask = null;
        }
        this.mMask = bitmap;
    }

    protected void init() {
        super.init();
        setFitToScreen(true);
        this.mPaint = new Paint();
        this.mPaint.setFilterBitmap(true);
    }

    @SuppressLint({"DrawAllocation"})
    @Override
    protected void onDraw(Canvas canvas) {
        if (mMask == null || mBmpShader == null) {
            super.onDraw(canvas);
            return;
        }
        canvas.save();
        Matrix matrix = new Matrix(getImageViewMatrix());
        float f2 = mFrame.width() / ((float) mMask.getWidth());
        canvas.scale(f2, f2);
        matrix.postScale(1.0f / f2, 1.0f / f2);
        mBmpShader.setLocalMatrix(matrix);
        if (!(mMask == null || mMask.isRecycled())) {
            canvas.drawBitmap(mMask, 0.0f, 0.0f, mPaint);
            Log.d(TAG, "onDraw: Round corner view Width:" + getWidth() + " Height:" + getHeight());
        }
        canvas.restore();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mFrame.set(0.0f, 0.0f, (float) (right - left), (float) (bottom - top));
    }

    @Override
    protected void onBitmapChanged(Drawable drawable2) {
        if (drawable2 == null) {
            mBmpShader = null;
            return;
        }
        mBmpShader = createShader(((FastDrawable) drawable2).getBitmap());
        mPaint.setShader(mBmpShader);
        super.onBitmapChanged(drawable2);
    }
}
