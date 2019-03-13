package nonworkingcode.grid.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.view.ViewCompat;

import com.formationapps.nameart.R;

import java.io.File;

import formationapps.helper.stickers.StickerConst;
import nonworkingcode.grid.custominterface.CollageInterface;

public class CollageConst {
    public static Context appContext;
    public static int backgroundColor;
    public static Drawable backgroundDrawable;
    public static int collageBaseWidth;
    public static Bitmap[] collageBitmaps = new Bitmap[12];
    public static Uri[] collageIds = new Uri[12];
    ;
    public static CollageInterface collageView;
    public static int[][] collages;
    public static float cornerRadius;
    private static Bitmap defaultBitmap;
    public static Boolean[] effectApplied = new Boolean[12];
    public static int frameBorderAssetId;
    public static File frameAssetsPath;
    public static int gridIndex;
    public static float lineThickness;
    public static int ratioIndex;
    public static float shadow;
    public static int statusBarHeight;

    static void initValues() {
        collageView = null;
        statusBarHeight = 0;
        int[][] r0 = new int[10][];
        r0[0] = new int[]{316, 317, 318, 319, 0, 304, 306, 314, 315};
        r0[1] = new int[]{268, 285, 305, 307, 311, 2, 269, 270, 284, 1, 32, 33, 267};
        r0[2] = new int[]{259, 262, 263, 264, 275, 296, 297, 298, 299, 276, 282, 286, 287, 288, 289, 290, 291, 292, 293, 294, 295, 3, 4, 5, 6, 7, 8, 28, 29, 30, 31, 256, 257, 258};
        r0[3] = new int[]{261, 271, 272, 280, 281, 283, 300, 11, 12, 13, 9, 10, 16, 17, 18, 19, 260, 301, 302, 312, 313};
        r0[4] = new int[]{273, 274, 277, 278, 279, 303, 308, 309, 310, 20, 21, 22, 14, 15, 23, 24, 25, 26, 27, 34, 35, 35, 37, 38, 39, 40, 41, 42, 43, 54, 55};
        r0[5] = new int[]{46, 47, 48, 44, 45, 49, 50, 51, 52, 53};
        r0[6] = new int[]{63, 64};
        r0[7] = new int[]{56, 57};
        r0[8] = new int[]{62};
        r0[9] = new int[]{59, 60, 58, 61};
        collages = r0;
        gridIndex = -1;
        cornerRadius = 0.0f;
        lineThickness = 0.02f;
        shadow = 0.0f;
        backgroundDrawable = null;
        backgroundColor = 0;
        frameBorderAssetId = -1;
        frameAssetsPath = null;
        ratioIndex = 0;
        collageBaseWidth = 0;
        defaultBitmap = null;
    }

    public static void init() {
        initValues();
        double factor;
        reset();
        double width = (double) Math.min((((float) CollageUtil.screenHeight) * 440.0f) / 800.0f,
                ((float) CollageUtil.screenWidth) - CollageUtil.dpToPx(40.0f));
        if (CollageUtil.isTablet(appContext)) {
            factor = 0.8d;
        } else {
            factor = 1.0d;
        }
        collageBaseWidth = (int) (factor * width);
        CollageUtil.collageWidth = collageBaseWidth;
        CollageUtil.collageHeight = collageBaseWidth;
        defaultBitmap = BitmapFactory.decodeResource(appContext.getResources(), R.drawable.ic_add_image);
        collageView = null;
        StickerConst.drawingLayout = null;
    }
    public static Bitmap reInitDefaultBitmap(){
        if(defaultBitmap!=null&&!defaultBitmap.isRecycled()){
            //defaultBitmap.recycle();
        }
        defaultBitmap = BitmapFactory.decodeResource(appContext.getResources(), R.drawable.ic_add_image);
        return defaultBitmap;
    }

    public static void reset() {
        appContext = CollageUtil._appContext;
        gridIndex = -1;
        cornerRadius = 0.0f;
        lineThickness = 0.02f;
        shadow = 0.0f;
        backgroundDrawable = null;
        backgroundColor = 0;
        frameBorderAssetId = -1;
        ratioIndex = 0;
        collageBaseWidth = 0;
    }

    public static Bitmap getShadowBitmap(Bitmap source, float shadow) {
        Bitmap bitmap = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(0, Mode.CLEAR);
        Paint paint = new Paint();
        paint.setMaskFilter(new BlurMaskFilter(shadow, Blur.NORMAL));
        int[] var4 = new int[2];
        Bitmap alphaBtm = source.extractAlpha(paint, var4);
        Paint paint1 = new Paint();
        paint1.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawBitmap(alphaBtm, (float) var4[0], (float) var4[1], paint1);
        //alphaBtm.recycle();
        canvas.drawBitmap(source, 0.0f, 0.0f, null);
        return bitmap;
    }

    public static float getRatio() {
        switch (ratioIndex) {
            case 0:
                return 1.0f;
            case 1 /*1*/:
                return 1.5f;
            case 2 /*2*/:
                return 0.6666667f;
            case 3 /*3*/:
                return 1.3333334f;
            case 4 /*4*/:
                return 0.75f;
            case 5 /*5*/:
                return 2.0f;
            case 6 /*6*/:
                return 0.5f;
            default:
                return 1.0f;
        }
    }

    public static int getImageSize() {
        int i = 0;
        for (Uri id : collageIds) {
            if (id != null) {
                i++;
            }
        }
        if (i > 0) {
            return i - 1;
        }
        return 1;
    }

    public static void destroy() {
        for (Bitmap btm : collageBitmaps) {
            if (!(btm == null || btm.isRecycled())) {
                btm.recycle();
            }
        }
        gridIndex = -1;
        collageView = null;
        StickerConst.drawingLayout = null;
        System.gc();
        collageBitmaps = new Bitmap[12];
        collageIds = new Uri[12];
    }
}
