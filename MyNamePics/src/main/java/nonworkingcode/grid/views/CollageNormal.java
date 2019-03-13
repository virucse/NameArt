package nonworkingcode.grid.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
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

public class CollageNormal extends BaseCollageView {
    final String TAG = CollageNormal.class.getName();
    private List<NoramlImageView> noramlImageViewList;
    private List<NormalCollageGrid> normalCollageGridList;
    private List<CollageRatioManager> collageRatioManagerList;
    private List<CollageRatioManager> collageRatioManagerList1;

    public CollageNormal(Context context) {
        super(context);
        this.normalCollageGridList = null;
        this.collageRatioManagerList = null;
        this.collageRatioManagerList1 = null;
        this.noramlImageViewList = new ArrayList();
    }

    @Override
    public View getViewAtPosition(int i) {
        return (View) this.noramlImageViewList.get(i);
    }

    /*
    * some replacement done by caliber fashion such as
    * k=>f83k
    * l=>f84l
    * m=>f85m
    * */
    private void m62a(float var1, float var2) {
        var2 = (this.collageRatioManagerList.get(this.f83k)).ratio;
        (this.collageRatioManagerList.get(this.f83k)).ratio = var2 + ((var1 - ((float) this.f84l)) / ((float) CollageUtil.collageWidth));
        for (int var3 = 0; var3 < this.normalCollageGridList.size(); var3++) {
            if (!(this.normalCollageGridList.get(var3)).m38a((float) CollageUtil.collageWidth, (float) CollageUtil.collageHeight, CollageConst.lineThickness, this.collageRatioManagerList, this.collageRatioManagerList1)) {
                (this.collageRatioManagerList.get(this.f83k)).ratio = var2;
                break;
            }
        }
        m67c();
    }

    private void m63b(float var1, float var2) {
        var1 = (this.collageRatioManagerList1.get(this.f83k)).ratio;
        (this.collageRatioManagerList1.get(this.f83k)).ratio = var1 + ((var2 - ((float) this.f85m)) / ((float) CollageUtil.collageHeight));
        for (int var3 = 0; var3 < this.normalCollageGridList.size(); var3++) {
            if (!(this.normalCollageGridList.get(var3)).m38a((float) CollageUtil.collageWidth, (float) CollageUtil.collageHeight, CollageConst.lineThickness, this.collageRatioManagerList, this.collageRatioManagerList1)) {
                (this.collageRatioManagerList1.get(this.f83k)).ratio = var1;
                break;
            }
        }
        m67c();
    }

    private int m64c(Point var1) {
        float var2 = ((float) CollageConst.collageBaseWidth) * Math.max(CollageConst.lineThickness, 0.02f);
        if (this.collageRatioManagerList != null) {
            for (int i = 0; i < this.collageRatioManagerList.size(); i++) {
                CollageRatioManager var3 = this.collageRatioManagerList.get(i);
                int var7 = (int) ((var3.ratio * ((float) CollageUtil.collageWidth)) - var2);
                int var5;
                if (var3.a >= 0) {
                    var5 = (int) ((this.collageRatioManagerList1.get(var3.a)).ratio * ((float) CollageUtil.collageHeight));
                } else {
                    var5 = 0;
                }
                int var8 = (int) ((var3.ratio * ((float) CollageUtil.collageWidth)) + var2);
                int var6;
                if (var3.b >= 0) {
                    var6 = (int) ((this.collageRatioManagerList1.get(var3.b)).ratio * ((float) CollageUtil.collageHeight));
                } else {
                    var6 = CollageUtil.collageHeight;
                }
                if (var1.x > var7 && var1.x < var8 && var1.y > var5 && var1.y < var6) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int m65d(Point var1) {
        float var2 = ((float) CollageConst.collageBaseWidth) * Math.max(CollageConst.lineThickness, 0.02f);
        if (this.collageRatioManagerList1 != null) {
            for (int var4 = 0; var4 < this.collageRatioManagerList1.size(); var4++) {
                int var5;
                CollageRatioManager var3 = this.collageRatioManagerList1.get(var4);
                int var7 = (int) ((var3.ratio * ((float) CollageUtil.collageHeight)) - var2);
                if (var3.a >= 0) {
                    var5 = (int) ((this.collageRatioManagerList.get(var3.a)).ratio * ((float) CollageUtil.collageWidth));
                } else {
                    var5 = 0;
                }
                int var8 = (int) ((var3.ratio * ((float) CollageUtil.collageHeight)) + var2);
                int var6;
                if (var3.b >= 0) {
                    var6 = (int) ((this.collageRatioManagerList.get(var3.b)).ratio * ((float) CollageUtil.collageWidth));
                } else {
                    var6 = CollageUtil.collageWidth;
                }
                if (var1.x > var5 && var1.x < var6 && var1.y > var7 && var1.y < var8) {
                    return var4;
                }
            }
        }
        return -1;
    }

    void m67c() {
        if (this.normalCollageGridList != null) {
            for (int i = 0; i < this.normalCollageGridList.size(); i++) {
                NormalCollageGrid grid = this.normalCollageGridList.get(i);
                NoramlImageView view = this.noramlImageViewList.get(i);
                grid.m38a((float) CollageUtil.collageWidth, (float) CollageUtil.collageHeight, CollageConst.lineThickness, this.collageRatioManagerList, this.collageRatioManagerList1);
                LayoutParams param = new LayoutParams(-1, -1);
                param.setMargins((int) grid.f60f, (int) grid.f61g, (int) grid.f62h, (int) grid.f63i);
                view.setLayoutParams(param);
            }
        }
        if (CollageConst.shadow > 0.0f) {
            m66d();
        }
    }

    private void m66d() {
        float var1 = 200.0f / ((float) CollageUtil.collageWidth);
        float var2 = 200.0f / ((float) CollageUtil.collageHeight);
        Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#838384"));//-8618884
        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < this.normalCollageGridList.size(); i++) {
            canvas.drawRoundRect(new RectF((this.normalCollageGridList.get(i)).f60f * var1, (this.normalCollageGridList.get(i)).f61g * var2, 200.0f - (((NormalCollageGrid) this.normalCollageGridList.get(i)).f62h * var1), 200.0f - (((NormalCollageGrid) this.normalCollageGridList.get(i)).f63i * var2)), CollageConst.cornerRadius * var1, CollageConst.cornerRadius * var2, paint);
        }
        BaseCollageView.setBackground(this.mShapeLayer, new BitmapDrawable(getResources(), CollageConst.getShadowBitmap(bitmap, CollageConst.shadow)));
    }

    public int getImageListSize() {
        return this.normalCollageGridList == null ? 0 : this.normalCollageGridList.size();
    }

    @SuppressLint({"ClickableViewAccessibility"})
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
    public boolean onTouchEvent(MotionEvent var1) {
        if (this.mIsEditable && !this.gestureDetector.onTouchEvent(var1)) {
            int var4 = var1.getPointerCount();
            switch (var1.getAction()) {
                case ACTION_DOWN /*0*/:
                case ACTION_POINTER_DOWN /*5*/:
                case 261:
                    //StickerConst.invalidateStickers();
                    Point var2;
                    if (var4 >= 2) {
                        var2 = new Point((int) var1.getX(0), (int) var1.getY(0));
                        Point var3 = new Point((int) var1.getX(1), (int) var1.getY(1));
                        var4 = getPositionAtPoint(var2);
                        if (var4 >= 0 && var4 == getPositionAtPoint(var3)) {
                            this.f82j = 3;
                            this.f83k = var4;
                        }
                    } else {
                        this.f84l = (int) var1.getX();
                        this.f85m = (int) var1.getY();
                        var2 = new Point(this.f84l, this.f85m);
                        var4 = m54b(var2);
                        if (var4 >= 0) {
                            this.f82j = 4;
                            this.f83k = var4;
                        } else {
                            var4 = m64c(var2);
                            if (var4 >= 0) {
                                this.f82j = 1;
                                this.f83k = var4;
                                m67c();
                                for (NoramlImageView a : this.noramlImageViewList) {
                                    a.m32a();
                                }
                            } else {
                                var4 = m65d(var2);
                                if (var4 >= 0) {
                                    this.f82j = 2;
                                    this.f83k = var4;
                                    m67c();
                                    for (NoramlImageView a2 : this.noramlImageViewList) {
                                        a2.m32a();
                                    }
                                } else {
                                    var4 = getPositionAtPoint(var2);
                                    if (var4 >= 0) {
                                        this.f82j = 3;
                                        this.f83k = var4;
                                    } else {
                                        this.f82j = 0;
                                        this.f83k = var4;
                                    }
                                }
                            }
                        }
                    }
                    if (this.f82j != 3) {
                        if (!(this.f82j == 1 || this.f82j == 2 || this.f82j != 4)) {
                            m53a(var1);
                            break;
                        }
                    }
                    if (f83k != -1&&f83k<normalCollageGridList.size()) {
                        (noramlImageViewList.get(f83k)).m34a(var1);
                    }
                    break;
                case ACTION_UP /*1*/:
                case ACTION_POINTER_UP /*6*/:
                case 262:
                    if (this.f82j == 3) {
                        (this.noramlImageViewList.get(this.f83k)).m34a(var1);
                    } else if (this.f82j == 1) {
                        m62a(var1.getX(), var1.getY());
                        m67c();
                        for (NoramlImageView a22 : this.noramlImageViewList) {
                            a22.m32a();
                        }
                    } else if (this.f82j == 2) {
                        m63b(var1.getX(), var1.getY());
                        m67c();
                        for (NoramlImageView a222 : this.noramlImageViewList) {
                            a222.m32a();
                        }
                    } else if (this.f82j == 4) {
                        m55b(var1);
                    }
                    this.f82j = 0;
                    break;
                case ACTION_MOVE /*2*/:
                    break;
            }
            if (this.f82j == 3) {
                (this.noramlImageViewList.get(this.f83k)).m34a(var1);
            } else if (this.f82j == 1) {
                m62a(var1.getX(), var1.getY());
                m67c();
                for (NoramlImageView a2222 : this.noramlImageViewList) {
                    a2222.m32a();
                }
            } else if (this.f82j == 2) {
                m63b(var1.getX(), var1.getY());
                m67c();
                for (NoramlImageView a22222 : this.noramlImageViewList) {
                    a22222.m32a();
                }
            } else if (this.f82j == 4) {
                m55b(var1);
            }
            this.f84l = (int) var1.getX();
            this.f85m = (int) var1.getY();
        }
        return true;
    }

    public int getPositionAtPoint(Point var1) {
        if (this.normalCollageGridList != null) {
            for (int var3 = 0; var3 < this.normalCollageGridList.size(); var3++) {
                NormalCollageGrid var2 = (NormalCollageGrid) this.normalCollageGridList.get(var3);
                if (((float) var1.x) > var2.f60f && ((float) var1.x) < ((float) CollageUtil.collageWidth) - var2.f62h && ((float) var1.y) > var2.f61g && ((float) var1.y) < ((float) CollageUtil.collageHeight) - var2.f63i) {
                    return var3;
                }
            }
        }
        return -1;
    }

    public void setSelectedAtPosition(boolean selected, int index) {
        for (NoramlImageView view : this.noramlImageViewList) {
            view.setIsSelected(false);
        }
        if (selected) {
            (this.noramlImageViewList.get(index)).setIsSelected(true);
            //Log.d(TAG, "setSelectedAtPosition: NormalCollageGrid at:" + index + " :k " + f83k);
        }
    }

    public void setCornerRadious(float var1) {
        CollageConst.cornerRadius = var1;
        if (this.noramlImageViewList != null) {
            for (NoramlImageView cornerRadius : this.noramlImageViewList) {
                cornerRadius.setCornerRadius(CollageConst.cornerRadius);
            }
        }
        if (CollageConst.shadow > 0.0f) {
            m66d();
        }
    }

    public void setGridNumber(int index) {
        CollageConst.gridIndex = index;
        this.normalCollageGridList = NormalCollageGrid.m37a(CollageConst.gridIndex);
        this.collageRatioManagerList = CollageRatioManager.m10a(CollageConst.gridIndex);
        this.collageRatioManagerList1 = CollageRatioManager.m11b(CollageConst.gridIndex);
        this.mFrameLayer.removeAllViews();
        this.noramlImageViewList.clear();
        if (this.normalCollageGridList != null) {
            for (index = 0; index < this.normalCollageGridList.size(); index++) {
                NoramlImageView view = new NoramlImageView(getContext());
                view.setImageBitmap(CollageConst.collageBitmaps[index]);
                this.noramlImageViewList.add(view);
                this.mFrameLayer.addView(view);
            }
        }
        m67c();
        setCornerRadious(CollageConst.cornerRadius);
    }

    public void setLineThickness(float var1) {
        CollageConst.lineThickness = var1;
        m67c();
        for (NoramlImageView a : this.noramlImageViewList) {
            a.m32a();
        }
    }

    public void setShadowSize(float var1) {
        if (var1 <= 0.0f) {
            CollageConst.shadow = 0.0f;
            BaseCollageView.setBackground(this.mShapeLayer, null);
            return;
        }
        CollageConst.shadow = var1;
        m66d();
    }

    public void updateRatio(int var1, int var2) {
        LayoutParams var3 = new LayoutParams(var1, var2);
        var3.addRule(13, -1);
        setLayoutParams(var3);
        CollageUtil.collageWidth = var1;
        CollageUtil.collageHeight = var2;
        m67c();
    }

    public void resetBitmap(Bitmap var1, int var2) {
        if (var2 < this.noramlImageViewList.size()) {
            CollageConst.collageBitmaps[var2] = var1;
            (this.noramlImageViewList.get(var2)).setImageBitmap(var1);
        }
    }

    public void update(Bitmap var1, int var2) {
        if (var2 < this.noramlImageViewList.size()) {
            CollageConst.collageBitmaps[var2] = var1;
            (this.noramlImageViewList.get(var2)).updateImageView(var1);
        }
    }

}
