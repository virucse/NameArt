package nonworkingcode.fcrop.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


public final class ScalingUtilities {

    public static Bitmap m113a(Bitmap bitmap, int i, int i2, ScalingEnum scalingUtilities) {
        Rect rect;
        Rect rect2;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (scalingUtilities == ScalingEnum.CROP) {
            float f = ((float) i) / ((float) i2);
            int i3;
            if (((float) width) / ((float) height) > f) {
                i3 = (int) (((float) height) * f);
                width = (width - i3) / 2;
                rect = new Rect(width, 0, i3 + width, height);
            } else {
                i3 = (int) (((float) width) / f);
                height = (height - i3) / 2;
                rect = new Rect(0, height, width, i3 + height);
            }
        } else {
            rect = new Rect(0, 0, width, height);
        }
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        if (scalingUtilities == ScalingEnum.FIT) {
            float f2 = ((float) width) / ((float) height);
            rect2 = f2 > ((float) i) / ((float) i2) ? new Rect(0, 0, i, (int) (((float) i) / f2)) : new Rect(0, 0, (int) (f2 * ((float) i2)), i2);
        } else {
            rect2 = new Rect(0, 0, i, i2);
        }
        Bitmap createBitmap = Bitmap.createBitmap(rect2.width(), rect2.height(), Config.ARGB_8888);
        new Canvas(createBitmap).drawBitmap(bitmap, rect, rect2, new Paint(2));
        return createBitmap;
    }

    public enum ScalingEnum {
        CROP,
        FIT
    }
}
