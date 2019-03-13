package nonworkingcode.grid.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import nonworkingcode.grid.util.CollageConst;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_UP;
import static android.view.MotionEvent.ACTION_UP;

public class StylishImgView extends AppCompatImageView implements OnTouchListener {
    private final float[] f28e;
    public FancyCollageGrid grid;
    String f26a;
    private float f27c;
    private int height;
    private Boolean isScaled;
    private boolean isSelected;
    private Boolean isTouching;
    private Boolean isValid;
    private int f29j;
    private int f30k;
    private float f31l;
    private int left;
    private float f32m;
    private Matrix matrix;
    private float f33n;
    private boolean f34o;
    private int f35p;
    private int f36q;
    private int top;
    private int width;

    public StylishImgView(Context var1) {
        super(var1);
        //this.f26a = "FuncyImageView";
        this.grid = null;
        this.f27c = 100.0f;
        this.f28e = new float[9];
        this.f31l = -1f;
        this.isScaled = Boolean.valueOf(false);
        this.isTouching = Boolean.valueOf(true);
        this.isValid = Boolean.valueOf(false);
        this.isSelected = false;
        m20d();
    }

    private float m19a(float var1, float var2, float var3, float var4) {
        var1 -= var2;
        var2 = var3 - var4;
        return (float) Math.sqrt((double) ((var1 * var1) + (var2 * var2)));
    }

    private void m20d() {
        setScaleType(ScaleType.MATRIX);
        this.matrix = new Matrix();
        Drawable var1 = getDrawable();
        if (var1 != null) {
            this.f29j = var1.getIntrinsicWidth();
            this.f30k = var1.getIntrinsicHeight();
        }
    }

    private float m21e() {
        return (float) Math.sqrt((double) ((this.width * this.width) + (this.height * this.height)));
    }

    protected float m22a(Matrix var1, int var2) {
        var1.getValues(this.f28e);
        return this.f28e[var2];
    }

    public void m23a() {
        int left = getLeft();
        int top = getTop();
        int right = getRight();
        int bottom = getBottom();
        if (this.grid != null) {
            this.width = Math.min(right - left, (int) (this.grid.m16a().right - this.grid.m16a().left));
            this.height = Math.min(bottom - top, (int) (this.grid.m16a().bottom - this.grid.m16a().top));
            this.top = (int) this.grid.m16a().top;
            this.left = (int) this.grid.m16a().left;
        } else {
            this.top = 0;
            this.left = 0;
            this.width = right - left;
            this.height = bottom - top;
        }
        this.f32m = ((float) this.width) / ((float) this.f29j);
        if (this.f32m * ((float) this.f30k) < ((float) this.height)) {
            this.f32m = ((float) this.height) / ((float) this.f30k);
        }
    }

    public void m24a(float var1, int var2, int var3) {
        float var5 = getScale();
        if (var5 * var1 < this.f32m) {
            var1 = this.f32m / var5;
        }
        float var4 = var1;
        if (var1 >= 1.0f) {
            var4 = var1;
            if (var5 * var1 > this.f27c) {
                var4 = this.f27c / var5;
            }
        }
        this.matrix.postScale(var4, var4);
        this.matrix.postTranslate((-((((float) this.width) * var4) - ((float) this.width))) / 2.0f, (-((((float) this.height) * var4) - ((float) this.height))) / 2.0f);
        this.matrix.postTranslate(((float) (-(var2 - (this.width / 2)))) * var4, 0.0f);
        this.matrix.postTranslate(0.0f, ((float) (-(var3 - (this.height / 2)))) * var4);
        setImageMatrix(this.matrix);
    }

    public void m25a(MotionEvent event) {
        if (this.isValid.booleanValue()) {
            int pointerCount = event.getPointerCount();
            switch (event.getAction()) {
                case ACTION_DOWN /*0*/:
                case ACTION_POINTER_DOWN /*5*/:
                case 261:
                    if (pointerCount < 2) {
                        this.f35p = (int) event.getX();
                        this.f36q = (int) event.getY();
                        break;
                    }
                    this.f33n = m19a(event.getX(0), event.getX(1), event.getY(0), event.getY(1));
                    this.f34o = true;
                    this.isScaled = Boolean.valueOf(true);
                    break;
                case ACTION_UP /*1*/:
                case ACTION_POINTER_UP /*6*/:
                case 262:
                    if (event.getPointerCount() <= 1) {
                        this.f34o = false;
                        return;
                    }
                    return;
                case ACTION_MOVE /*2*/:
                    break;
                default:
                    return;
            }
            if (pointerCount >= 2 && this.f34o) {
                float var2 = m19a(event.getX(0), event.getX(1), event.getY(0), event.getY(1));
                float var3 = (var2 - this.f33n) / m21e();
                this.f33n = var2;
                var2 = 1.0f + var3;
                m27b(var2 * var2, this.width / 2, this.height / 2);
                this.isScaled = Boolean.valueOf(true);
                m28c();
            } else if (!this.f34o) {
                pointerCount = this.f35p;
                int var5 = (int) event.getX();
                int var6 = this.f36q;
                int var7 = (int) event.getY();
                this.f35p = (int) event.getX();
                this.f36q = (int) event.getY();
                this.matrix.postTranslate((float) (-(pointerCount - var5)), (float) (-(var6 - var7)));
                m28c();
            }
        }
    }

    public void m26b() {
        if (!this.isScaled.booleanValue()) {
            m24a(1.0f, this.width / 2, this.height / 2);
        }
    }

    public void m27b(float var1, int var2, int var3) {
        float var5 = getScale();
        if (var5 * var1 < this.f32m) {
            var1 = this.f32m / var5;
        }
        float var4 = var1;
        if (var1 >= 1.0f) {
            var4 = var1;
            if (var5 * var1 > this.f27c) {
                var4 = this.f27c / var5;
            }
        }
        this.matrix.postScale(var4, var4);
        this.matrix.postTranslate(((float) (-(var2 - (this.width / 2)))) * var4, 0.0f);
        this.matrix.postTranslate(0.0f, ((float) (-(var3 - (this.height / 2)))) * var4);
        setImageMatrix(this.matrix);
    }

    public void m28c() {
        int var1 = (int) (((float) this.f29j) * getScale());
        int var2 = (int) (((float) this.f30k) * getScale());
        if (getTranslateX() < ((float) (this.left - (var1 - this.width)))) {
            this.matrix.postTranslate(((float) this.left) - ((getTranslateX() + ((float) var1)) - ((float) this.width)), 0.0f);
        }
        if (getTranslateX() > ((float) this.left)) {
            this.matrix.postTranslate(((float) this.left) - getTranslateX(), 0.0f);
        }
        if (getTranslateY() < ((float) (this.top - (var2 - this.height)))) {
            this.matrix.postTranslate(0.0f, ((float) this.top) - ((getTranslateY() + ((float) var2)) - ((float) this.height)));
        }
        if (getTranslateY() > ((float) this.top)) {
            this.matrix.postTranslate(0.0f, ((float) this.top) - getTranslateY());
        }
        if (var1 < this.width) {
            this.matrix.postTranslate((float) ((this.width - var1) / 2), 0.0f);
        }
        if (var2 < this.height) {
            this.matrix.postTranslate(0.0f, (float) ((this.height - var2) / 2));
        }
        setImageMatrix(this.matrix);
    }

    protected float getScale() {
        return m22a(this.matrix, 0);
    }

    public float getTranslateX() {
        return m22a(this.matrix, 2);
    }

    protected float getTranslateY() {
        return m22a(this.matrix, 5);
    }

    private RectF rect;
    @Override
    protected void onDraw(Canvas canvas) {
        try {
            if(rect==null){
                rect = new RectF(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
            }else {
                rect = new RectF(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
            }
            Drawable drawable = getDrawable();
            if (this.grid != null) {
                Paint paint = ((BitmapDrawable) drawable).getPaint();
                int var4 = canvas.saveLayer(rect, null, Canvas.ALL_SAVE_FLAG);
                paint.setAntiAlias(true);
                canvas.drawARGB(0, 0, 0, 0);
                paint.setColor(Color.parseColor("#ff777777"));//-8947849
                this.grid.m17a(canvas, rect, paint);
                Xfermode var5 = paint.getXfermode();
                paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
                super.onDraw(canvas);
                paint.setXfermode(var5);
                canvas.restoreToCount(var4);
                return;
            }
            super.onDraw(canvas);
        }catch (Exception e){

        }

    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouch(View view, MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent event) {
        if (this.isTouching.booleanValue()) {
            m25a(event);
        }
        return false;
    }

    protected boolean setFrame(int var1, int var2, int var3, int var4) {
        int var6 = 0;
        if (this.grid != null) {
            this.width = Math.min(var3 - var1, (int) (this.grid.m16a().right - this.grid.m16a().left));
            this.height = Math.min(var4 - var2, (int) (this.grid.m16a().bottom - this.grid.m16a().top));
            this.top = (int) this.grid.m16a().top;
            this.left = (int) this.grid.m16a().left;
        } else {
            this.top = 0;
            this.left = 0;
            this.width = var3 - var1;
            this.height = var4 - var2;
        }
        int var8;
        if (!this.isValid.booleanValue()) {
            this.matrix.reset();
            this.f31l = ((float) this.width) / ((float) this.f29j);
            if (this.f31l * ((float) this.f30k) < ((float) this.height)) {
                this.f31l = ((float) this.height) / ((float) this.f30k);
                this.matrix.postScale(this.f31l, this.f31l);
                var6 = (var3 - this.width) / 2;
                var8 = 0;
            } else {
                this.matrix.postScale(this.f31l, this.f31l);
                var8 = (var4 - this.height) / 2;
            }
            this.matrix.postTranslate((float) var6, (float) var8);
            setImageMatrix(this.matrix);
            this.f32m = this.f31l;
            m27b(this.f31l, this.width / 2, this.height / 2);
            m28c();
        } else if (this.f31l < 0.0f) {
            this.matrix.reset();
            this.f31l = ((float) this.width) / ((float) this.f29j);
            if (this.f31l * ((float) this.f30k) < ((float) this.height)) {
                this.f31l = ((float) this.height) / ((float) this.f30k);
                this.matrix.postScale(this.f31l, this.f31l);
                var6 = (byte) 0;
                var8 = (var3 - this.width) / 2;
            } else {
                this.matrix.postScale(this.f31l, this.f31l);
                var6 = (var4 - this.height) / 2;
                var8 = 0;
            }
            this.matrix.postTranslate((float) var8, (float) var6);
            setImageMatrix(this.matrix);
            this.f32m = this.f31l;
            m27b(1.0f, this.width / 2, this.height / 2);
            m28c();
        } else {
            this.f32m = ((float) this.width) / ((float) this.f29j);
            if (this.f32m * ((float) this.f30k) < ((float) this.height)) {
                this.f32m = ((float) this.height) / ((float) this.f30k);
            }
        }
        return super.setFrame(var1, var2, var3, var4);
    }

    public void setImageBitmap(Bitmap var1) {
        this.isValid = Boolean.TRUE;
        Bitmap var2 = var1;
        if (var1 == null) {
            this.isValid = Boolean.FALSE;
            var2 = CollageConst.reInitDefaultBitmap();
        }
        super.setImageBitmap(var2);
        this.f31l = -1f;
        m20d();
    }

    public void updateImageBitmap(Bitmap var1) {
        this.isValid = Boolean.TRUE;
        Bitmap var2 = var1;
        if (var1 == null) {
            this.isValid = Boolean.FALSE;
            var2 = CollageConst.reInitDefaultBitmap();
        }
        super.setImageBitmap(var2);
        if (!this.isValid.booleanValue()) {
            this.f31l = -1f;
            m20d();
        }
        if (this.isSelected && this.isValid.booleanValue() && getDrawable() != null) {
            getDrawable().setColorFilter(1442775040, Mode.SRC_ATOP);
        }
    }

    public void setImageDescriptios(FancyCollageGrid grid) {
        this.grid = grid;
        invalidate();
    }

    public void setIsTouching(Boolean var1) {
        this.isTouching = var1;
    }

    public void setScaled(boolean var1) {
        this.isScaled = Boolean.valueOf(var1);
    }

    public void setIsSelected(boolean selected) {
        if (selected != this.isSelected) {
            this.isSelected = selected;
            if (this.isValid.booleanValue() && getDrawable() != null) {
                if (this.isSelected) {
                    getDrawable().setColorFilter(1442775040, Mode.SRC_ATOP);
                } else {
                    getDrawable().clearColorFilter();
                }
            }
            postInvalidate();
            postInvalidate();
        }
    }
}
