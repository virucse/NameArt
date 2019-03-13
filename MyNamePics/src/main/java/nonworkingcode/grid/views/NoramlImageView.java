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
import android.graphics.Rect;
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

public class NoramlImageView extends AppCompatImageView implements OnTouchListener {
    private final float[] f38d;
    private float f37b;
    private int f39e;
    private int f40f;
    private int f41g;
    private int f42h;
    private float f43i;
    private boolean isSelected;
    private float f44j;
    private float f45k;
    private boolean f46l;
    private int f47m;
    private Matrix matrix;
    private int f48n;
    private float f49o;
    private Bitmap f50p;
    private Boolean f51q;
    private Boolean f52r;
    private Boolean f53s;

    public NoramlImageView(Context var1) {
        super(var1);
        this.f37b = 100.0f;
        this.f38d = new float[9];
        this.f43i = -1f;//ImageViewTouchBase.ZOOM_INVALID;
        this.f49o = 0.0f;
        this.f50p = null;
        this.f51q = Boolean.valueOf(true);
        this.f52r = Boolean.valueOf(false);
        this.f53s = Boolean.valueOf(false);
        this.isSelected = false;
        init();
    }

    private float m29a(float var1, float var2, float var3, float var4) {
        var1 -= var2;
        var2 = var3 - var4;
        return (float) Math.sqrt((double) ((var1 * var1) + (var2 * var2)));
    }

    private void init() {
        setScaleType(ScaleType.MATRIX);
        this.matrix = new Matrix();
        Drawable var1 = getDrawable();
        if (var1 != null) {
            this.f41g = var1.getIntrinsicWidth();
            this.f42h = var1.getIntrinsicHeight();
        }
    }

    private float m30d() {
        return (float) Math.sqrt((double) ((this.f39e * this.f39e) + (this.f40f * this.f40f)));
    }

    protected float m31a(Matrix var1, int var2) {
        var1.getValues(this.f38d);
        return this.f38d[var2];
    }

    public void m32a() {
        if (!this.f53s.booleanValue()) {
            m33a(1.0f, this.f39e / 2, this.f40f / 2);
        }
    }

    public void m33a(float var1, int var2, int var3) {
        float var5 = getScale();
        if (var5 * var1 < this.f44j) {
            var1 = this.f44j / var5;
        }
        float var4 = var1;
        if (var1 >= 1.0f) {
            var4 = var1;
            if (var5 * var1 > this.f37b) {
                var4 = this.f37b / var5;
            }
        }
        this.matrix.postScale(var4, var4);
        this.matrix.postTranslate(((float) (-(var2 - (this.f39e / 2)))) * var4, 0.0f);
        this.matrix.postTranslate(0.0f, ((float) (-(var3 - (this.f40f / 2)))) * var4);
        setImageMatrix(this.matrix);
    }

    public void m34a(MotionEvent var1) {
        if (this.f52r.booleanValue()) {
            int var4 = var1.getPointerCount();
            switch (var1.getAction()) {
                case ACTION_DOWN /*0*/:
                case ACTION_POINTER_DOWN /*5*/:
                case 261:
                    if (var4 < 2) {
                        this.f47m = (int) var1.getX();
                        this.f48n = (int) var1.getY();
                        break;
                    }
                    this.f45k = m29a(var1.getX(0), var1.getX(1), var1.getY(0), var1.getY(1));
                    this.f46l = true;
                    this.f53s = Boolean.valueOf(true);
                    break;
                case ACTION_UP /*1*/:
                case ACTION_POINTER_UP /*6*/:
                case 262:
                    if (var1.getPointerCount() <= 1) {
                        this.f46l = false;
                        return;
                    }
                    return;
                case ACTION_MOVE /*2*/:
                    break;
                default:
                    return;
            }
            if (var4 >= 2 && this.f46l) {
                float var2 = m29a(var1.getX(0), var1.getX(1), var1.getY(0), var1.getY(1));
                float var3 = (var2 - this.f45k) / m30d();
                this.f45k = var2;
                var2 = 1.0f + var3;
                m36b(var2 * var2, this.f39e / 2, this.f40f / 2);
                this.f53s = Boolean.valueOf(true);
                m35b();
            } else if (!this.f46l) {
                var4 = this.f47m;
                int var5 = (int) var1.getX();
                int var6 = this.f48n;
                int var7 = (int) var1.getY();
                this.f47m = (int) var1.getX();
                this.f48n = (int) var1.getY();
                this.matrix.postTranslate((float) (-(var4 - var5)), (float) (-(var6 - var7)));
                m35b();
            }
        }
    }

    public void m35b() {
        int var1 = (int) (((float) this.f41g) * getScale());
        int var2 = (int) (((float) this.f42h) * getScale());
        if (getTranslateX() < ((float) (-(var1 - this.f39e)))) {
            this.matrix.postTranslate(-((getTranslateX() + ((float) var1)) - ((float) this.f39e)), 0.0f);
        }
        if (getTranslateX() > 0.0f) {
            this.matrix.postTranslate(-getTranslateX(), 0.0f);
        }
        if (getTranslateY() < ((float) (-(var2 - this.f40f)))) {
            this.matrix.postTranslate(0.0f, -((getTranslateY() + ((float) var2)) - ((float) this.f40f)));
        }
        if (getTranslateY() > 0.0f) {
            this.matrix.postTranslate(0.0f, -getTranslateY());
        }
        if (var1 < this.f39e) {
            this.matrix.postTranslate((float) ((this.f39e - var1) / 2), 0.0f);
        }
        if (var2 < this.f40f) {
            this.matrix.postTranslate(0.0f, (float) ((this.f40f - var2) / 2));
        }
        setImageMatrix(this.matrix);
    }

    public void m36b(float var1, int var2, int var3) {
        float var5 = getScale();
        if (var5 * var1 < this.f44j) {
            var1 = this.f44j / var5;
        }
        float var4 = var1;
        if (var1 >= 1.0f) {
            var4 = var1;
            if (var5 * var1 > this.f37b) {
                var4 = this.f37b / var5;
            }
        }
        this.matrix.postScale(var4, var4);
        this.matrix.postTranslate((-((((float) this.f39e) * var4) - ((float) this.f39e))) / 2.0f, (-((((float) this.f40f) * var4) - ((float) this.f40f))) / 2.0f);
        this.matrix.postTranslate(((float) (-(var2 - (this.f39e / 2)))) * var4, 0.0f);
        this.matrix.postTranslate(0.0f, ((float) (-(var3 - (this.f40f / 2)))) * var4);
        setImageMatrix(this.matrix);
    }

    protected float getScale() {
        return m31a(this.matrix, 0);
    }

    public float getTranslateX() {
        return m31a(this.matrix, 2);
    }

    protected float getTranslateY() {
        return m31a(this.matrix, 5);
    }

    RectF var2;
   @Override
    protected void onDraw(Canvas canvas) {
        try {
            if(var2==null){
                var2 = new RectF(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
            }else {
                var2 = new RectF(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
            }
            Drawable var3 = getDrawable();
            Paint var6;
            int var4;
            Xfermode var5;
            if ((var3 instanceof BitmapDrawable) && this.f50p != null) {
                var6 = ((BitmapDrawable) var3).getPaint();
                var4 = canvas.saveLayer(var2, null, Canvas.ALL_SAVE_FLAG);
                var6.setAntiAlias(true);
                canvas.drawARGB(0, 0, 0, 0);
                var6.setColor(Color.parseColor("#ff777777"));//-8947849
                canvas.drawBitmap(this.f50p, new Rect(0, 0, this.f50p.getWidth(), this.f50p.getHeight()), var2, var6);
                var5 = var6.getXfermode();
                var6.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
                super.onDraw(canvas);
                var6.setXfermode(var5);
                canvas.restoreToCount(var4);
            } else if (!(var3 instanceof BitmapDrawable) || this.f49o <= 0.0f) {
                super.onDraw(canvas);
            } else {
                var6 = ((BitmapDrawable) var3).getPaint();
                var4 = canvas.saveLayer(var2, null, Canvas.ALL_SAVE_FLAG);
                var6.setAntiAlias(true);
                canvas.drawARGB(0, 0, 0, 0);
                var6.setColor(Color.parseColor("#ff777777"));//-8947849
                canvas.drawRoundRect(var2, this.f49o, this.f49o, var6);
                var5 = var6.getXfermode();
                var6.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
                super.onDraw(canvas);
                var6.setXfermode(var5);
                canvas.restoreToCount(var4);
            }
        }catch (Exception e){

        }
    }

   @Override
    public boolean onTouch(View var1, MotionEvent var2) {
        return super.onTouchEvent(var2);
    }

   @Override
    public boolean onTouchEvent(MotionEvent var1) {
        if (this.f51q.booleanValue()) {
            m34a(var1);
        }
        return false;
    }

    public void setCornerRadius(float var1) {
        this.f49o = var1;
        invalidate();
    }

    protected boolean setFrame(int var1, int var2, int var3, int var4) {
        int var6 = 0;
        this.f39e = var3 - var1;
        this.f40f = var4 - var2;
        int var8;
        if (!this.f52r) {
            this.matrix.reset();
            this.f43i = ((float) this.f39e) / ((float) this.f41g);
            if (this.f43i * ((float) this.f42h) < ((float) this.f40f)) {
                this.f43i = ((float) this.f40f) / ((float) this.f42h);
                this.matrix.postScale(this.f43i, this.f43i);
                var6 = (var3 - this.f39e) / 2;
                var8 = 0;
            } else {
                this.matrix.postScale(this.f43i, this.f43i);
                var8 = (var4 - this.f40f) / 2;
            }
            this.matrix.postTranslate((float) var6, (float) var8);
            setImageMatrix(this.matrix);
            this.f44j = this.f43i;
            m36b(this.f43i, this.f39e / 2, this.f40f / 2);
            m35b();
        } else if (this.f43i < 0.0f) {
            this.matrix.reset();
            this.f43i = ((float) this.f39e) / ((float) this.f41g);
            if (this.f43i * ((float) this.f42h) < ((float) this.f40f)) {
                this.f43i = ((float) this.f40f) / ((float) this.f42h);
                this.matrix.postScale(this.f43i, this.f43i);
                var6 = (byte) 0;
                var8 = (var3 - this.f39e) / 2;
            } else {
                this.matrix.postScale(this.f43i, this.f43i);
                var6 = (var4 - this.f40f) / 2;
                var8 = 0;
            }
            this.matrix.postTranslate((float) var8, (float) var6);
            setImageMatrix(this.matrix);
            this.f44j = this.f43i;
            m36b(1.0f, this.f39e / 2, this.f40f / 2);
            m35b();
        } else {
            this.f44j = ((float) this.f39e) / ((float) this.f41g);
            if (this.f44j * ((float) this.f42h) < ((float) this.f40f)) {
                this.f44j = ((float) this.f40f) / ((float) this.f42h);
            }
        }
        return super.setFrame(var1, var2, var3, var4);
    }

    public void setImageBitmap(Bitmap var1) {
        this.f52r = Boolean.TRUE;
        Bitmap var2 = var1;
        if (var1 == null) {
            this.f52r = Boolean.FALSE;
            var2 = CollageConst.reInitDefaultBitmap();
        }
        super.setImageBitmap(var2);
        this.f43i = -1;
        init();
    }

    public void setIsTouching(Boolean var1) {
        this.f51q = var1;
    }

    public void setMaskBitmap(Bitmap var1) {
        this.f50p = var1;
        init();
    }

    public void setScaled(boolean var1) {
        this.f53s = var1;
    }

    public void setIsSelected(boolean selected) {
        if (selected != this.isSelected) {
            isSelected = selected;
            if (this.f52r && getDrawable() != null) {
                if (isSelected) {
                    getDrawable().setColorFilter(1442775040, Mode.SRC_ATOP);
                } else {
                    getDrawable().clearColorFilter();
                }
            }
            postInvalidate();
        }
    }

    public void updateImageView(Bitmap var1) {
        this.f52r = Boolean.TRUE;
        Bitmap var2 = var1;
        if (var1 == null) {
            this.f52r = Boolean.FALSE;
            var2 = CollageConst.reInitDefaultBitmap();
            init();
            this.f43i = -1;
        }
        super.setImageBitmap(var2);
        if (this.isSelected && this.f52r.booleanValue() && getDrawable() != null) {
            getDrawable().setColorFilter(1442775040, Mode.SRC_ATOP);
        }
    }
}
