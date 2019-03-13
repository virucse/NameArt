package nonworkingcode.grid.views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import nonworkingcode.grid.util.CollageUtil;

public class CustomShape {
    public int f7b;
    public float f8c;
    public float f9d;
    public float f10e;
    public float f11f;
    public float f12g;
    public float f13h;
    public float f14i;
    public float f15j;
    public Bitmap f16k;
    public int resId;

    public CustomShape(int res, float var2, float var3, float var4, float var5) {
        this.resId = 0;
        this.f7b = 1;
        this.f8c = 0.5f;
        this.f9d = 0.5f;
        this.f10e = 1.0f;
        this.f11f = 1.0f;
        this.f12g = 1.0f;
        this.f13h = 1.0f;
        this.f14i = -1f;
        this.f15j = -1f;
        this.f16k = null;
        this.resId = res;
        this.f16k = BitmapFactory.decodeResource(CollageUtil._appContext.getResources(), this.resId);
        this.f8c = var2;
        this.f9d = var3;
        this.f10e = var4;
        this.f11f = var5;
    }

    public CustomShape(int res, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
        this.resId = 0;
        this.f7b = 1;
        this.f8c = 0.5f;
        this.f9d = 0.5f;
        this.f10e = 1.0f;
        this.f11f = 1.0f;
        this.f12g = 1.0f;
        this.f13h = 1.0f;
        this.f14i = -1f;
        this.f15j = -1f;
        this.f16k = null;
        this.resId = res;
        this.f16k = BitmapFactory.decodeResource(CollageUtil._appContext.getResources(), this.resId);
        this.f8c = var2;
        this.f9d = var3;
        this.f10e = var4;
        this.f11f = var5;
        this.f12g = var6;
        this.f13h = var7;
        this.f14i = var8;
        this.f15j = var9;
    }

    public CustomShape(int res, float var2, float var3, float var4, float var5, int var6) {
        this.resId = 0;
        this.f7b = 1;
        this.f8c = 0.5f;
        this.f9d = 0.5f;
        this.f10e = 1.0f;
        this.f11f = 1.0f;
        this.f12g = 1.0f;
        this.f13h = 1.0f;
        this.f14i = -1f;
        this.f15j = -1f;
        this.f16k = null;
        this.resId = res;
        this.f16k = BitmapFactory.decodeResource(CollageUtil._appContext.getResources(), this.resId);
        this.f8c = var2;
        this.f9d = var3;
        this.f10e = var4;
        this.f11f = var5;
        this.f7b = var6;
    }
}
