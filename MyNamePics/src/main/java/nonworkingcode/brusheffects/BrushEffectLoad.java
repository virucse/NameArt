package nonworkingcode.brusheffects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class BrushEffectLoad {
    protected Context mContext;
    private float f2009b;
    private Bitmap[] arrayOfBitmap;
    private int mAlpha;
    private float f2012e;
    private Bitmap f2013f;
    private boolean f2014g;
    private int numRow;
    private String fileName;
    private Paint mPaint;
    private int numColumn;
    private float f2019l;
    private int f2020m;
    private BrushEffectLoadEnum f2021n;
    private boolean isRandomColor = true;
    private int index;

    public BrushEffectLoad() {
        this.mAlpha = 120;
        this.f2012e = 0.15f;
        this.f2014g = false;
        this.numRow = 1;
        this.numColumn = 1;
    }

    public static Bitmap m3805a(Context context, String str) {
        try {
            InputStream open = context.getResources().getAssets().open(str);
            Bitmap decodeStream = BitmapFactory.decodeStream(open);
            try {
                open.close();
                return decodeStream;
            } catch (IOException e) {
                e.printStackTrace();
                return decodeStream;
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public void m3806a() {
        if (this.arrayOfBitmap != null) {
            for (Bitmap bitmap : this.arrayOfBitmap) {
                if (!(bitmap == null || bitmap.isRecycled())) {
                    bitmap.recycle();
                }
            }
        }
        this.arrayOfBitmap = null;
        if (this.f2013f != null && !this.f2013f.isRecycled()) {
            this.f2013f.recycle();
            this.f2013f = null;
        }
    }

    public void m3809a(Context context) {
        m3806a();
        mPaint = new Paint();
        mPaint.setXfermode(new PorterDuffXfermode(Mode.SCREEN));
        if (mAlpha != 0) {
            mPaint.setAlpha(this.mAlpha);
        }
        this.arrayOfBitmap = new Bitmap[(this.numColumn * this.numRow)];
        Bitmap a = BrushEffectLoad.m3805a(context, this.fileName);
        if (a == null || a.isRecycled()) {
            Log.e("bitmap", "null");
            return;
        }
        int width = a.getWidth() / this.numRow;
        int height = a.getHeight() / this.numColumn;
        this.f2013f = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        int i = 0;
        for (int i2 = 0; i2 < this.numColumn; i2++) {
            int i3 = 0;
            while (i3 < this.numRow) {
                this.arrayOfBitmap[i] = Bitmap.createBitmap(a, i3 * width, i2 * height, width, height);
                i3++;
                i++;
            }
        }
        this.f2020m = GetDeviceWidth.getWidth(context);
        this.f2019l = ((float) this.f2020m) * this.f2012e;
        this.f2009b = (float) width;
    }

    public void drawEffect(Canvas canvas, float f, float f2, int color, int i2, int i3, int i4) {
        if (this.f2013f != null && !this.f2013f.isRecycled()) {
            Canvas canvas2 = new Canvas(this.f2013f);
            canvas2.drawColor(0, Mode.CLEAR);
            if (this.arrayOfBitmap != null && this.arrayOfBitmap.length > i2 && this.arrayOfBitmap[i2] != null && !this.arrayOfBitmap[i2].isRecycled()) {
                canvas2.drawBitmap(this.arrayOfBitmap[i2], 0.0f, 0.0f, null);
                if (this.mAlpha != 0) {
                    String key = getFileName();
                    if (BrushPref.getIsBrushRandomColor(mContext, key)) {
                        if (getIndex() >= 10) {
                            //canvas2.drawColor(color, Mode.SRC_ATOP);
                        } else {
                            canvas2.drawColor(color, Mode.MULTIPLY);
                        }
                    } else {
                        canvas2.drawColor(color, Mode.SRC_ATOP);
                    }

                }
                Matrix matrix = new Matrix();
                if (this.f2014g) {
                    if (i4 == 0) {
                        i3 += 315;
                    }
                    matrix.postRotate((float) i3);
                }
                float width = (this.f2019l * (((float) canvas.getWidth()) / ((float) this.f2020m))) / this.f2009b;
                matrix.postScale(width, width);
                matrix.postTranslate(f - ((((float) this.f2013f.getWidth()) * width) / 2.0f), f2 - ((width * ((float) this.f2013f.getHeight())) / 2.0f));
                canvas.drawBitmap(this.f2013f, matrix, this.mPaint);
            }
        }
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setResLoc(BrushEffectLoadEnum brushEffectLoad) {
        this.f2021n = brushEffectLoad;
    }

    public int getColumn() {
        return this.numColumn;
    }

    //how many part widh devided into.
    public void setColumn(int i) {
        this.numColumn = i;
    }

    public int getRow() {
        return this.numRow;
    }

    public void setRow(int i) {
        this.numRow = i;
    }

    public void m3813a(boolean z) {
        this.f2014g = z;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String str) {
        this.fileName = str;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void m3807a(float f) {
        this.f2012e = f;
    }

    public void setAlpha(int alpha) {
        this.mAlpha = alpha;
    }

    public enum BrushEffectLoadEnum {
        RES,
        ASSETS,
        FILTERED,
        ONLINE,
        CACHE
    }
}
