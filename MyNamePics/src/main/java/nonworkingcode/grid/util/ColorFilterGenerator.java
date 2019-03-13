package nonworkingcode.grid.util;

import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

public class ColorFilterGenerator {
    public static ColorFilter adjustHue(float hue) {
        ColorMatrix matrix = new ColorMatrix();
        adjustHue(matrix, hue);
        return new ColorMatrixColorFilter(matrix);
    }

    public static void adjustHue(ColorMatrix matrix, float hue) {
        float value = (cleanValue(hue, 180.0f) / 180.0f) * 3.1415927f;
        if (value != 0.0f) {
            hue = (float) Math.cos((double) value);
            value = (float) Math.sin((double) value);
            matrix.postConcat(new ColorMatrix(new float[]{((0.787f * hue) + 0.213f) + (-0.213f * value),
                    ((-0.715f * hue) + 0.715f) + (-0.715f * value), ((-0.072f * hue) + 0.072f) + (0.928f * value),
                    0.0f, 0.0f, ((-0.213f * hue) + 0.213f) + (0.143f * value), ((0.28500003f * hue) + 0.715f) + (0.14f * value),
                    ((-0.072f * hue) + 0.072f) + (-0.283f * value), 0.0f, 0.0f, ((-0.213f * hue) + 0.213f) + (-0.787f * value),
                    ((-0.715f * hue) + 0.715f) + (value * 0.715f), ((0.928f * hue) + 0.072f) + (value * 0.072f), 0.0f, 0.0f, 0.0f,
                    0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f}));
        }
    }

    protected static float cleanValue(float var0, float var1) {
        return Math.min(var1, Math.max(-var1, var0));
    }
}
