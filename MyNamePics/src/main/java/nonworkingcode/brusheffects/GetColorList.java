package nonworkingcode.brusheffects;

import android.graphics.Color;

import java.util.Random;

public class GetColorList {
    private static String[] colors;

    static {
        colors = new String[]{"#F44336", "#F44336", "#9C27B0", "#2196F3", "#3F51B5", "#673AB7", "#03A9F4", "#00BCD4",
                "#009688", "#4CAF50", "#CDDC39", "#8BC34A", "#FFEB3B", "#FFC107", "#FF9800", "#FF5722"};
    }

    public static int getRandomColor() {
        return Color.parseColor(colors[new Random().nextInt(colors.length)]);
    }
}
