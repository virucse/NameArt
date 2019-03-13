package nonworkingcode.grid.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;

import nonworkingcode.grid.custominterface.CollageInterface;
import nonworkingcode.grid.custominterface.ICollageClickListener;
import nonworkingcode.grid.util.CollageConst;
import nonworkingcode.grid.util.ColorFilterGenerator;

/**
 * Created by Caliber Fashion on 12/9/2016.
 */

public abstract class BaseCollageView extends RelativeLayout implements CollageInterface, View.OnTouchListener {

    public int mBackgroundColor;
    protected boolean mIsEditable;
    protected RelativeLayout mBackLayer, mShapeLayer, mTopLayer, mFrameLayer;
    protected GestureDetector gestureDetector;
    int f82j;
    int f83k;
    int f84l;
    int f85m;
    private ICollageClickListener mClickListener;
    private AnimatorSet mInAnimationSet;
    private AnimatorSet mOutAnimationSet;
    private AlphaAnimation mAlphaAnimation;
    private float mHueValue;
    private ImageView mImgTopLayer;

    public BaseCollageView(Context context) {
        super(context);
        this.mClickListener = null;
        this.mInAnimationSet = new AnimatorSet();
        this.mOutAnimationSet = new AnimatorSet();
        mAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        mBackgroundColor = -1;
        this.f82j = 0;
        this.f83k = 0;
        this.mIsEditable = true;
        this.mHueValue = 0.0f;
        this.mClickListener = (ICollageClickListener) context;
        init();
    }

    @SuppressLint({"NewApi"})
    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }
    //**************************************BaseCollageView.init() ended************************

    public static void setBackground(View view, int resid) {
        view.setBackgroundResource(resid);
    }

    //**************************************BaseCollageView.init() started************************
    private void init() {
        setOnTouchListener(this);
        mBackLayer = new RelativeLayout(getContext());
        mImgTopLayer = new ImageView(getContext());
        mShapeLayer = new RelativeLayout(getContext());
        mTopLayer = new RelativeLayout(getContext());
        mFrameLayer = new RelativeLayout(getContext());

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mBackLayer.setLayoutParams(layoutParams);
        addView(mBackLayer);

        mImgTopLayer.setLayoutParams(layoutParams);
        addView(mImgTopLayer);

        mShapeLayer.setLayoutParams(layoutParams);
        addView(this.mShapeLayer);

        this.mFrameLayer.setLayoutParams(layoutParams);
        addView(this.mFrameLayer);

        this.mTopLayer.setLayoutParams(layoutParams);
        addView(this.mTopLayer);

        gestureDetector = new GestureDetector(new GestureListener());
        if (CollageConst.backgroundDrawable == null) {
            CollageConst.backgroundDrawable = new ColorDrawable(-1);
        }
        this.mAlphaAnimation.setDuration(800);
        this.mAlphaAnimation.setFillAfter(true);
        this.mAlphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Drawable drawable = BaseCollageView.this.mBackLayer.getBackground();
                BaseCollageView.this.setBackgroudBottom(null);
                if (drawable instanceof BitmapDrawable) {
                    Bitmap var2 = ((BitmapDrawable) drawable).getBitmap();
                    if (!(CollageConst.backgroundDrawable instanceof BitmapDrawable)) {
                        BaseCollageView.this.recycleDrawable(drawable);
                    } else if (var2 != ((BitmapDrawable) CollageConst.backgroundDrawable).getBitmap()) {
                        BaseCollageView.this.recycleDrawable(drawable);
                    }
                }
                BaseCollageView.this.setBackgroudBottom(CollageConst.backgroundDrawable);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void setBackgroudBottom(Drawable drawable) {
        setBackground(mBackLayer, drawable);
    }

    @Override
    public int m41b(Point point) {
        return -1;
    }

    @Override
    public Bitmap getDrawingBitmap(int desiredWidth, int desiredHeight) {
        invalidate();
        float scaleX = ((float) desiredWidth) / ((float) getWidth());
        float scaleY = ((float) desiredHeight) / ((float) getHeight());
        Bitmap bitmap = Bitmap.createBitmap(desiredWidth, desiredHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(scaleX, scaleY);
        draw(canvas);
        //bitmap.recycle();
        destroyDrawingCache();
        buildDrawingCache();
        return getDrawingCache();
    }

    @Override
    public int getImageListSize() {
        return 0;
    }

    @Override
    public int getPositionAtPoint(Point point) {
        return 0;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public abstract View getViewAtPosition(int i);

    @Override
    public void handleImage() {
        if (this.mImgTopLayer == null || this.mImgTopLayer.getBackground() == null) {
            this.mImgTopLayer.setColorFilter(ColorFilterGenerator.adjustHue(this.mHueValue));
            this.mImgTopLayer.invalidate();
            return;
        }
        this.mImgTopLayer.getBackground().setColorFilter(ColorFilterGenerator.adjustHue(this.mHueValue));
        this.mImgTopLayer.invalidate();
    }

    @Override
    public void recycleBackgroud() {
        setBackgroudBottom(null);
        setBackgroudTop(null);
        recycleDrawable(CollageConst.backgroundDrawable);
        CollageConst.backgroundDrawable = null;
    }

    private void setBackgroudTop(Drawable drawable) {
        Drawable d = this.mImgTopLayer.getDrawable();
        this.mImgTopLayer.setImageDrawable(null);
        recycleDrawable(d);
        setBackground(this.mImgTopLayer, drawable);
    }

    @Override
    public void recycleDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitamp = ((BitmapDrawable) drawable).getBitmap();
            if (bitamp != null && !bitamp.isRecycled()) {
                //bitamp.recycle();
            }
        }
    }

    @Override
    public void resetBitmap(Bitmap bitmap, int i) {

    }

    @Override
    public void setBackgroundTopColor(int color) {
        if (Build.VERSION.SDK_INT < 11) {
            this.mImgTopLayer.setBackgroundColor(color);
        } else {
            animatorColor(color);
        }
    }

    @TargetApi(11)
    private void animatorColor(int color) {
        ObjectAnimator animator = ObjectAnimator.ofInt(this.mImgTopLayer, "backgroundColor", new int[]{this.mBackgroundColor, color});
        animator.setDuration(200);
        animator.setEvaluator(new ArgbEvaluator());
        animator.start();
    }

    @Override
    public void setCornerRadious(float f) {

    }

    @Override
    public void setCustomBorderId(int index, File frameFile) {
        if (index >= 0 && frameFile != null) {
            try {
                setBackground(this.mTopLayer, Drawable.createFromPath(frameFile.getAbsolutePath()));
                CollageConst.frameBorderAssetId = index;
                CollageConst.frameAssetsPath = frameFile;
                return;
            } catch (Exception var3) {
                var3.printStackTrace();
                return;
            }
        }
        setBackground(this.mTopLayer, null);
        CollageConst.frameBorderAssetId = index;
    }

    @Override
    public void setEditableCell(boolean editable) {
        this.mIsEditable = editable;
        if (editable) {
            setSelectedAtPosition(false, 0);
        }
        postInvalidate();
    }

    @Override
    public void setGridNumber(int i) {

    }

    @Override
    public void setHueValue(float f) {
        this.mHueValue = f;
    }

    @Override
    public void setLayoutParams(LayoutParams layoutParams) {
        super.setLayoutParams(layoutParams);
    }

    @Override
    public void setLineThickness(float f) {

    }

    @Override
    public void setSelectedAtPosition(boolean z, int i) {

    }

    @Override
    public void setShadowSize(float f) {

    }

    @Override
    public void setSquareBackground(Drawable drawable) {
        CollageConst.backgroundDrawable = drawable;
        setBackgroudTop(CollageConst.backgroundDrawable);
        this.mImgTopLayer.startAnimation(this.mAlphaAnimation);
    }

    @Override
    public void update(Bitmap bitmap, int i) {

    }

    @Override
    public void updateRatio(int i, int i2) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public void m55b(MotionEvent event) {
        //Log.e("here we go", "Base Collage touch cancelled");
    }

    void m53a(MotionEvent var1) {
    }

    public int m54b(Point paint) {
        return -1;
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (!BaseCollageView.this.mIsEditable) {
                return false;
            }
            Point point = new Point((int) e.getX(), (int) e.getY());
            final int index = BaseCollageView.this.getPositionAtPoint(point);
            if (index >= 0 && CollageConst.collageBitmaps.length > index) {
                if (CollageConst.collageBitmaps[index] != null) {
                    BaseCollageView.this.mClickListener.onCollageItemTapAt(index, point);
                } else if (BaseCollageView.this.mInAnimationSet.isRunning() || BaseCollageView.this.mOutAnimationSet.isRunning()) {
                    return false;
                } else {
                    View var5 = BaseCollageView.this.getViewAtPosition(index);
                    BaseCollageView.this.mInAnimationSet = new AnimatorSet();
                    BaseCollageView.this.mOutAnimationSet = new AnimatorSet();
                    ObjectAnimator scaleX = ObjectAnimator.ofFloat(var5, "scaleX", new float[]{0.7f});
                    ObjectAnimator scaleY = ObjectAnimator.ofFloat(var5, "scaleY", new float[]{0.7f});
                    ObjectAnimator scaleX1 = ObjectAnimator.ofFloat(var5, "scaleX", new float[]{1.0f});
                    ObjectAnimator scaleY1 = ObjectAnimator.ofFloat(var5, "scaleY", new float[]{1.0f});
                    BaseCollageView.this.mInAnimationSet.setDuration(50);
                    BaseCollageView.this.mInAnimationSet.play(scaleX).with(scaleY);
                    BaseCollageView.this.mInAnimationSet.start();
                    BaseCollageView.this.mOutAnimationSet.play(scaleX1).with(scaleY1);
                    BaseCollageView.this.mOutAnimationSet.setStartDelay(50);
                    BaseCollageView.this.mOutAnimationSet.setDuration(50);
                    BaseCollageView.this.mOutAnimationSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            BaseCollageView.this.mClickListener.onCollageItemClickAt(index);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                    BaseCollageView.this.mOutAnimationSet.start();
                }
            }
            return super.onSingleTapConfirmed(e);
        }
    }
}
