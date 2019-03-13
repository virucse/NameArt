package nonworkingcode.fcrop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.formationapps.nameart.R;

import java.util.ArrayList;
import java.util.List;

import nonworkingcode.fcrop.activity.FeatureCutActivity;

public class FeatureCropView extends View implements OnTouchListener {
    public static List<CropPoint> mCropPointList;
    Path mPath;
    int f26c;
    boolean f27d;
    CropPoint f28e;
    boolean f29f;
    CropPoint f30g;
    Bitmap mBitmap;
    Context mContext;
    private Paint mPaint;
    private int f34k;
    private int f35l;

    public FeatureCropView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mPath = new Path();
        this.f26c = 2;
        this.f27d = true;
        this.f28e = null;
        this.f29f = false;
        this.f30g = null;
        init(context);
    }

    public FeatureCropView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mPath = new Path();
        this.f26c = 2;
        this.f27d = true;
        this.f28e = null;
        this.f29f = false;
        this.f30g = null;
        init(context);
    }

    public FeatureCropView(Context context) {
        super(context);
        this.mPath = new Path();
        this.f26c = 2;
        this.f27d = true;
        this.f28e = null;
        this.f29f = false;
        this.f30g = null;
        init(context);
    }

    private static boolean m24a(CropPoint CropPoint, CropPoint CropPoint2) {
        int i = (int) (CropPoint2.y - 3.0f);
        int i2 = (int) (CropPoint2.x + 3.0f);
        int i3 = (int) (CropPoint2.y + 3.0f);
        if (((float) ((int) (CropPoint2.x - 3.0f))) >= CropPoint.x || CropPoint.x >= ((float) i2) || ((float) i) >= CropPoint.y || CropPoint.y >= ((float) i3) || mCropPointList.size() < 10) {
            return false;
        }
        return true;
    }

    private void init(Context context) {
        this.mContext = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        setOnTouchListener(this);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setPathEffect(new DashPathEffect(new float[]{10.0f, 20.0f}, 0.0f));
        this.mPaint.setStrokeWidth(5.0f);
        this.mPaint.setColor(-1);
        mCropPointList = new ArrayList();
        this.f29f = false;
    }

    public final void m26a(Bitmap bitmap) {
        if (bitmap != null) {
            this.f34k = bitmap.getWidth();
            this.f35l = bitmap.getHeight();
            this.mBitmap = bitmap;
            invalidate();
        }
    }

    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(this.f34k, this.f35l);
    }

    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, null);
        int i = 0;
        Object obj = 1;
        while (i < mCropPointList.size()) {
            Object obj2;
            CropPoint CropPoint = (CropPoint) mCropPointList.get(i);
            if (obj != null) {
                this.mPath.moveTo(CropPoint.x, CropPoint.y);
                obj2 = null;
            } else if (i < mCropPointList.size() - 1) {
                CropPoint CropPoint2 = (CropPoint) mCropPointList.get(i + 1);
                this.mPath.quadTo(CropPoint.x, CropPoint.y, CropPoint2.x, CropPoint2.y);
                obj2 = obj;
            } else {
                this.f30g = (CropPoint) mCropPointList.get(i);
                this.mPath.lineTo(CropPoint.x, CropPoint.y);
                obj2 = obj;
            }
            i += 2;
            obj = obj2;
        }
        canvas.drawPath(this.mPath, this.mPaint);
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        CropPoint CropPoint = new CropPoint(this);
        CropPoint.x = (float) ((int) motionEvent.getX());
        CropPoint.y = (float) ((int) motionEvent.getY());
        if (this.f27d) {
            if (!this.f29f) {
                mCropPointList.add(CropPoint);
            } else if (m24a(this.f28e, CropPoint)) {
                mCropPointList.add(this.f28e);
                this.f27d = false;
                FeatureCutActivity.getInstance().findViewById(R.id.optionLyt).setVisibility(VISIBLE);
            } else {
                mCropPointList.add(CropPoint);
            }
            if (!this.f29f) {
                this.f28e = CropPoint;
                this.f29f = true;
            }
        }
        invalidate();
        switch (action) {
            case 1 /*1*/:
            case 3 /*3*/:
                this.f30g = CropPoint;
                if (this.f27d && mCropPointList.size() > 12 && !m24a(this.f28e, this.f30g)) {
                    this.f27d = false;
                    mCropPointList.add(this.f28e);
                    FeatureCutActivity.getInstance().findViewById(R.id.optionLyt).setVisibility(VISIBLE);
                }
                invalidate();
                break;
        }
        return true;
    }

    public final void m25a() {
        mCropPointList.clear();
        this.mPaint.setColor(-1);
        this.mPaint.setStyle(Style.STROKE);
        this.f27d = true;
        this.f28e = null;
        this.f30g = null;
        this.f29f = false;
        this.mPath.reset();
        invalidate();
    }

    public class CropPoint {
        final FeatureCropView f23c;
        public float x;
        public float y;

        CropPoint(FeatureCropView featureCropView) {
            this.f23c = featureCropView;
        }

        public final String toString() {
            return this.x + ", " + this.y;
        }
    }
}
