package nonworkingcode.grid.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import nonworkingcode.grid.util.CollageConst;
import nonworkingcode.grid.util.CollageUtil;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_UP;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by Caliber Fashion on 12/9/2016.
 */

public class FancyCollage extends BaseCollageView {
    private List<FancyCollageGrid> collageGrids;
    private List<StylishImgView> mImageViews;

    public FancyCollage(Context context) {
        super(context);
        this.collageGrids = null;
        this.mImageViews = new ArrayList();
    }

    public int getPositionAtPoint(Point point) {
        if (this.collageGrids != null) {
            for (int i = 0; i < this.collageGrids.size(); i++) {
                if (((FancyCollageGrid) this.collageGrids.get(i)).m18a(point)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public View getViewAtPosition(int index) {
        return (View) this.mImageViews.get(index);
    }

    public void updateRatio(int ratioX, int ratioY) {
        LayoutParams var3 = new LayoutParams(ratioX, ratioY);
        var3.addRule(13, -1);
        setLayoutParams(var3);
        CollageUtil.collageWidth = ratioX;
        CollageUtil.collageHeight = ratioY;
        if (CollageConst.shadow > 0.0f) {
            m68c();
        }
        invalidate(new Rect(0, 0, getWidth(), getHeight()));
    }

    public void resetBitmap(Bitmap bitmap, int index) {
        if (index < this.mImageViews.size()) {
            CollageConst.collageBitmaps[index] = bitmap;
            ((StylishImgView) this.mImageViews.get(index)).setImageBitmap(bitmap);
        }
    }

    public void update(Bitmap bitmap, int index) {
        if (index < this.mImageViews.size()) {
            CollageConst.collageBitmaps[index] = bitmap;
            ((StylishImgView) this.mImageViews.get(index)).updateImageBitmap(bitmap);
        }
    }

    void m68c() {
        if (CollageConst.shadow == 0.0f) {
            BaseCollageView.setBackground(this.mShapeLayer, null);
            return;
        }
        boolean var12;
        RectF var2 = new RectF(0.0f, 0.0f, 200.0f, 200.0f);
        int var8 = 0;
        while (var8 < this.collageGrids.size()) {
            if ((this.collageGrids.get(var8)).f22i != null || (this.collageGrids.get(var8)).f20g != null) {
                var12 = true;
                break;
            }
            var8++;
        }
        var12 = false;
        Bitmap var3 = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Canvas var4 = new Canvas(var3);
        Paint var5 = new Paint();
        var5.setAntiAlias(true);
        var5.setColor(Color.parseColor("#333334"));//-3355444
        var5.setStyle(Paint.Style.FILL);
        if (var12) {
            for (var8 = (byte) 0; var8 < this.collageGrids.size(); var8++) {
                Bitmap var6 = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
                (this.collageGrids.get(var8)).m17a(new Canvas(var6), var2, var5);
                var4.drawBitmap(var6, null, var2, var5);
            }
        } else {
            for (int var9 = 0; var9 < this.collageGrids.size(); var9++) {
                (this.collageGrids.get(var9)).m17a(var4, var2, var5);
            }
        }
        BaseCollageView.setBackground(this.mShapeLayer, new BitmapDrawable(getResources(), CollageConst.getShadowBitmap(var3, CollageConst.shadow)));
    }

    public int getImageListSize() {
        return this.collageGrids == null ? 0 : this.collageGrids.size();
    }

    public boolean onTouch(View var1, MotionEvent var2) {
        return super.onTouchEvent(var2);
    }

    /*
   * some replacement done by caliber fashion such as
   * k=>f83k
   * l=>f84l
   * m=>f85m
   * j=>f82j
   * */
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mIsEditable && !this.gestureDetector.onTouchEvent(event)) {
            int pCount = event.getPointerCount();
            switch (event.getAction()) {
                case ACTION_DOWN /*0*/:
                case ACTION_POINTER_DOWN /*5*/:
                case 261:
                    //StickerConst.invalidateStickers();
                    Point paint;
                    if (pCount >= 2) {
                        paint = new Point((int) event.getX(0), (int) event.getY(0));
                        Point var3 = new Point((int) event.getX(1), (int) event.getY(1));
                        pCount = getPositionAtPoint(paint);
                        if (pCount >= 0 && pCount == getPositionAtPoint(var3)) {
                            this.f82j = 3;
                            this.f83k = pCount;
                        }
                    } else {
                        this.f84l = (int) event.getX();
                        this.f85m = (int) event.getY();
                        paint = new Point(this.f84l, this.f85m);
                        pCount = m54b(paint);
                        if (pCount >= 0) {
                            this.f82j = 4;
                            this.f83k = pCount;
                        } else {
                            pCount = getPositionAtPoint(paint);
                            if (pCount >= 0) {
                                this.f82j = 3;
                                this.f83k = pCount;
                            } else {
                                this.f82j = 0;
                                this.f83k = pCount;
                            }
                        }
                    }
                    if (this.f82j != 3) {
                        if (this.f82j == 4) {
                            m53a(event);
                            break;
                        }
                    }
                    if (f83k != -1) {
                        (this.mImageViews.get(this.f83k)).m25a(event);
                    }
                    break;
                case ACTION_UP /*1*/:
                case ACTION_POINTER_UP /*6*/:
                case 262:
                    if (this.f82j == 3) {
                        if (f83k != -1) {
                            (this.mImageViews.get(this.f83k)).m25a(event);
                        }
                    } else if (this.f82j == 4) {
                        m55b(event);
                    }
                    this.f82j = 0;
                    break;
                case ACTION_MOVE /*2*/:
                    break;
            }
            if (this.f82j == 3) {
                if (f83k != -1) {
                    (this.mImageViews.get(this.f83k)).m25a(event);
                }
            } else if (this.f82j == 4) {
                m55b(event);
            }
            this.f84l = (int) event.getX();
            this.f85m = (int) event.getY();
        }
        return true;
    }

    public void setCornerRadious(float var1) {
    }

    public void setGridNumber(int var1) {
        CollageConst.gridIndex = var1;
        if (CollageConst.gridIndex >= 256) {
            var1 = CollageConst.gridIndex - 256;
        }
        this.collageGrids = FancyCollageGrid.m15a(var1);
        this.mFrameLayer.removeAllViews();
        this.mImageViews.clear();
        for (var1 = 0; var1 < this.collageGrids.size(); var1++) {
            StylishImgView var2 = new StylishImgView(getContext());
            var2.setImageBitmap(CollageConst.collageBitmaps[var1]);
            var2.setImageDescriptios((FancyCollageGrid) this.collageGrids.get(var1));
            this.mImageViews.add(var2);
            this.mFrameLayer.addView(var2);
            var2.setLayoutParams(new LayoutParams(-1, -1));
        }
        if (CollageConst.shadow > 0.0f) {
            m68c();
        }
        invalidate(new Rect(0, 0, getWidth(), getHeight()));
    }

    public void setLineThickness(float var1) {
        CollageConst.lineThickness = var1;
        if (CollageConst.shadow > 0.0f) {
            m68c();
        }
        for (StylishImgView var3 : this.mImageViews) {
            var3.m23a();
            var3.postInvalidate(0, 0, getWidth(), getHeight());
        }
        for (StylishImgView b : this.mImageViews) {
            b.m26b();
        }
    }

    public void setSelectedAtPosition(boolean value, int index) {
        for (StylishImgView view : this.mImageViews) {
            view.setIsSelected(false);
        }
        if (value) {
            (this.mImageViews.get(index)).setIsSelected(true);
            (this.mImageViews.get(index)).bringToFront();
        }
    }

    public void setShadowSize(float var1) {
        CollageConst.shadow = var1;
        m68c();
        invalidate(new Rect(0, 0, getWidth(), getHeight()));
    }
}
