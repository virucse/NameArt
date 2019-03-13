package nonworkingcode.grid.views;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.v4.internal.view.SupportMenu;

import com.formationapps.nameart.R;

import java.util.ArrayList;
import java.util.List;

import nonworkingcode.grid.util.CollageConst;
import nonworkingcode.grid.util.CollageUtil;

public class FancyCollageGrid {
    static final float f17a;
    static final double f18b;
    static Bitmap bitmap;
    static Canvas mCanvas;
    static Paint mPaint;

    static {
        f17a = (float) Math.sqrt(2.0d);
        f18b = Math.sqrt(5.0d);
        mPaint = new Paint();
    }

    public List<CollageOffset> f19f;
    public List<CollageOffset> f20g;
    public List<CustomShape> f21h;
    public List<CustomShape> f22i;
    private float f23j;
    private int f24k;
    private int f25l;
    private RectF rect;

    public FancyCollageGrid(List<CollageOffset> var1, List<CollageOffset> var2, List<CustomShape> var3, List<CustomShape> var4) {
        this.f19f = null;
        this.f20g = null;
        this.f21h = null;
        this.f22i = null;
        this.f23j = -1f;
        this.f24k = -1;
        this.f25l = -1;
        this.rect = new RectF();
        this.f19f = var1;
        this.f20g = var2;
        this.f21h = var3;
        this.f22i = var4;
    }

    public static CustomShape m12a(int var0, float var1, float var2, float var3, float var4) {
        CustomShape var7 = new CustomShape(var0, (var3 + var1) / 2.0f, (var4 + var2) / 2.0f, var3 - var1, var4 - var2);
        if (var1 == 0.0f) {
            var1 = 1.0f;
        } else {
            var1 = 0.5f;
        }
        var7.f12g = var1;
        var1 = 0.5f;
        if (var2 == 0.0f) {
            var1 = 1.0f;
        }
        var7.f13h = var1;
        if (var3 == 1.0f) {
            var1 = -1f;
        } else {
            var1 = -0.5f;
        }
        var7.f14i = var1;
        if (var4 == 1.0f) {
            var1 = -1f;
        } else {
            var1 = -0.5f;
        }
        var7.f15j = var1;
        return var7;
    }

    public static CustomShape m13a(int var0, float var1, float var2, float var3, float var4, int var5) {
        CustomShape var6 = new CustomShape(var0, (var3 + var1) / 2.0f, (var4 + var2) / 2.0f, var3 - var1, var4 - var2);
        if (var1 == 0.0f) {
            var1 = 1.0f;
        } else {
            var1 = 0.5f;
        }
        var6.f12g = var1;
        if (var2 == 0.0f) {
            var1 = 1.0f;
        } else {
            var1 = 0.5f;
        }
        var6.f13h = var1;
        if (var3 == 1.0f) {
            var1 = -1f;
        } else {
            var1 = -0.5f;
        }
        var6.f14i = var1;
        if (var4 == 1.0f) {
            var1 = -1f;
        } else {
            var1 = -0.5f;
        }
        var6.f15j = var1;
        var6.f7b = var5;
        return var6;
    }

    public static List<CollageOffset> m14a(float var0, float var1, float var2, float var3) {
        double var4;
        double var6;
        ArrayList<CollageOffset> var12 = new ArrayList();
        double var8 = (double) var0;
        double var10 = (double) var1;
        if (var0 == 0.0f) {
            var4 = 1.0d;
        } else {
            var4 = 0.5d;
        }
        if (var1 == 0.0f) {
            var6 = 1.0d;
        } else {
            var6 = 0.5d;
        }
        var12.add(new CollageOffset(var8, var10, var4, var6));
        var8 = (double) var2;
        var10 = (double) var1;
        if (var2 == 1.0f) {
            var4 = -1.0d;
        } else {
            var4 = -0.5d;
        }
        if (var1 == 0.0f) {
            var6 = 1.0d;
        } else {
            var6 = 0.5d;
        }
        var12.add(new CollageOffset(var8, var10, var4, var6));
        var8 = (double) var2;
        var10 = (double) var3;
        if (var2 == 1.0f) {
            var4 = -1.0d;
        } else {
            var4 = -0.5d;
        }
        if (var3 == 1.0f) {
            var6 = -1.0d;
        } else {
            var6 = -0.5d;
        }
        var12.add(new CollageOffset(var8, var10, var4, var6));
        var8 = (double) var0;
        var10 = (double) var3;
        if (var0 == 0.0f) {
            var4 = 1.0d;
        } else {
            var4 = 0.5d;
        }
        if (var3 == 1.0f) {
            var6 = -1.0d;
        } else {
            var6 = -0.5d;
        }
        var12.add(new CollageOffset(var8, var10, var4, var6));
        return var12;
    }

    public static List<FancyCollageGrid> m15a(int var0) {
        List<FancyCollageGrid> var1 = new ArrayList();
        List<CollageOffset> var2;
        List<CustomShape> var4;
        List<CustomShape> var3;
        switch (var0) {
            case 0 /*0*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(0));
                var2.add(CollageOffset.m9a(16));
                var2.add(new CollageOffset(0.5d, 0.5d, -0.5d, -0.5d * ((double) (f17a - 1.0f))));
                var2.add(CollageOffset.m9a(14));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(17));
                var2.add(CollageOffset.m9a(1));
                var2.add(CollageOffset.m9a(8));
                var2.add(new CollageOffset(0.5d, 0.5d, 0.5d, -0.5d * ((double) (f17a - 1.0f))));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(12));
                var2.add(CollageOffset.m9a(10));
                var2.add(CollageOffset.m9a(11));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 1 /*1*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(6));
                var2.add(CollageOffset.m9a(4));
                var2.add(CollageOffset.m9a(5));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(21));
                var2.add(CollageOffset.m9a(3));
                var2.add(CollageOffset.m9a(13));
                var2.add(new CollageOffset(0.5d, 0.5d, -0.5d, 0.5d * ((double) (f17a - 1.0f))));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(2));
                var2.add(CollageOffset.m9a(20));
                var2.add(new CollageOffset(0.5d, 0.5d, 0.5d, 0.5d * ((double) (f17a - 1.0f))));
                var2.add(CollageOffset.m9a(7));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 2 /*2*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(9));
                var2.add(CollageOffset.m9a(7));
                var2.add(CollageOffset.m9a(8));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(23));
                var2.add(CollageOffset.m9a(0));
                var2.add(CollageOffset.m9a(5));
                var2.add(new CollageOffset(0.5d, 0.5d, -0.5d * ((double) (f17a - 1.0f)), -0.5d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(3));
                var2.add(CollageOffset.m9a(22));
                var2.add(new CollageOffset(0.5d, 0.5d, -0.5d * ((double) (f17a - 1.0f)), 0.5d));
                var2.add(CollageOffset.m9a(10));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 3 /*3*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(15));
                var2.add(CollageOffset.m9a(13));
                var2.add(CollageOffset.m9a(14));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(19));
                var2.add(CollageOffset.m9a(2));
                var2.add(CollageOffset.m9a(11));
                var2.add(new CollageOffset(0.5d, 0.5d, 0.5d * ((double) (f17a - 1.0f)), 0.5d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(1));
                var2.add(CollageOffset.m9a(18));
                var2.add(new CollageOffset(0.5d, 0.5d, 0.5d * ((double) (f17a - 1.0f)), -0.5d));
                var2.add(CollageOffset.m9a(4));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 4 /*4*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(0));
                var2.add(CollageOffset.m9a(1));
                var2.add(CollageOffset.m9a(18));
                var2.add(CollageOffset.m9a(23));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(22));
                var2.add(new CollageOffset(0.5d, 0.5d, -0.5d - (0.5d * ((double) f17a)), 0.5d));
                var2.add(CollageOffset.m9a(14));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.5d, 0.5d, 0.5d + (0.5d * ((double) f17a)), 0.5d));
                var2.add(CollageOffset.m9a(19));
                var2.add(CollageOffset.m9a(8));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(12));
                var2.add(CollageOffset.m9a(10));
                var2.add(CollageOffset.m9a(11));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 5 /*5*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(4));
                var2.add(CollageOffset.m9a(5));
                var2.add(CollageOffset.m9a(6));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(13));
                var2.add(new CollageOffset(0.5d, 0.5d, -0.5d - (0.5d * ((double) f17a)), -0.5d));
                var2.add(CollageOffset.m9a(23));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.5d, 0.5d, 0.5d + (0.5d * ((double) f17a)), -0.5d));
                var2.add(CollageOffset.m9a(7));
                var2.add(CollageOffset.m9a(18));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(22));
                var2.add(CollageOffset.m9a(19));
                var2.add(CollageOffset.m9a(2));
                var2.add(CollageOffset.m9a(3));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 6 /*6*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(0));
                var2.add(CollageOffset.m9a(16));
                var2.add(new CollageOffset(0.5d, 0.25d, -0.5d, -0.5d));
                var2.add(new CollageOffset(0.20000000298023224d, 0.25d, -0.5d, -0.5d));
                var2.add(new CollageOffset(0.20000000298023224d, 0.75d, -0.5d, 0.5d));
                var2.add(new CollageOffset(0.5d, 0.75d, -0.5d, 0.5d));
                var2.add(CollageOffset.m9a(21));
                var2.add(CollageOffset.m9a(3));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(2));
                var2.add(CollageOffset.m9a(20));
                var2.add(new CollageOffset(0.5d, 0.75d, 0.5d, 0.5d));
                var2.add(new CollageOffset(0.800000011920929d, 0.75d, 0.5d, 0.5d));
                var2.add(new CollageOffset(0.800000011920929d, 0.25d, 0.5d, -0.5d));
                var2.add(new CollageOffset(0.5d, 0.25d, 0.5d, -0.5d));
                var2.add(CollageOffset.m9a(17));
                var2.add(CollageOffset.m9a(1));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.20000000298023224d, 0.25d, 0.5d, 0.5d));
                var2.add(new CollageOffset(0.800000011920929d, 0.25d, -0.5d, 0.5d));
                var2.add(new CollageOffset(0.800000011920929d, 0.75d, -0.5d, -0.5d));
                var2.add(new CollageOffset(0.20000000298023224d, 0.75d, 0.5d, -0.5d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 7 /*7*/:
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.0f, 1.0f, 0.5f), m14a(0.2f, 0.25f, 0.8f, 0.75f), null, null));
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.5f, 1.0f, 1.0f), m14a(0.2f, 0.25f, 0.8f, 0.75f), null, null));
                var1.add(new FancyCollageGrid(m14a(0.2f, 0.25f, 0.8f, 0.75f), null, null, null));
                break;
            case 8 /*8*/:
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_s8_1, 0.0f, 0.0f, 0.53f, 0.25f));
                var4.add(m12a(R.drawable.mask_s8_1, 0.0f, 0.75f, 0.53f, 1.0f));
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.0f, 0.2f, 1.0f), null, var4, null));
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_s8_2, 0.47f, 0.0f, 1.0f, 0.25f));
                var4.add(m12a(R.drawable.mask_s8_2, 0.47f, 0.75f, 1.0f, 1.0f));
                var1.add(new FancyCollageGrid(m14a(0.8f, 0.0f, 1.0f, 1.0f), null, var4, null));
                var1.add(new FancyCollageGrid(m14a(0.2f, 0.25f, 0.8f, 0.75f), null, null, null));
                break;
            case 9 /*9*/:
                var4 = new ArrayList();
                var4.add(m13a(R.drawable.mask_circle, 0.25f, 0.25f, 0.75f, 0.75f, 0));
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.0f, 0.5f, 1.0f), null, null, var4));
                var4 = new ArrayList();
                var4.add(m13a(R.drawable.mask_circle, 0.25f, 0.25f, 0.75f, 0.75f, 0));
                var1.add(new FancyCollageGrid(m14a(0.5f, 0.0f, 1.0f, 1.0f), null, null, var4));
                var4 = new ArrayList();
                var4.add(m13a(R.drawable.mask_circle, 0.25f, 0.25f, 0.75f, 0.75f, 0));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
            case 10/*10*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(13));
                var2.add(CollageOffset.m9a(10));
                var2.add(CollageOffset.m9a(3));
                var3 = new ArrayList();
                var3.add(m13a(R.drawable.mask_circle, 0.25f, 0.25f, 0.75f, 0.75f, 0));
                var1.add(new FancyCollageGrid(var2, null, null, var3));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(4));
                var2.add(CollageOffset.m9a(1));
                var2.add(CollageOffset.m9a(8));
                var3 = new ArrayList();
                var3.add(m13a(R.drawable.mask_circle, 0.25f, 0.25f, 0.75f, 0.75f, 0));
                var1.add(new FancyCollageGrid(var2, null, null, var3));
                var4 = new ArrayList();
                var4.add(m13a(R.drawable.mask_circle, 0.25f, 0.25f, 0.75f, 0.75f, 0));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
            case 11 /*11*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(0));
                var2.add(CollageOffset.m9a(16));
                var2.add(new CollageOffset(0.45d, 0.1d, -0.5d, 0.8d));
                var2.add(new CollageOffset(0.55d, 0.3d, -0.5d, 0.4d));
                var2.add(new CollageOffset(0.45d, 0.5d, -0.5d, 0.0d));
                var2.add(new CollageOffset(0.55d, 0.7d, -0.5d, -0.4d));
                var2.add(new CollageOffset(0.45d, 0.9d, -0.5d, -0.8d));
                var2.add(CollageOffset.m9a(21));
                var2.add(CollageOffset.m9a(3));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(1));
                var2.add(CollageOffset.m9a(17));
                var2.add(new CollageOffset(0.45d, 0.1d, 0.5d, 0.8d));
                var2.add(new CollageOffset(0.55d, 0.3d, 0.5d, 0.4d));
                var2.add(new CollageOffset(0.45d, 0.5d, 0.5d, 0.0d));
                var2.add(new CollageOffset(0.55d, 0.7d, 0.5d, -0.4d));
                var2.add(new CollageOffset(0.45d, 0.9d, 0.5d, -0.8d));
                var2.add(CollageOffset.m9a(20));
                var2.add(CollageOffset.m9a(2));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 12 /*12*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(0));
                var2.add(CollageOffset.m9a(23));
                var2.add(new CollageOffset(0.1d, 0.55d, 0.8d, -0.5d));
                var2.add(new CollageOffset(0.3d, 0.45d, 0.4d, -0.5d));
                var2.add(new CollageOffset(0.5d, 0.55d, 0.0d, -0.5d));
                var2.add(new CollageOffset(0.7d, 0.45d, -0.4d, -0.5d));
                var2.add(new CollageOffset(0.9d, 0.55d, -0.8d, -0.5d));
                var2.add(CollageOffset.m9a(18));
                var2.add(CollageOffset.m9a(1));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(3));
                var2.add(CollageOffset.m9a(22));
                var2.add(new CollageOffset(0.1d, 0.55d, 0.8d, 0.5d));
                var2.add(new CollageOffset(0.3d, 0.45d, 0.4d, 0.5d));
                var2.add(new CollageOffset(0.5d, 0.55d, 0.0d, 0.5d));
                var2.add(new CollageOffset(0.7d, 0.45d, -0.4d, 0.5d));
                var2.add(new CollageOffset(0.9d, 0.55d, -0.8d, 0.5d));
                var2.add(CollageOffset.m9a(19));
                var2.add(CollageOffset.m9a(2));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 13 /*13*/:
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_s13_1, 0.0f, 0.0f, 0.56f, 1.0f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_s13_2, 0.44666666f, 0.0f, 1.0f, 1.0f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
            case 14 /*14*/:
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_s14_1, 0.0f, 0.0f, 1.0f, 0.56f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_s14_2, 0.0f, 0.44666666f, 1.0f, 1.0f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
            case 15 /*15*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(0));
                var2.add(CollageOffset.m9a(16));
                var2.add(new CollageOffset(0.4d, 0.25d, -0.5d, 0.5d));
                var2.add(CollageOffset.m9a(24));
                var2.add(new CollageOffset(0.25d, 0.6d, 0.5d, -0.5d));
                var2.add(CollageOffset.m9a(23));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(1));
                var2.add(CollageOffset.m9a(18));
                var2.add(new CollageOffset(0.75d, 0.4d, -0.5d, -0.5d));
                var2.add(CollageOffset.m9a(25));
                var2.add(new CollageOffset(0.4d, 0.25d, 0.5d, 0.5d));
                var2.add(CollageOffset.m9a(17));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(2));
                var2.add(CollageOffset.m9a(20));
                var2.add(new CollageOffset(0.6d, 0.75d, 0.5d, -0.5d));
                var2.add(CollageOffset.m9a(26));
                var2.add(new CollageOffset(0.75d, 0.4d, -0.5d, 0.5d));
                var2.add(CollageOffset.m9a(19));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(3));
                var2.add(CollageOffset.m9a(22));
                var2.add(new CollageOffset(0.25d, 0.6d, 0.5d, 0.5d));
                var2.add(CollageOffset.m9a(27));
                var2.add(new CollageOffset(0.6d, 0.75d, -0.5d, -0.5d));
                var2.add(CollageOffset.m9a(21));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 16 /*16*/:
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_s16_1, 0.0f, 0.0f, 0.5f, 0.63f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_s16_2, 0.37f, 0.0f, 1.0f, 0.5f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_s16_3, 0.5f, 0.37f, 1.0f, 1.0f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_s16_4, 0.0f, 0.5f, 0.63f, 1.0f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
            case 17 /*17*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(6));
                var2.add(CollageOffset.m9a(4));
                var2.add(CollageOffset.m9a(5));
                var3 = new ArrayList();
                var3.add(m13a(R.drawable.mask_circle, 0.25f, 0.25f, 0.75f, 0.75f, 0));
                var1.add(new FancyCollageGrid(var2, null, null, var3));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(9));
                var2.add(CollageOffset.m9a(7));
                var2.add(CollageOffset.m9a(8));
                var3 = new ArrayList();
                var3.add(m13a(R.drawable.mask_circle, 0.25f, 0.25f, 0.75f, 0.75f, 0));
                var1.add(new FancyCollageGrid(var2, null, null, var3));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(12));
                var2.add(CollageOffset.m9a(10));
                var2.add(CollageOffset.m9a(11));
                var3 = new ArrayList();
                var3.add(m13a(R.drawable.mask_circle, 0.25f, 0.25f, 0.75f, 0.75f, 0));
                var1.add(new FancyCollageGrid(var2, null, null, var3));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(15));
                var2.add(CollageOffset.m9a(13));
                var2.add(CollageOffset.m9a(14));
                var3 = new ArrayList();
                var3.add(m13a(R.drawable.mask_circle, 0.25f, 0.25f, 0.75f, 0.75f, 0));
                var1.add(new FancyCollageGrid(var2, null, null, var3));
                var4 = new ArrayList();
                var4.add(m13a(R.drawable.mask_circle, 0.25f, 0.25f, 0.75f, 0.75f, 0));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
            case 18 /*18*/:
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.0f, 0.5f, 0.5f), m14a(0.25f, 0.25f, 0.75f, 0.75f), null, null));
                var1.add(new FancyCollageGrid(m14a(0.5f, 0.0f, 1.0f, 0.5f), m14a(0.25f, 0.25f, 0.75f, 0.75f), null, null));
                var1.add(new FancyCollageGrid(m14a(0.5f, 0.5f, 1.0f, 1.0f), m14a(0.25f, 0.25f, 0.75f, 0.75f), null, null));
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.5f, 0.5f, 1.0f), m14a(0.25f, 0.25f, 0.75f, 0.75f), null, null));
                var1.add(new FancyCollageGrid(m14a(0.25f, 0.25f, 0.75f, 0.75f), null, null, null));
                break;
            case 19 /*19*/:
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.5d, 0.25d, 0.0d, 0.5d));
                var2.add(new CollageOffset(0.75d, 0.5d, -0.5d, 0.0d));
                var2.add(new CollageOffset(0.5d, 0.75d, 0.0d, -0.5d));
                var2.add(new CollageOffset(0.25d, 0.5d, 0.5d, 0.0d));
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.0f, 0.5f, 1.0f), var2, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.5d, 0.25d, 0.0d, 0.5d));
                var2.add(new CollageOffset(0.75d, 0.5d, -0.5d, 0.0d));
                var2.add(new CollageOffset(0.5d, 0.75d, 0.0d, -0.5d));
                var2.add(new CollageOffset(0.25d, 0.5d, 0.5d, 0.0d));
                var1.add(new FancyCollageGrid(m14a(0.5f, 0.0f, 1.0f, 1.0f), var2, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.5d, 0.25d, 0.0d, 0.5d));
                var2.add(new CollageOffset(0.75d, 0.5d, -0.5d, 0.0d));
                var2.add(new CollageOffset(0.5d, 0.75d, 0.0d, -0.5d));
                var2.add(new CollageOffset(0.25d, 0.5d, 0.5d, 0.0d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 20 /*20*/:
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_pentagon, 0.2f, 0.2f, 0.8f, 0.8f));
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.0f, 0.5f, 1.0f), null, null, var4));
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_pentagon, 0.2f, 0.2f, 0.8f, 0.8f));
                var1.add(new FancyCollageGrid(m14a(0.5f, 0.0f, 1.0f, 1.0f), null, null, var4));
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_pentagon, 0.2f, 0.2f, 0.8f, 0.8f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
            case 21 /*21*/:
                var4 = new ArrayList();
                var4.add(m13a(R.drawable.mask_circle, 0.25f, 0.25f, 0.75f, 0.75f, 0));
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.0f, 0.5f, 0.5f), null, null, var4));
                var4 = new ArrayList();
                var4.add(m13a(R.drawable.mask_circle, 0.25f, 0.25f, 0.75f, 0.75f, 0));
                var1.add(new FancyCollageGrid(m14a(0.5f, 0.0f, 1.0f, 0.5f), null, null, var4));
                var4 = new ArrayList();
                var4.add(m13a(R.drawable.mask_circle, 0.25f, 0.25f, 0.75f, 0.75f, 0));
                var1.add(new FancyCollageGrid(m14a(0.5f, 0.5f, 1.0f, 1.0f), null, null, var4));
                var4 = new ArrayList();
                var4.add(m13a(R.drawable.mask_circle, 0.25f, 0.25f, 0.75f, 0.75f, 0));
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.5f, 0.5f, 1.0f), null, null, var4));
                var4 = new ArrayList();
                var4.add(m13a(R.drawable.mask_circle, 0.25f, 0.25f, 0.75f, 0.75f, 0));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
            case 22 /*22*/:
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.5d, 0.25d, 0.0d, 0.5d));
                var2.add(new CollageOffset(0.75d, 0.5d, -0.5d, 0.0d));
                var2.add(new CollageOffset(0.5d, 0.75d, 0.0d, -0.5d));
                var2.add(new CollageOffset(0.25d, 0.5d, 0.5d, 0.0d));
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.0f, 0.5f, 0.5f), var2, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.5d, 0.25d, 0.0d, 0.5d));
                var2.add(new CollageOffset(0.75d, 0.5d, -0.5d, 0.0d));
                var2.add(new CollageOffset(0.5d, 0.75d, 0.0d, -0.5d));
                var2.add(new CollageOffset(0.25d, 0.5d, 0.5d, 0.0d));
                var1.add(new FancyCollageGrid(m14a(0.5f, 0.0f, 1.0f, 0.5f), var2, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.5d, 0.25d, 0.0d, 0.5d));
                var2.add(new CollageOffset(0.75d, 0.5d, -0.5d, 0.0d));
                var2.add(new CollageOffset(0.5d, 0.75d, 0.0d, -0.5d));
                var2.add(new CollageOffset(0.25d, 0.5d, 0.5d, 0.0d));
                var1.add(new FancyCollageGrid(m14a(0.5f, 0.5f, 1.0f, 1.0f), var2, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.5d, 0.25d, 0.0d, 0.5d));
                var2.add(new CollageOffset(0.75d, 0.5d, -0.5d, 0.0d));
                var2.add(new CollageOffset(0.5d, 0.75d, 0.0d, -0.5d));
                var2.add(new CollageOffset(0.25d, 0.5d, 0.5d, 0.0d));
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.5f, 0.5f, 1.0f), var2, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.5d, 0.25d, 0.0d, 0.5d));
                var2.add(new CollageOffset(0.75d, 0.5d, -0.5d, 0.0d));
                var2.add(new CollageOffset(0.5d, 0.75d, 0.0d, -0.5d));
                var2.add(new CollageOffset(0.25d, 0.5d, 0.5d, 0.0d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 23 /*23*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(6));
                var2.add(CollageOffset.m9a(4));
                var2.add(CollageOffset.m9a(5));
                var1.add(new FancyCollageGrid(var2, m14a(0.25f, 0.25f, 0.75f, 0.75f), null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(9));
                var2.add(CollageOffset.m9a(7));
                var2.add(CollageOffset.m9a(8));
                var1.add(new FancyCollageGrid(var2, m14a(0.25f, 0.25f, 0.75f, 0.75f), null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(12));
                var2.add(CollageOffset.m9a(10));
                var2.add(CollageOffset.m9a(11));
                var1.add(new FancyCollageGrid(var2, m14a(0.25f, 0.25f, 0.75f, 0.75f), null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(15));
                var2.add(CollageOffset.m9a(13));
                var2.add(CollageOffset.m9a(14));
                var1.add(new FancyCollageGrid(var2, m14a(0.25f, 0.25f, 0.75f, 0.75f), null, null));
                var1.add(new FancyCollageGrid(m14a(0.25f, 0.25f, 0.75f, 0.75f), null, null, null));
                break;
            case 24 /*24*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(4));
                var2.add(CollageOffset.m9a(1));
                var2.add(CollageOffset.m9a(8));
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.33333334f, 0.6666667f, 1.0f), var2, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(13));
                var2.add(CollageOffset.m9a(10));
                var2.add(CollageOffset.m9a(3));
                var1.add(new FancyCollageGrid(m14a(0.33333334f, 0.0f, 1.0f, 0.6666667f), var2, null, null));
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.0f, 0.33333334f, 0.33333334f), null, null, null));
                var1.add(new FancyCollageGrid(m14a(0.6666667f, 0.6666667f, 1.0f, 1.0f), null, null, null));
                break;
            case 25 /*25*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(11));
                var2.add(CollageOffset.m9a(7));
                var2.add(CollageOffset.m9a(2));
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.0f, 0.6666667f, 0.6666667f), var2, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(0));
                var2.add(CollageOffset.m9a(5));
                var2.add(CollageOffset.m9a(14));
                var1.add(new FancyCollageGrid(m14a(0.33333334f, 0.33333334f, 1.0f, 1.0f), var2, null, null));
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.6666667f, 0.33333334f, 1.0f), null, null, null));
                var1.add(new FancyCollageGrid(m14a(0.6666667f, 0.0f, 1.0f, 0.33333334f), null, null, null));
                break;
            case 26 /*26*/:
                var4 = new ArrayList();
                var4.add(m13a(R.drawable.mask_circle, -0.5f, 0.5f, 0.5f, 1.5f, 0));
                var4.add(m13a(R.drawable.mask_circle, 0.5f, -0.5f, 1.5f, 0.5f, 0));
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.0f, 1.0f, 1.0f), null, null, var4));
                var4 = new ArrayList();
                var4.add(m13a(R.drawable.mask_circle, -0.5f, 0.5f, 0.5f, 1.5f, 0));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                var4 = new ArrayList();
                var4.add(m13a(R.drawable.mask_circle, 0.5f, -0.5f, 1.5f, 0.5f, 0));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
            case 27 /*27*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(6));
                var2.add(CollageOffset.m9a(4));
                var2.add(CollageOffset.m9a(5));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(9));
                var2.add(CollageOffset.m9a(7));
                var2.add(CollageOffset.m9a(8));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(12));
                var2.add(CollageOffset.m9a(10));
                var2.add(CollageOffset.m9a(11));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(15));
                var2.add(CollageOffset.m9a(13));
                var2.add(CollageOffset.m9a(14));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 28 /*28*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(0));
                var2.add(CollageOffset.m9a(5));
                var2.add(CollageOffset.m9a(14));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(7));
                var2.add(CollageOffset.m9a(2));
                var2.add(CollageOffset.m9a(11));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 29 /*29*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(13));
                var2.add(CollageOffset.m9a(10));
                var2.add(CollageOffset.m9a(3));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(4));
                var2.add(CollageOffset.m9a(1));
                var2.add(CollageOffset.m9a(8));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 30 /*30*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(4));
                var2.add(CollageOffset.m9a(5));
                var2.add(CollageOffset.m9a(6));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(13));
                var2.add(CollageOffset.m9a(14));
                var2.add(CollageOffset.m9a(15));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(7));
                var2.add(CollageOffset.m9a(2));
                var2.add(CollageOffset.m9a(11));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 31 /*31*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(4));
                var2.add(CollageOffset.m9a(5));
                var2.add(CollageOffset.m9a(6));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(7));
                var2.add(CollageOffset.m9a(8));
                var2.add(CollageOffset.m9a(9));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(13));
                var2.add(CollageOffset.m9a(10));
                var2.add(CollageOffset.m9a(3));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 32 /*32*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(0));
                var2.add(CollageOffset.m9a(5));
                var2.add(CollageOffset.m9a(14));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(7));
                var2.add(CollageOffset.m9a(8));
                var2.add(CollageOffset.m9a(9));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(10));
                var2.add(CollageOffset.m9a(11));
                var2.add(CollageOffset.m9a(12));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 33 /*33*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(10));
                var2.add(CollageOffset.m9a(11));
                var2.add(CollageOffset.m9a(12));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(13));
                var2.add(CollageOffset.m9a(14));
                var2.add(CollageOffset.m9a(15));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(4));
                var2.add(CollageOffset.m9a(1));
                var2.add(CollageOffset.m9a(8));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 34 /*34*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(0));
                var2.add(CollageOffset.m9a(5));
                var2.add(CollageOffset.m9a(14));
                var1.add(new FancyCollageGrid(var2, m14a(0.5f, 0.0f, 1.0f, 0.5f), null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(7));
                var2.add(CollageOffset.m9a(2));
                var2.add(CollageOffset.m9a(11));
                var1.add(new FancyCollageGrid(var2, m14a(0.5f, 0.0f, 1.0f, 0.5f), null, null));
                var1.add(new FancyCollageGrid(m14a(0.5f, 0.0f, 1.0f, 0.5f), null, null, null));
                break;
            case 35 /*35*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(4));
                var2.add(CollageOffset.m9a(1));
                var2.add(CollageOffset.m9a(8));
                var1.add(new FancyCollageGrid(var2, m14a(0.0f, 0.0f, 0.5f, 0.5f), null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(13));
                var2.add(CollageOffset.m9a(10));
                var2.add(CollageOffset.m9a(3));
                var1.add(new FancyCollageGrid(var2, m14a(0.0f, 0.0f, 0.5f, 0.5f), null, null));
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.0f, 0.5f, 0.5f), null, null, null));
                break;
            case 36 /*36*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(0));
                var2.add(CollageOffset.m9a(5));
                var2.add(CollageOffset.m9a(14));
                var1.add(new FancyCollageGrid(var2, m14a(0.0f, 0.5f, 0.5f, 1.0f), null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(7));
                var2.add(CollageOffset.m9a(2));
                var2.add(CollageOffset.m9a(11));
                var1.add(new FancyCollageGrid(var2, m14a(0.0f, 0.5f, 0.5f, 1.0f), null, null));
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.5f, 0.5f, 1.0f), null, null, null));
                break;
            case 37 /*37*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(4));
                var2.add(CollageOffset.m9a(1));
                var2.add(CollageOffset.m9a(8));
                var1.add(new FancyCollageGrid(var2, m14a(0.5f, 0.5f, 1.0f, 1.0f), null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(13));
                var2.add(CollageOffset.m9a(10));
                var2.add(CollageOffset.m9a(3));
                var1.add(new FancyCollageGrid(var2, m14a(0.5f, 0.5f, 1.0f, 1.0f), null, null));
                var1.add(new FancyCollageGrid(m14a(0.5f, 0.5f, 1.0f, 1.0f), null, null, null));
                break;
            case 38 /*38*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(0));
                var2.add(new CollageOffset(0.0d, 0.5d, 1.0d, (-f18b) / 4.0d));
                var2.add(new CollageOffset(1.0d, 0.0d, -(1.0d + (f18b / 2.0d)), 1.0d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.0d, 0.5d, 1.0d, f18b / 4.0d));
                var2.add(new CollageOffset(1.0d, 0.0d, -1.0d, 1.0d + (f18b / 4.0d)));
                var2.add(new CollageOffset(1.0d, 0.5d, -1.0d, (-f18b) / 4.0d));
                var2.add(new CollageOffset(0.0d, 1.0d, 1.0d, -(1.0d + (f18b / 4.0d))));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(2));
                var2.add(new CollageOffset(1.0d, 0.5d, -1.0d, f18b / 4.0d));
                var2.add(new CollageOffset(0.0d, 1.0d, 1.0d + (f18b / 2.0d), -1.0d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 39 /*39*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(1));
                var2.add(new CollageOffset(1.0d, 0.5d, -1.0d, (-f18b) / 4.0d));
                var2.add(new CollageOffset(0.0d, 0.0d, 1.0d + (f18b / 2.0d), 1.0d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset(1.0d, 0.5d, -1.0d, f18b / 4.0d));
                var2.add(new CollageOffset(0.0d, 0.0d, 1.0d, 1.0d + (f18b / 4.0d)));
                var2.add(new CollageOffset(0.0d, 0.5d, 1.0d, (-f18b) / 4.0d));
                var2.add(new CollageOffset(1.0d, 1.0d, -1.0d, -(1.0d + (f18b / 4.0d))));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(3));
                var2.add(new CollageOffset(0.0d, 0.5d, 1.0d, f18b / 4.0d));
                var2.add(new CollageOffset(1.0d, 1.0d, -(1.0d + (f18b / 2.0d)), -1.0d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 40 /*40*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(0));
                var2.add(new CollageOffset(0.5d, 0.0d, (-f18b) / 4.0d, 1.0d));
                var2.add(new CollageOffset(0.0d, 1.0d, 1.0d, -(1.0d + (f18b / 2.0d))));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.5d, 0.0d, f18b / 4.0d, 1.0d));
                var2.add(new CollageOffset(0.0d, 1.0d, 1.0d + (f18b / 4.0d), -1.0d));
                var2.add(new CollageOffset(0.5d, 1.0d, (-f18b) / 4.0d, -1.0d));
                var2.add(new CollageOffset(1.0d, 0.0d, -(1.0d + (f18b / 4.0d)), 1.0d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(2));
                var2.add(new CollageOffset(0.5d, 1.0d, f18b / 4.0d, -1.0d));
                var2.add(new CollageOffset(1.0d, 0.0d, -1.0d, 1.0d + (f18b / 2.0d)));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 41 /*41*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(3));
                var2.add(new CollageOffset(0.5d, 1.0d, (-f18b) / 4.0d, -1.0d));
                var2.add(new CollageOffset(0.0d, 0.0d, 1.0d, 1.0d + (f18b / 2.0d)));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.5d, 1.0d, f18b / 4.0d, -1.0d));
                var2.add(new CollageOffset(0.0d, 0.0d, 1.0d + (f18b / 4.0d), 1.0d));
                var2.add(new CollageOffset(0.5d, 0.0d, (-f18b) / 4.0d, 1.0d));
                var2.add(new CollageOffset(1.0d, 1.0d, -(1.0d + (f18b / 4.0d)), -1.0d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(1));
                var2.add(new CollageOffset(0.5d, 0.0d, f18b / 4.0d, 1.0d));
                var2.add(new CollageOffset(1.0d, 1.0d, -1.0d, -(1.0d + (f18b / 2.0d))));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 42 /*42*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(3));
                var2.add(new CollageOffset(0.5d, 1.0d, (-f18b) / 4.0d, -1.0d));
                var2.add(new CollageOffset(0.0d, 0.0d, 1.0d, 1.0d + (f18b / 2.0d)));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.0d, 0.0d, 1.0d + (f18b / 4.0d), 1.0d));
                var2.add(new CollageOffset(1.0d, 0.0d, -(1.0d + (f18b / 4.0d)), 1.0d));
                var2.add(new CollageOffset(0.5d, 1.0d, 0.0d, -(1.0d + (f18b / 2.0d))));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(2));
                var2.add(new CollageOffset(0.5d, 1.0d, f18b / 4.0d, -1.0d));
                var2.add(new CollageOffset(1.0d, 0.0d, -1.0d, 1.0d + (f18b / 2.0d)));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 43 /*43*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(0));
                var2.add(new CollageOffset(0.5d, 0.0d, (double) ((-(f17a + 1.0f)) / 2.0f), 1.0d));
                var2.add(new CollageOffset(0.0d, 0.5d, 1.0d, (double) ((-(f17a + 1.0f)) / 2.0f)));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(1));
                var2.add(new CollageOffset(0.5d, 0.0d, (double) ((f17a - 1.0f) / 2.0f), 1.0d));
                var2.add(new CollageOffset(0.0d, 0.5d, 1.0d, (double) ((f17a - 1.0f) / 2.0f)));
                var2.add(CollageOffset.m9a(3));
                var2.add(new CollageOffset(0.5d, 1.0d, (double) ((-(f17a - 1.0f)) / 2.0f), -1.0d));
                var2.add(new CollageOffset(1.0d, 0.5d, -1.0d, (double) ((-(f17a - 1.0f)) / 2.0f)));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(2));
                var2.add(new CollageOffset(0.5d, 1.0d, (double) ((f17a + 1.0f) / 2.0f), -1.0d));
                var2.add(new CollageOffset(1.0d, 0.5d, -1.0d, (double) ((f17a + 1.0f) / 2.0f)));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 44 /*44*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(0));
                var2.add(new CollageOffset(0.75d, 0.0d, -0.5d, 1.0d));
                var2.add(new CollageOffset(0.5d, 0.5d, -0.5d, -0.5d));
                var2.add(new CollageOffset(0.0d, 0.25d, 1.0d, -0.5d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(1));
                var2.add(new CollageOffset(1.0d, 0.75d, -1.0d, -0.5d));
                var2.add(new CollageOffset(0.5d, 0.5d, 0.5d, -0.5d));
                var2.add(new CollageOffset(0.75d, 0.0d, 0.5d, 1.0d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(2));
                var2.add(new CollageOffset(0.25d, 1.0d, 0.5d, -1.0d));
                var2.add(new CollageOffset(0.5d, 0.5d, 0.5d, 0.5d));
                var2.add(new CollageOffset(1.0d, 0.75d, -1.0d, 0.5d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(3));
                var2.add(new CollageOffset(0.0d, 0.25d, 1.0d, 0.5d));
                var2.add(new CollageOffset(0.5d, 0.5d, -0.5d, 0.5d));
                var2.add(new CollageOffset(0.25d, 1.0d, -0.5d, -1.0d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 45 /*45*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(0));
                var2.add(new CollageOffset(0.25d, 0.0d, -0.5d, 1.0d));
                var2.add(new CollageOffset(0.5d, 0.5d, -0.5d, -0.5d));
                var2.add(new CollageOffset(0.0d, 0.75d, 1.0d, -0.5d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(1));
                var2.add(new CollageOffset(1.0d, 0.25d, -1.0d, -0.5d));
                var2.add(new CollageOffset(0.5d, 0.5d, 0.5d, -0.5d));
                var2.add(new CollageOffset(0.25d, 0.0d, 0.5d, 1.0d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(2));
                var2.add(new CollageOffset(0.75d, 1.0d, 0.5d, -1.0d));
                var2.add(new CollageOffset(0.5d, 0.5d, 0.5d, 0.5d));
                var2.add(new CollageOffset(1.0d, 0.25d, -1.0d, 0.5d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(3));
                var2.add(new CollageOffset(0.0d, 0.75d, 1.0d, 0.5d));
                var2.add(new CollageOffset(0.5d, 0.5d, -0.5d, 0.5d));
                var2.add(new CollageOffset(0.75d, 1.0d, -0.5d, -1.0d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 46 /*46*/:
                var4 = new ArrayList();
                var4.add(new CustomShape(R.drawable.mask_circle, 0.5f, 0.0f, 1.0f, 1.0f));
                var3 = new ArrayList();
                var3.add(new CustomShape(R.drawable.mask_circle, 1.0f, 0.5f, 1.0f, 1.0f));
                var1.add(new FancyCollageGrid(null, null, var4, var3));
                var4 = new ArrayList();
                var4.add(new CustomShape(R.drawable.mask_circle, 1.0f, 0.5f, 1.0f, 1.0f));
                var3 = new ArrayList();
                var3.add(new CustomShape(R.drawable.mask_circle, 0.5f, 1.0f, 1.0f, 1.0f));
                var1.add(new FancyCollageGrid(null, null, var4, var3));
                var4 = new ArrayList();
                var4.add(new CustomShape(R.drawable.mask_circle, 0.5f, 1.0f, 1.0f, 1.0f));
                var3 = new ArrayList();
                var3.add(new CustomShape(R.drawable.mask_circle, 0.0f, 0.5f, 1.0f, 1.0f));
                var1.add(new FancyCollageGrid(null, null, var4, var3));
                var4 = new ArrayList();
                var4.add(new CustomShape(R.drawable.mask_circle, 0.0f, 0.5f, 1.0f, 1.0f));
                var3 = new ArrayList();
                var3.add(new CustomShape(R.drawable.mask_circle, 0.5f, 0.0f, 1.0f, 1.0f));
                var1.add(new FancyCollageGrid(null, null, var4, var3));
                break;
            case 47 /*47*/:
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_circle, -0.5f, -0.5f, 0.5f, 0.5f));
                var4.add(m12a(R.drawable.mask_circle, 0.5f, -0.5f, 1.5f, 0.5f));
                var4.add(m12a(R.drawable.mask_circle, 0.5f, 0.5f, 1.5f, 1.5f));
                var4.add(m12a(R.drawable.mask_circle, -0.5f, 0.5f, 0.5f, 1.5f));
                var1.add(new FancyCollageGrid(m14a(0.0f, 0.0f, 1.0f, 1.0f), null, null, var4));
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_circle, -0.5f, -0.5f, 0.5f, 0.5f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_circle, 0.5f, -0.5f, 1.5f, 0.5f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_circle, 0.5f, 0.5f, 1.5f, 1.5f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_circle, -0.5f, 0.5f, 0.5f, 1.5f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
            case 48 /*48*/:
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_heart, 0.0f, 0.0f, 1.0f, 1.0f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
            case 49 /*49*/:
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_heart, 0.5f, 0.5f, 1.0f, 1.0f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_heart, 0.0f, 0.0f, 0.85f, 0.85f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
            case 50 /*50*/:
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_star, 0.0f, 0.0f, 1.0f, 1.0f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
            case 51 /*51*/:
                var4 = new ArrayList();
                var4.add(new CustomShape(R.drawable.mask_s51_1, 0.32333332f, 0.5f, 0.64666665f, 1.0f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                var4 = new ArrayList();
                var4.add(new CustomShape(R.drawable.mask_s51_2, 0.6766667f, 0.5f, 0.64666665f, 1.0f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
            case 52 /*52*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(0));
                var2.add(new CollageOffset(0.5d, 0.0d, -0.5d * ((double) f17a), 1.0d));
                var2.add(new CollageOffset(0.3333333432674408d, 0.20000000298023224d, -0.5d, -0.5d));
                var2.add(new CollageOffset(0.0d, 0.25d, 1.0d, -0.5d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.3333333432674408d, 0.20000000298023224d, -0.5d, 0.5d));
                var2.add(new CollageOffset(0.0d, 0.25d, 1.0d, 0.5d));
                var2.add(new CollageOffset(0.0d, 0.75d, 1.0d, -0.5d));
                var2.add(new CollageOffset(0.3333333432674408d, 0.800000011920929d, -0.5d, -0.5d));
                var2.add(new CollageOffset(0.75d, 0.5d, -0.5d, 0.0d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(3));
                var2.add(new CollageOffset(0.0d, 0.75d, 1.0d, 0.5d));
                var2.add(new CollageOffset(0.3333333432674408d, 0.800000011920929d, -0.5d, 0.5d));
                var2.add(new CollageOffset(0.5d, 1.0d, -0.5d * ((double) f17a), -1.0d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(1));
                var2.add(new CollageOffset(0.5d, 0.0d, 0.5d, 1.0d));
                var2.add(new CollageOffset(0.3333333432674408d, 0.20000000298023224d, 0.5d, 0.0d));
                var2.add(new CollageOffset(0.75d, 0.5d, 0.5d, -0.5d));
                var2.add(CollageOffset.m9a(18));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(2));
                var2.add(new CollageOffset(0.5d, 1.0d, 0.5d, -1.0d));
                var2.add(new CollageOffset(0.3333333432674408d, 0.800000011920929d, 0.5d, 0.0d));
                var2.add(new CollageOffset(0.75d, 0.5d, 0.5d, 0.5d));
                var2.add(CollageOffset.m9a(19));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 53 /*53*/:
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.0f, 0.0f, 2.0f, (float) 1.0f));
                var2.add(new CollageOffset((float) 1.0f, 0.0f, -2.0f, (float) 1.0f));
                var2.add(new CollageOffset(0.75d, 0.35d, 0.0d, -1.0d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset((float) 1.0f, 0.0f, (float) -1f, 2.0f));
                var2.add(new CollageOffset((float) 1.0f, (float) 1.0f, (float) -1f, -2.0f));
                var2.add(new CollageOffset(0.65d, 0.75d, 1.0d, 0.0d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset((float) 1.0f, (float) 1.0f, -2.0f, (float) -1f));
                var2.add(new CollageOffset(0.0f, (float) 1.0f, 2.0f, (float) -1f));
                var2.add(new CollageOffset(0.25d, 0.65d, 0.0d, 1.0d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.0f, 0.0f, (float) 1.0f, 2.0f));
                var2.add(new CollageOffset(0.0f, (float) 1.0f, (float) 1.0f, -2.0f));
                var2.add(new CollageOffset(0.35d, 0.25d, -1.0d, 0.0d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.35d, 0.25d, 0.0d, 0.0d));
                var2.add(new CollageOffset(0.75d, 0.35d, 0.0d, 0.0d));
                var2.add(new CollageOffset(0.65d, 0.75d, 0.0d, 0.0d));
                var2.add(new CollageOffset(0.25d, 0.65d, 0.0d, 0.0d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 54 /*54*/:
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(0));
                var2.add(CollageOffset.m9a(16));
                var2.add(new CollageOffset(0.5d, 0.20000000298023224d, -0.5d, -0.5d));
                var2.add(new CollageOffset(0.0d, 0.3333333432674408d, 1.0d, -0.5d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(1));
                var2.add(CollageOffset.m9a(17));
                var2.add(new CollageOffset(0.5d, 0.20000000298023224d, 0.5d, -0.5d));
                var2.add(new CollageOffset(1.0d, 0.3333333432674408d, -1.0d, -0.5d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(2));
                var2.add(CollageOffset.m9a(20));
                var2.add(new CollageOffset(0.5d, 0.800000011920929d, 0.5d, 0.5d));
                var2.add(new CollageOffset(1.0d, 0.6666666865348816d, -1.0d, 0.5d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(CollageOffset.m9a(3));
                var2.add(CollageOffset.m9a(21));
                var2.add(new CollageOffset(0.5d, 0.800000011920929d, -0.5d, 0.5d));
                var2.add(new CollageOffset(0.0d, 0.6666666865348816d, 1.0d, 0.5d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.5d, 0.20000000298023224d, 0.0d, 0.5d));
                var2.add(new CollageOffset(1.0d, 0.3333333432674408d, -1.0d, 0.5d));
                var2.add(new CollageOffset(1.0d, 0.6666666865348816d, -1.0d, -0.5d));
                var2.add(new CollageOffset(0.5d, 0.800000011920929d, 0.0d, -0.5d));
                var2.add(new CollageOffset(0.0d, 0.6666666865348816d, 1.0d, -0.5d));
                var2.add(new CollageOffset(0.0d, 0.3333333432674408d, 1.0d, 0.5d));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 55 /*55*/:
                var4 = new ArrayList();
                var4.add(new CustomShape(R.drawable.mask_circle, 0.0f, 0.0f, 2.0f / f17a, 2.0f / f17a, 0.0f, 0.0f, 0.0f, 0.0f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                var4 = new ArrayList();
                var4.add(new CustomShape(R.drawable.mask_circle, 1.0f, 1.0f, 2.0f / f17a, 2.0f / f17a, 0.0f, 0.0f, 0.0f, 0.0f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
            case 56 /*56*/:
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_s56_1, 0.0f, 0.0f, 0.66f, 0.5f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_s56_2, 0.5f, 0.0f, 1.0f, 0.5f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_s56_3, 0.0f, 0.5f, 0.5f, 1.0f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_s56_4, 0.34f, 0.5f, 1.0f, 1.0f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
            case 57 /*57*/:
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_s57_1, 0.0f, 0.0f, 0.5f, 0.5f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_s57_2, 0.5f, 0.0f, 1.0f, 0.5f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_s57_1, 0.5f, 0.5f, 1.0f, 1.0f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_s57_2, 0.0f, 0.5f, 0.5f, 1.0f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
            case 58 /*58*/:
                var4 = new ArrayList();
                var4.add(m12a(R.drawable.mask_circle, 0.0f, 0.0f, 1.0f, 1.0f));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
            case 59 /*59*/:
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.5f, 0.1f, 0.0f, (float) 1.0f));
                var2.add(new CollageOffset(0.1f, 0.9f, (float) 1.0f, (float) -1f));
                var2.add(new CollageOffset(0.9f, 0.9f, (float) -1f, (float) -1f));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 60 /*60*/:
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.5f, 0.9f, 0.0f, (float) -1f));
                var2.add(new CollageOffset(0.1f, 0.1f, (float) 1.0f, (float) 1.0f));
                var2.add(new CollageOffset(0.9f, 0.1f, (float) -1f, (float) 1.0f));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 61 /*61*/:
                var2 = new ArrayList();
                var2.add(new CollageOffset(0.5f, 0.1f, 0.0f, (float) 1.0f));
                var2.add(new CollageOffset(0.9f, 0.5f, (float) -1f, 0.0f));
                var2.add(new CollageOffset(0.5f, 0.9f, 0.0f, (float) -1f));
                var2.add(new CollageOffset(0.1f, 0.5f, (float) 1.0f, 0.0f));
                var1.add(new FancyCollageGrid(var2, null, null, null));
                break;
            case 62 /*62*/:
                var4 = new ArrayList();
                var4.add(new CustomShape(R.drawable.mask_hexagonal_1, 0.5f, 0.5f, 0.783f, 0.9f, 0));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
            case 63 /*63*/:
                var4 = new ArrayList();
                var4.add(new CustomShape(R.drawable.mask_hexagonal_2, 0.5f, 0.5f, 0.9f, 0.783f, 0));
                var1.add(new FancyCollageGrid(null, null, var4, null));
                break;
        }
        return var1;
    }

    public RectF m16a() {
        if (CollageConst.lineThickness == this.f23j && this.f24k == CollageUtil.collageWidth && this.f25l == CollageUtil.collageHeight) {
            return this.rect;
        }
        this.f23j = CollageConst.lineThickness;
        this.f24k = CollageUtil.collageWidth;
        this.f25l = CollageUtil.collageHeight;
        this.rect.left = (float) CollageUtil.collageWidth;
        this.rect.right = 0.0f;
        this.rect.top = (float) CollageUtil.collageHeight;
        this.rect.bottom = 0.0f;
        float var3 = CollageConst.lineThickness * ((float) Math.max(CollageUtil.collageWidth, CollageUtil.collageHeight));
        if (this.f19f != null && this.f19f.size() > 0) {
            for (CollageOffset var7 : this.f19f) {
                float var1 = (float) ((var7.f0a * ((double) CollageUtil.collageWidth)) + (((double) var3) * var7.f2c));
                float var2 = (float) ((var7.f1b * ((double) CollageUtil.collageHeight)) + (((double) var3) * var7.f3d));
                this.rect.left = Math.min(this.rect.left, var1);
                this.rect.top = Math.min(this.rect.top, var2);
                this.rect.right = Math.max(this.rect.right, var1);
                this.rect.bottom = Math.max(this.rect.bottom, var2);
            }
        }
        if (this.f21h != null && this.f21h.size() > 0) {
            for (CustomShape var9 : this.f21h) {
                RectF var8 = new RectF();
                float var2, var1;
                if (var9.f7b == 0) {
                    var2 = (float) Math.min(CollageUtil.collageWidth, CollageUtil.collageHeight);
                } else {
                    var2 = (float) CollageUtil.collageWidth;
                }
                if (var9.f7b == 0) {
                    var1 = (float) Math.min(CollageUtil.collageWidth, CollageUtil.collageHeight);
                } else {
                    var1 = (float) CollageUtil.collageHeight;
                }
                var8.left = ((var9.f8c * ((float) CollageUtil.collageWidth)) - ((var9.f10e / 2.0f) * var2)) + (var9.f12g * var3);
                var8.top = ((var9.f9d * ((float) CollageUtil.collageHeight)) - ((var9.f11f / 2.0f) * var1)) + (var9.f13h * var3);
                var8.right = (((var9.f10e / 2.0f) * var2) + (var9.f8c * ((float) CollageUtil.collageWidth))) + (var9.f14i * var3);
                var8.bottom = ((var9.f15j * var3) + (var9.f9d * ((float) CollageUtil.collageHeight))) + (var1 * (var9.f11f / 2.0f));
                this.rect.left = Math.min(this.rect.left, var8.left);
                this.rect.top = Math.min(this.rect.top, var8.top);
                this.rect.right = Math.max(this.rect.right, var8.right);
                this.rect.bottom = Math.max(this.rect.bottom, var8.bottom);
            }
        }
        this.rect.left = Math.max(0.0f, this.rect.left);
        this.rect.top = Math.max(0.0f, this.rect.top);
        this.rect.right = Math.min((float) CollageUtil.collageWidth, this.rect.right);
        this.rect.bottom = Math.min((float) CollageUtil.collageHeight, this.rect.bottom);
        return this.rect;
    }

    public void m17a(Canvas canvas, RectF mRect, Paint pPoint) {
        Path path;
        int i;
        float var10;
        float var11;
        Xfermode xfMode;
        float var12 = CollageConst.lineThickness * Math.max(mRect.width(), mRect.height());
        if (this.f19f != null) {
            if (this.f19f.size() > 0) {
                path = new Path();
                double var4 = ((CollageOffset) this.f19f.get(0)).f0a;
                double var6 = (double) mRect.width();
                double var8 = (double) var12;
                path.moveTo((float) ((((CollageOffset) this.f19f.get(0)).f2c * var8) + (var6 * var4)), (float) ((((CollageOffset) this.f19f.get(0)).f1b * ((double) mRect.height())) + (((double) var12) * ((CollageOffset) this.f19f.get(0)).f3d)));
                i = 1;
                while (true) {
                    if (i >= this.f19f.size()) {
                        break;
                    }
                    double d = ((CollageOffset) this.f19f.get(i)).f0a;
                    double d2 = (double) var12;
                    path.lineTo((float) ((((CollageOffset) this.f19f.get(i)).f2c * d2) + (((double) mRect.width()) * d)), (float) ((((CollageOffset) this.f19f.get(i)).f1b * ((double) mRect.height())) + (((double) var12) * ((CollageOffset) this.f19f.get(i)).f3d)));
                    i++;
                }
                var4 = ((CollageOffset) this.f19f.get(0)).f0a;
                var6 = (double) mRect.width();
                var8 = (double) var12;
                path.lineTo((float) ((((CollageOffset) this.f19f.get(0)).f2c * var8) + (var6 * var4)), (float) ((((CollageOffset) this.f19f.get(0)).f1b * ((double) mRect.height())) + (((double) var12) * ((CollageOffset) this.f19f.get(0)).f3d)));
                canvas.drawPath(path, pPoint);
            }
        }
        if (this.f21h != null) {
            if (this.f21h.size() > 0) {
                for (CustomShape var16 : this.f21h) {
                    RectF var17 = new RectF();
                    if (var16.f7b == 0) {
                        var10 = Math.min(mRect.width(), mRect.height());
                    } else {
                        var10 = mRect.width();
                    }
                    if (var16.f7b == 0) {
                        var11 = Math.min(mRect.width(), mRect.height());
                    } else {
                        var11 = mRect.height();
                    }
                    var17.left = ((var16.f8c * mRect.width()) - ((var16.f10e / 2.0f) * var10)) + (var16.f12g * var12);
                    var17.top = ((var16.f9d * mRect.height()) - ((var16.f11f / 2.0f) * var11)) + (var16.f13h * var12);
                    float var13 = var16.f8c;
                    float var14 = mRect.width();
                    var17.right = (((var16.f10e / 2.0f) * var10) + (var13 * var14)) + (var16.f14i * var12);
                    var17.bottom = ((var16.f9d * mRect.height()) + ((var16.f11f / 2.0f) * var11)) + (var16.f15j * var12);
                    canvas.drawBitmap(var16.f16k, new Rect(0, 0, var16.f16k.getWidth(), var16.f16k.getHeight()), var17, pPoint);
                }
                if (var12 > 0.0f) {
                    xfMode = pPoint.getXfermode();
                    pPoint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
                    path = new Path();
                    path.moveTo(0.0f, 0.0f);
                    path.lineTo(0.0f, mRect.height());
                    path.lineTo(var12, mRect.height());
                    path.lineTo(var12, 0.0f);
                    path.lineTo(0.0f, 0.0f);
                    canvas.drawPath(path, pPoint);
                    path = new Path();
                    path.moveTo(0.0f, 0.0f);
                    path.lineTo(mRect.width(), 0.0f);
                    path.lineTo(mRect.width(), var12);
                    path.lineTo(0.0f, var12);
                    path.lineTo(0.0f, 0.0f);
                    canvas.drawPath(path, pPoint);
                    path = new Path();
                    path.moveTo(mRect.width(), 0.0f);
                    path.lineTo(mRect.width(), mRect.height());
                    path.lineTo(mRect.width() - var12, mRect.height());
                    path.lineTo(mRect.width() - var12, 0.0f);
                    path.lineTo(mRect.width(), 0.0f);
                    canvas.drawPath(path, pPoint);
                    path = new Path();
                    path.moveTo(0.0f, mRect.height());
                    path.lineTo(mRect.width(), mRect.height());
                    path.lineTo(mRect.width(), mRect.height() - var12);
                    path.lineTo(0.0f, mRect.height() - var12);
                    path.lineTo(0.0f, mRect.height());
                    canvas.drawPath(path, pPoint);
                    pPoint.setXfermode(xfMode);
                }
            }
        }
        xfMode = pPoint.getXfermode();
        pPoint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
        if (this.f20g != null) {
            if (this.f20g.size() > 0) {

                path = new Path();
                double var4 = ((CollageOffset) this.f20g.get(0)).f0a;
                double var8 = (double) var12;
                path.moveTo((float) ((((double) mRect.width()) * var4) - (((CollageOffset) this.f20g.get(0)).f2c * var8)), (float) ((((CollageOffset) this.f20g.get(0)).f1b * ((double) mRect.height())) - (((double) var12) * ((CollageOffset) this.f20g.get(0)).f3d)));
                i = 1;
                while (true) {
                    if (i >= this.f20g.size()) {
                        break;
                    }
                    double d = ((CollageOffset) this.f20g.get(i)).f0a;
                    double d2 = (double) var12;
                    path.lineTo((float) ((((double) mRect.width()) * d) - (((CollageOffset) this.f20g.get(i)).f2c * d2)), (float) ((((CollageOffset) this.f20g.get(i)).f1b * ((double) mRect.height())) - (((double) var12) * ((CollageOffset) this.f20g.get(i)).f3d)));
                    i++;
                }
                var4 = ((CollageOffset) this.f20g.get(0)).f0a;
                var8 = (double) var12;
                path.lineTo((float) ((((double) mRect.width()) * var4) - (((CollageOffset) this.f20g.get(0)).f2c * var8)), (float) ((((CollageOffset) this.f20g.get(0)).f1b * ((double) mRect.height())) - (((double) var12) * ((CollageOffset) this.f20g.get(0)).f3d)));
                canvas.drawPath(path, pPoint);
            }
        }
        if (this.f22i != null) {
            if (this.f22i.size() > 0) {
                for (CustomShape var24 : this.f22i) {
                    RectF var18 = new RectF();
                    if (var24.f7b == 0) {
                        var10 = Math.min(mRect.width(), mRect.height());
                    } else {
                        var10 = mRect.width();
                    }
                    if (var24.f7b == 0) {
                        var11 = Math.min(mRect.width(), mRect.height());
                    } else {
                        var11 = mRect.height();
                    }
                    var18.left = ((var24.f8c * mRect.width()) - ((var24.f10e / 2.0f) * var10)) - (var24.f12g * var12);
                    var18.top = ((var24.f9d * mRect.height()) - ((var24.f11f / 2.0f) * var11)) - (var24.f13h * var12);
                    float var13 = var24.f8c;
                    float var14 = mRect.width();
                    var18.right = (((var24.f10e / 2.0f) * var10) + (var13 * var14)) - (var24.f14i * var12);
                    var18.bottom = ((var24.f9d * mRect.height()) + ((var24.f11f / 2.0f) * var11)) - (var24.f15j * var12);
                    canvas.drawBitmap(var24.f16k, new Rect(0, 0, var24.f16k.getWidth(), var24.f16k.getHeight()), var18, pPoint);
                }
            }
        }
        pPoint.setXfermode(xfMode);
    }

    public boolean m18a(Point var1) {
        bitmap = Bitmap.createBitmap(200, 200, Config.ARGB_8888);
        mCanvas = new Canvas(bitmap);
        mPaint.setAntiAlias(true);
        mCanvas.drawARGB(0, 0, 0, 0);
        mPaint.setColor(SupportMenu.CATEGORY_MASK);
        m17a(mCanvas, new RectF(0.0f, 0.0f, 200.0f, 200.0f), mPaint);
        int var3 = (int) (((float) var1.x) * (200.0f / ((float) CollageUtil.collageWidth)));
        int var4 = (int) (((float) var1.y) * (200.0f / ((float) CollageUtil.collageHeight)));
        if (var3 < bitmap.getWidth() && var4 < bitmap.getHeight()) {
            int var2 = var3;
            if (var3 < 0) {
                var2 = 0;
            }
            var3 = var4;
            if (var4 < 0) {
                var3 = 0;
            }
            if (bitmap.getPixel(var2, var3) != 0) {
                return true;
            }
        }
        return false;
    }
}
